package com.example.core.hardware

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Debug
import android.os.Environment
import android.os.PowerManager
import android.os.Process
import android.os.StatFs
import java.io.File
import java.io.RandomAccessFile

sealed class MetricResult<out T> {
    object Loading : MetricResult<Nothing>()
    data class Available<out T>(val data: T) : MetricResult<T>()
    data class Unavailable(val reason: String) : MetricResult<Nothing>()
    data class PermissionRequired(val permission: String) : MetricResult<Nothing>()
    data class Error(val message: String) : MetricResult<Nothing>()
}

data class RamInfo(
    val totalBytes: Long,
    val availableBytes: Long,
    val usedBytes: Long,
    val usagePercentage: Int,
    val isLowMemory: Boolean,
    val thresholdBytes: Long,
    val processUsedBytes: Long,
    val cachedBytes: Long,
    val swapTotalBytes: Long,
    val swapFreeBytes: Long,
    val zRamAccessible: Boolean,
    val zRamUsageString: String,
    val swapSourceType: String,
    val swapSupportStatus: String
)

data class StorageInfo(
    val totalBytes: Long,
    val availableBytes: Long,
    val usedBytes: Long,
    val usagePercentage: Int,
    val path: String
)

data class CpuCoreFreq(
    val coreIndex: Int,
    val curFreqKHz: Long,
    val maxFreqKHz: Long
)

data class CpuInfo(
    val processorCount: Int,
    val architecture: String,
    val hardwareName: String,
    val socModel: String,
    val isSystemUsageAvailable: Boolean,
    val cpuUsagePercent: Int,
    val cpuUsageMode: String,
    val processCpuUsagePercent: Int,
    val coreFrequencies: List<CpuCoreFreq>,
    val features: List<String>
)

data class BatteryInfo(
    val isAvailable: Boolean,
    val levelPercent: Int,
    val temperatureCelsius: Float,
    val voltageMv: Int,
    val status: String,
    val health: String,
    val powerSource: String,
    val technology: String,
    val thermalStatus: String,
    val isOverheating: Boolean
)

data class DisplayInfo(
    val resolution: String,
    val densityDpi: Int,
    val refreshRateHz: Float
)

data class NetworkStatusInfo(
    val isConnected: Boolean,
    val transportType: String,
    val isVpnActive: Boolean,
    val downstreamBandwidthKbps: Int
)

class HardwareMonitor(private val context: Context) {

    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    private val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private var lastCpuTotalJiffies: Long = 0L
    private var lastCpuWorkJiffies: Long = 0L

    private var lastProcessUtime: Long = 0L
    private var lastProcessStime: Long = 0L
    private var lastProcessSampleTimeMs: Long = 0L

    fun getRamInfo(): RamInfo {
        val memInfo = ActivityManager.MemoryInfo()
        activityManager?.getMemoryInfo(memInfo)

        var total = memInfo.totalMem
        if (total <= 0L) {
            total = Runtime.getRuntime().maxMemory()
        }
        var avail = memInfo.availMem
        if (avail <= 0L) {
            avail = Runtime.getRuntime().freeMemory()
        }
        val used = (total - avail).coerceAtLeast(0L)
        val usagePct = if (total > 0) ((used.toDouble() / total) * 100).toInt() else 0

        // NovaBoost process memory footprint
        val runtimeUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

        // Parse Linux /proc/meminfo for Swap, zRAM, and Cached buffers
        var swapTotal = 0L
        var swapFree = 0L
        var cached = 0L
        var buffers = 0L
        var zramActiveFound = false
        var zramUsageStr = "zRAM information unavailable on this device"
        var swapSource = "Not detected or protected by kernel"
        var swapStatus = "Kernel Restricted"

        try {
            val meminfoFile = File("/proc/meminfo")
            if (meminfoFile.exists() && meminfoFile.canRead()) {
                meminfoFile.forEachLine { line ->
                    val parts = line.split(":").map { it.trim() }
                    if (parts.size >= 2) {
                        val key = parts[0]
                        val valueStr = parts[1].split("\\s+".toRegex())[0]
                        val kbVal = valueStr.toLongOrNull() ?: 0L
                        when (key) {
                            "SwapTotal" -> swapTotal = kbVal * 1024L
                            "SwapFree" -> swapFree = kbVal * 1024L
                            "Cached" -> cached = kbVal * 1024L
                            "Buffers" -> buffers = kbVal * 1024L
                            "ZRam", "Zswap" -> {
                                zramActiveFound = true
                                zramUsageStr = "${kbVal / 1024} MB (active)"
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Protected on some OEM SELinux policies
        }

        // Check /proc/swaps if accessible
        try {
            val swapsFile = File("/proc/swaps")
            if (swapsFile.exists() && swapsFile.canRead()) {
                swapsFile.forEachLine { line ->
                    if (line.contains("zram", ignoreCase = true)) {
                        zramActiveFound = true
                        swapSource = "Linux zRAM (Compressed In-Memory)"
                        swapStatus = "Active (zRAM)"
                    } else if (line.contains("partition", ignoreCase = true) || line.contains("file", ignoreCase = true)) {
                        swapSource = "Storage-backed Swap Partition"
                        swapStatus = "Active (Partition)"
                    }
                }
            }
        } catch (_: Exception) {}

        if (swapTotal > 0) {
            val swapUsed = swapTotal - swapFree
            if (swapSource == "Not detected or protected by kernel") {
                // Determine whether it is zRAM or partition
                if (File("/sys/block/zram0").exists()) {
                    zramActiveFound = true
                    swapSource = "Linux zRAM (/sys/block/zram0)"
                    swapStatus = "Active"
                } else {
                    swapSource = "System Swap"
                    swapStatus = "Active"
                }
            }
            zramUsageStr = "Total: ${swapTotal / (1024 * 1024)} MB | Active: ${swapUsed / (1024 * 1024)} MB"
        } else {
            swapSource = "STORAGE SWAP ACTIVATION: NOT AVAILABLE (Requires Root / Kernel Privilege)"
            swapStatus = "Not Active"
        }

        return RamInfo(
            totalBytes = total,
            availableBytes = avail,
            usedBytes = used,
            usagePercentage = usagePct,
            isLowMemory = memInfo.lowMemory,
            thresholdBytes = memInfo.threshold,
            processUsedBytes = runtimeUsed.coerceAtLeast(0L),
            cachedBytes = cached + buffers,
            swapTotalBytes = swapTotal,
            swapFreeBytes = swapFree,
            zRamAccessible = zramActiveFound,
            zRamUsageString = zramUsageStr,
            swapSourceType = swapSource,
            swapSupportStatus = swapStatus
        )
    }

    fun getStorageInfo(): StorageInfo {
        val path = Environment.getDataDirectory()
        var total = 0L
        var avail = 0L

        try {
            val stat = StatFs(path.path)
            total = stat.totalBytes
            avail = stat.availableBytes
        } catch (_: Exception) {}

        if (total <= 0L) {
            total = path.totalSpace
        }
        if (avail <= 0L) {
            avail = path.freeSpace
        }

        val used = (total - avail).coerceAtLeast(0L)
        val pct = if (total > 0) ((used.toDouble() / total) * 100).toInt() else 0

        return StorageInfo(
            totalBytes = total,
            availableBytes = avail,
            usedBytes = used,
            usagePercentage = pct,
            path = path.absolutePath
        )
    }

    fun getCpuInfo(): CpuInfo {
        val cores = Runtime.getRuntime().availableProcessors()
        val arch = Build.SUPPORTED_ABIS.firstOrNull() ?: Build.CPU_ABI ?: "ARM"
        val hardware = Build.HARDWARE
        val soc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Build.SOC_MODEL
        } else {
            Build.BOARD
        }

        val freqs = mutableListOf<CpuCoreFreq>()
        for (i in 0 until cores) {
            val curFreq = readFrequency("/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq")
            val maxFreq = readFrequency("/sys/devices/system/cpu/cpu$i/cpufreq/cpuinfo_max_freq")
            freqs.add(CpuCoreFreq(i, curFreq, maxFreq))
        }

        // Measure system CPU usage via /proc/stat delta calculation
        val statUsage = sampleProcStatCpuUsage()
        val isSystemAvailable = statUsage != null
        val usage = statUsage ?: 0
        val mode = if (isSystemAvailable) {
            "Direct kernel /proc/stat"
        } else {
            "DEVICE CPU UTILIZATION UNAVAILABLE (Restricted by Android SELinux)"
        }

        // Measure NovaBoost's own process CPU usage via /proc/self/stat
        val processUsage = sampleProcessCpuUsage(cores)

        val features = mutableListOf<String>()
        try {
            val cpuinfo = File("/proc/cpuinfo")
            if (cpuinfo.exists() && cpuinfo.canRead()) {
                cpuinfo.forEachLine { line ->
                    if (line.startsWith("Features") || line.startsWith("flags")) {
                        val tokens = line.substringAfter(":").trim().split("\\s+".toRegex())
                        features.addAll(tokens.take(8))
                    }
                }
            }
        } catch (_: Exception) {}

        return CpuInfo(
            processorCount = cores,
            architecture = arch,
            hardwareName = hardware,
            socModel = soc,
            isSystemUsageAvailable = isSystemAvailable,
            cpuUsagePercent = usage.coerceIn(0, 100),
            cpuUsageMode = mode,
            processCpuUsagePercent = processUsage,
            coreFrequencies = freqs,
            features = features.distinct()
        )
    }

    private fun readFrequency(path: String): Long {
        return try {
            val f = File(path)
            if (f.exists() && f.canRead()) {
                f.readText().trim().toLongOrNull() ?: 0L
            } else 0L
        } catch (_: Exception) {
            0L
        }
    }

    private fun sampleProcStatCpuUsage(): Int? {
        return try {
            val reader = RandomAccessFile("/proc/stat", "r")
            val line = reader.readLine() ?: return null
            reader.close()
            val toks = line.split("\\s+".toRegex())
            if (toks.size < 5) return null

            val user = toks[1].toLong()
            val nice = toks[2].toLong()
            val system = toks[3].toLong()
            val idle = toks[4].toLong()
            val iowait = if (toks.size > 5) toks[5].toLong() else 0L
            val irq = if (toks.size > 6) toks[6].toLong() else 0L
            val softirq = if (toks.size > 7) toks[7].toLong() else 0L

            val work = user + nice + system + irq + softirq
            val total = work + idle + iowait

            val deltaTotal = total - lastCpuTotalJiffies
            val deltaWork = work - lastCpuWorkJiffies

            lastCpuTotalJiffies = total
            lastCpuWorkJiffies = work

            if (deltaTotal > 0 && lastCpuTotalJiffies > 0) {
                ((deltaWork.toDouble() / deltaTotal) * 100).toInt().coerceIn(0, 100)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun sampleProcessCpuUsage(coreCount: Int): Int {
        return try {
            val statFile = File("/proc/self/stat")
            if (!statFile.exists() || !statFile.canRead()) return 0
            val content = statFile.readText()
            val parts = content.split(" ")
            if (parts.size > 14) {
                val utime = parts[13].toLongOrNull() ?: 0L
                val stime = parts[14].toLongOrNull() ?: 0L
                val now = System.currentTimeMillis()

                val timeDelta = now - lastProcessSampleTimeMs
                val tickDelta = (utime + stime) - (lastProcessUtime + lastProcessStime)

                lastProcessUtime = utime
                lastProcessStime = stime
                lastProcessSampleTimeMs = now

                if (timeDelta > 0 && lastProcessSampleTimeMs > 0 && tickDelta >= 0) {
                    // 100 jiffies per second on standard Linux kernel
                    val cpuTimeMs = tickDelta * 10L
                    val pct = ((cpuTimeMs.toDouble() / (timeDelta * coreCount)) * 100).toInt()
                    pct.coerceIn(0, 100)
                } else 0
            } else 0
        } catch (_: Exception) {
            0
        }
    }

    fun getBatteryInfo(): BatteryInfo {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        if (intent == null) {
            return BatteryInfo(
                isAvailable = false,
                levelPercent = 0,
                temperatureCelsius = 0.0f,
                voltageMv = 0,
                status = "Unavailable",
                health = "Unavailable",
                powerSource = "Unknown",
                technology = "Unknown",
                thermalStatus = "Unavailable",
                isOverheating = false
            )
        }

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val pct = if (level >= 0 && scale > 0) (level * 100 / scale) else 0

        val tempTenths = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val tempCelsius = tempTenths / 10.0f
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)

        val statusInt = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val status = when (statusInt) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
            else -> "Unknown"
        }

        val healthInt = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val health = when (healthInt) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failed"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Normal"
        }

        val pluggedInt = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        val powerSource = when (pluggedInt) {
            BatteryManager.BATTERY_PLUGGED_AC -> "AC Wall Charger"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB Port"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
            else -> "Battery Power"
        }

        val tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion"

        var thermalStatusStr = "Nominal (Cool)"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && powerManager != null) {
            thermalStatusStr = when (powerManager.currentThermalStatus) {
                PowerManager.THERMAL_STATUS_NONE -> "Nominal (Cool)"
                PowerManager.THERMAL_STATUS_LIGHT -> "Light Warmth"
                PowerManager.THERMAL_STATUS_MODERATE -> "Moderate Throttling"
                PowerManager.THERMAL_STATUS_SEVERE -> "Severe Thermal Load"
                PowerManager.THERMAL_STATUS_CRITICAL -> "Critical Throttling"
                PowerManager.THERMAL_STATUS_EMERGENCY -> "Emergency Cooldown"
                PowerManager.THERMAL_STATUS_SHUTDOWN -> "Thermal Shutdown"
                else -> "Nominal"
            }
        }

        val overheating = tempCelsius > 42.0f || healthInt == BatteryManager.BATTERY_HEALTH_OVERHEAT

        return BatteryInfo(
            isAvailable = true,
            levelPercent = pct,
            temperatureCelsius = tempCelsius,
            voltageMv = voltage,
            status = status,
            health = health,
            powerSource = powerSource,
            technology = tech,
            thermalStatus = thermalStatusStr,
            isOverheating = overheating
        )
    }

    fun getDisplayInfo(): DisplayInfo {
        val metrics = context.resources.displayMetrics
        val res = "${metrics.widthPixels} × ${metrics.heightPixels}"
        val dpi = metrics.densityDpi
        var refresh = 60.0f

        try {
            val dm = context.getSystemService(Context.DISPLAY_SERVICE) as? android.hardware.display.DisplayManager
            val display = dm?.getDisplay(android.view.Display.DEFAULT_DISPLAY)
            if (display != null) {
                refresh = display.refreshRate
            }
        } catch (_: Exception) {}

        return DisplayInfo(
            resolution = res,
            densityDpi = dpi,
            refreshRateHz = refresh
        )
    }

    fun getNetworkStatus(): NetworkStatusInfo {
        val cm = connectivityManager ?: return NetworkStatusInfo(
            isConnected = false,
            transportType = "No Network Connection",
            isVpnActive = false,
            downstreamBandwidthKbps = 0
        )
        val active = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(active)

        if (caps == null) {
            return NetworkStatusInfo(
                isConnected = false,
                transportType = "Disconnected",
                isVpnActive = false,
                downstreamBandwidthKbps = 0
            )
        }

        val isVpn = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        val transport = when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular (Mobile Data)"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth Tethering"
            isVpn -> "VPN Tunnel"
            else -> "Active Network"
        }

        return NetworkStatusInfo(
            isConnected = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
            transportType = transport,
            isVpnActive = isVpn,
            downstreamBandwidthKbps = caps.linkDownstreamBandwidthKbps
        )
    }
}
