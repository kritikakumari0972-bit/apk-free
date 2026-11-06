package com.example.core.gaming

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.example.core.hardware.HardwareMonitor
import com.example.core.network.NetworkDiagnostics
import com.example.data.GamingSessionDao
import com.example.data.GamingSessionRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ActiveGamingSession(
    val gameName: String,
    val packageName: String,
    val startTimeMs: Long,
    val durationSeconds: Long,
    val currentRamAvailMb: Long,
    val currentCpuUsagePct: Int,
    val currentTempCelsius: Float,
    val currentPingMs: Int,
    val peakTempCelsius: Float,
    val profileName: String,
    val thermalWarningActive: Boolean
)

data class LagDiagnosticReport(
    val memoryPressureStatus: String,
    val isMemoryCritical: Boolean,
    val thermalStatus: String,
    val isThermalThrottling: Boolean,
    val networkLatencyStatus: String,
    val isNetworkLagging: Boolean,
    val storageHeadroomStatus: String,
    val recommendations: List<String>
)

class GamingSessionManager(
    private val context: Context,
    private val hardwareMonitor: HardwareMonitor,
    private val networkDiagnostics: NetworkDiagnostics,
    private val gamingSessionDao: GamingSessionDao
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    private val scope = CoroutineScope(Dispatchers.Default)
    private var trackingJob: Job? = null

    private val _activeSessionFlow = MutableStateFlow<ActiveGamingSession?>(null)
    val activeSessionFlow: StateFlow<ActiveGamingSession?> = _activeSessionFlow.asStateFlow()

    private val ramSamples = mutableListOf<Long>()
    private val pingSamples = mutableListOf<Int>()
    private var peakObservedTemp = 0.0f

    fun isDndAccessGranted(): Boolean {
        return notificationManager?.isNotificationPolicyAccessGranted == true
    }

    fun openDndSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun setDndMode(enabled: Boolean): Boolean {
        if (!isDndAccessGranted()) return false
        val nm = notificationManager ?: return false
        return try {
            if (enabled) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
            } else {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun startSession(gameName: String, packageName: String, profileName: String = "Balanced") {
        trackingJob?.cancel()
        ramSamples.clear()
        pingSamples.clear()
        peakObservedTemp = 0.0f

        val startMs = System.currentTimeMillis()
        val initialBat = hardwareMonitor.getBatteryInfo()
        peakObservedTemp = initialBat.temperatureCelsius

        trackingJob = scope.launch {
            while (isActive) {
                val ram = hardwareMonitor.getRamInfo()
                val cpu = hardwareMonitor.getCpuInfo()
                val battery = hardwareMonitor.getBatteryInfo()
                val ping = networkDiagnostics.measureSocketPing()

                val availMb = ram.availableBytes / (1024 * 1024)
                ramSamples.add(availMb)
                if (battery.temperatureCelsius > peakObservedTemp) {
                    peakObservedTemp = battery.temperatureCelsius
                }
                if (ping.isSuccess) {
                    pingSamples.add(ping.latencyMs.toInt())
                }

                val duration = (System.currentTimeMillis() - startMs) / 1000L

                _activeSessionFlow.value = ActiveGamingSession(
                    gameName = gameName,
                    packageName = packageName,
                    startTimeMs = startMs,
                    durationSeconds = duration,
                    currentRamAvailMb = availMb,
                    currentCpuUsagePct = cpu.cpuUsagePercent,
                    currentTempCelsius = battery.temperatureCelsius,
                    currentPingMs = if (ping.isSuccess) ping.latencyMs.toInt() else -1,
                    peakTempCelsius = peakObservedTemp,
                    profileName = profileName,
                    thermalWarningActive = battery.isOverheating
                )

                delay(2000L)
            }
        }
    }

    suspend fun stopSession(): GamingSessionRecord? {
        val current = _activeSessionFlow.value
        trackingJob?.cancel()
        trackingJob = null
        _activeSessionFlow.value = null

        if (current == null) return null

        val avgRam = if (ramSamples.isNotEmpty()) ramSamples.average().toLong() else current.currentRamAvailMb
        val avgPing = if (pingSamples.isNotEmpty()) pingSamples.average().toInt() else current.currentPingMs

        val record = GamingSessionRecord(
            gameName = current.gameName,
            packageName = current.packageName,
            durationSeconds = current.durationSeconds,
            peakTemperatureCelsius = peakObservedTemp,
            avgAvailableRamMb = avgRam,
            avgPingMs = avgPing,
            profileName = current.profileName
        )

        gamingSessionDao.insertSession(record)
        return record
    }

    suspend fun evaluateLagDiagnostics(): LagDiagnosticReport {
        val ram = hardwareMonitor.getRamInfo()
        val cpu = hardwareMonitor.getCpuInfo()
        val battery = hardwareMonitor.getBatteryInfo()
        val storage = hardwareMonitor.getStorageInfo()
        val ping = networkDiagnostics.measureSocketPing()

        val recs = mutableListOf<String>()

        val isMemCritical = ram.isLowMemory || ram.usagePercentage > 85
        val memStatus = if (isMemCritical) {
            recs.add("RAM is under high pressure (${ram.usagePercentage}% used). Consider closing heavy background tasks.")
            "High Pressure (${ram.usagePercentage}%)"
        } else {
            "Optimal (${ram.usagePercentage}% used, ${ram.availableBytes / (1024 * 1024)} MB available)"
        }

        val isThermalThrottling = battery.temperatureCelsius > 41.0f || battery.isOverheating
        val thermalStatus = if (isThermalThrottling) {
            recs.add("Battery temperature is elevated (${"%.1f".format(battery.temperatureCelsius)}°C). Hardware thermal throttling may lower GPU clocks.")
            "Elevated (${"%.1f".format(battery.temperatureCelsius)}°C)"
        } else {
            "Cool (${"%.1f".format(battery.temperatureCelsius)}°C)"
        }

        val isNetworkLagging = !ping.isSuccess || ping.latencyMs > 120
        val netStatus = when {
            !ping.isSuccess -> {
                recs.add("Network probe failed (Connection timed out or unreachable). Check your active internet connection.")
                "Connection Failed / Unreachable"
            }
            ping.latencyMs > 120 -> {
                recs.add("Elevated network ping (${ping.latencyMs} ms). Connect to a 5 GHz Wi-Fi or lower-latency network to reduce input lag.")
                "High Latency (${ping.latencyMs} ms)"
            }
            else -> {
                "${ping.latencyMs} ms (Optimal)"
            }
        }

        val storageCrit = storage.usagePercentage > 90
        val storageStatus = if (storageCrit) {
            recs.add("Storage capacity is over 90% full. Clean temporary files to prevent swap starvation.")
            "Low Storage Headroom (${storage.usagePercentage}%)"
        } else {
            "${100 - storage.usagePercentage}% Free (${storage.availableBytes / (1024 * 1024 * 1024)} GB)"
        }

        if (recs.isEmpty()) {
            recs.add("System environment is in prime condition for gaming.")
            recs.add("Enable NovaBoost Gaming DND mode to prevent notifications during gameplay.")
        }

        return LagDiagnosticReport(
            memoryPressureStatus = memStatus,
            isMemoryCritical = isMemCritical,
            thermalStatus = thermalStatus,
            isThermalThrottling = isThermalThrottling,
            networkLatencyStatus = netStatus,
            isNetworkLagging = isNetworkLagging,
            storageHeadroomStatus = storageStatus,
            recommendations = recs
        )
    }
}
