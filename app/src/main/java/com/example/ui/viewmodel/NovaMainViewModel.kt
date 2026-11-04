package com.example.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.apps.AppManager
import com.example.core.apps.InstalledAppItem
import com.example.core.gaming.ActiveGamingSession
import com.example.core.gaming.GamingSessionManager
import com.example.core.gaming.LagDiagnosticReport
import com.example.core.hardware.BatteryInfo
import com.example.core.hardware.CpuInfo
import com.example.core.hardware.DisplayInfo
import com.example.core.hardware.GpuDiagnostics
import com.example.core.hardware.GpuProbe
import com.example.core.hardware.HardwareMonitor
import com.example.core.hardware.NetworkStatusInfo
import com.example.core.hardware.RamInfo
import com.example.core.hardware.StorageInfo
import com.example.core.network.DnsTestResult
import com.example.core.network.NetworkDetails
import com.example.core.network.NetworkDiagnostics
import com.example.core.network.PingResult
import com.example.core.network.SpeedTestProgress
import com.example.core.storage.CleanCategory
import com.example.core.storage.CleanExecutionResult
import com.example.core.storage.CleanItem
import com.example.core.storage.CleanScanResult
import com.example.core.storage.StorageCleaner
import com.example.data.CleanHistoryRecord
import com.example.data.GamingSessionRecord
import com.example.data.NovaDatabase
import com.example.data.PreferencesManager
import com.example.services.NovaBoostVpnService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AutoScanStep(
    val title: String,
    val status: String,
    val isComplete: Boolean,
    val details: String
)

data class AutoScanReport(
    val isScanning: Boolean,
    val progressPercent: Int,
    val steps: List<AutoScanStep>,
    val overallHealthScore: Int,
    val keyFindings: List<String>,
    val recommendations: List<String>
)

class NovaMainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    val hardwareMonitor = HardwareMonitor(context)
    val storageCleaner = StorageCleaner(context)
    val appManager = AppManager(context)
    val networkDiagnostics = NetworkDiagnostics(context)
    private val database = NovaDatabase.getInstance(context)
    val preferencesManager = PreferencesManager(context)
    val gamingSessionManager = GamingSessionManager(
        context,
        hardwareMonitor,
        networkDiagnostics,
        database.gamingSessionDao()
    )

    // Hardware Telemetry States with genuine initial readings
    val ramInfo = MutableStateFlow(hardwareMonitor.getRamInfo())
    val storageInfo = MutableStateFlow(hardwareMonitor.getStorageInfo())
    val cpuInfo = MutableStateFlow(hardwareMonitor.getCpuInfo())
    val batteryInfo = MutableStateFlow(hardwareMonitor.getBatteryInfo())
    val displayInfo = MutableStateFlow(hardwareMonitor.getDisplayInfo())
    val networkStatus = MutableStateFlow(hardwareMonitor.getNetworkStatus())
    val gpuDiagnostics = MutableStateFlow(GpuProbe.probeGpu(context))
    val networkDetails = MutableStateFlow(networkDiagnostics.getNetworkDetails())

    // Dynamic Health Score & History
    val healthScore = MutableStateFlow(
        computeLiveHealthScore(ramInfo.value, storageInfo.value, batteryInfo.value)
    )
    val ramUsageHistory = MutableStateFlow<List<Float>>(
        listOf((ramInfo.value.usagePercentage.toFloat()).coerceAtLeast(10f))
    )

    // Telemetry Refresh Job
    private var telemetryJob: Job? = null
    val lastRefreshTime = MutableStateFlow(System.currentTimeMillis())

    // Cleaner State
    val isCleaningScanning = MutableStateFlow(false)
    val cleanScanResult = MutableStateFlow<CleanScanResult?>(null)
    // CRITICAL: Default selection MUST be empty (manual user selection required)
    val selectedCleanItemIds = MutableStateFlow<Set<String>>(emptySet())
    val cleanExecutionResult = MutableStateFlow<CleanExecutionResult?>(null)
    val cleanHistory: StateFlow<List<CleanHistoryRecord>> = database.cleanHistoryDao().getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Gaming State
    val activeGamingSession: StateFlow<ActiveGamingSession?> = gamingSessionManager.activeSessionFlow
    val installedApps = MutableStateFlow<List<InstalledAppItem>>(emptyList())
    val customGamePackages: StateFlow<Set<String>> = preferencesManager.customGamesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val gamingSessionsHistory: StateFlow<List<GamingSessionRecord>> = database.gamingSessionDao().getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val lagDiagnosticReport = MutableStateFlow<LagDiagnosticReport?>(null)
    val isLagDiagnosing = MutableStateFlow(false)
    val lastEndedSessionReport = MutableStateFlow<GamingSessionRecord?>(null)

    // Network & Speed Test State
    val pingResult = MutableStateFlow<PingResult?>(null)
    val dnsResult = MutableStateFlow<DnsTestResult?>(null)
    val isPinging = MutableStateFlow(false)
    val speedTestProgress = MutableStateFlow<SpeedTestProgress?>(null)
    val isTestingSpeed = MutableStateFlow(false)

    // VPN Real Status
    val isVpnActive: StateFlow<Boolean> = NovaBoostVpnService.isVpnRunning
    val vpnStatusText: StateFlow<String> = NovaBoostVpnService.vpnStatusMessage

    // Auto-Scan State
    val autoScanReport = MutableStateFlow<AutoScanReport?>(null)

    // Preferences
    val tempThreshold: StateFlow<Float> = preferencesManager.tempThresholdFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 42.0f)
    val autoRefreshSec: StateFlow<Int> = preferencesManager.autoRefreshSecFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)
    val overlayEnabled: StateFlow<Boolean> = preferencesManager.overlayEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val dndGaming: StateFlow<Boolean> = preferencesManager.dndGamingFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val darkTheme: StateFlow<Boolean> = preferencesManager.darkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        startTelemetryLoop()
        loadInstalledApps()
        startStorageScan()
        runNetworkPing()
    }

    fun computeLiveHealthScore(ram: RamInfo, storage: StorageInfo, battery: BatteryInfo): Int {
        var score = 100
        val ramUsage = ram.usagePercentage
        val storageUsage = storage.usagePercentage

        if (ramUsage > 85) score -= 15
        else if (ramUsage > 70) score -= 8
        else if (ramUsage > 50) score -= 4

        if (storageUsage > 90) score -= 20
        else if (storageUsage > 75) score -= 10
        else if (storageUsage > 60) score -= 5

        if (battery.isAvailable) {
            if (battery.temperatureCelsius > 42f) score -= 15
            else if (battery.temperatureCelsius > 38f) score -= 6

            if (!battery.health.equals("Good", ignoreCase = true) && !battery.health.equals("Normal", ignoreCase = true)) {
                score -= 10
            }
        }

        if (ram.isLowMemory) score -= 15

        return score.coerceIn(20, 100)
    }

    fun startTelemetryLoop() {
        telemetryJob?.cancel()
        telemetryJob = viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                refreshHardwareTelemetry()
                delay(3000L)
            }
        }
    }

    fun refreshHardwareTelemetry() {
        try {
            val newRam = hardwareMonitor.getRamInfo()
            val newStorage = hardwareMonitor.getStorageInfo()
            val newCpu = hardwareMonitor.getCpuInfo()
            val newBattery = hardwareMonitor.getBatteryInfo()
            val newNetStatus = hardwareMonitor.getNetworkStatus()
            val newNetDetails = networkDiagnostics.getNetworkDetails()

            ramInfo.value = newRam
            storageInfo.value = newStorage
            cpuInfo.value = newCpu
            batteryInfo.value = newBattery
            networkStatus.value = newNetStatus
            networkDetails.value = newNetDetails
            lastRefreshTime.value = System.currentTimeMillis()

            // Update real health score
            healthScore.value = computeLiveHealthScore(newRam, newStorage, newBattery)

            // Update RAM history samples
            val history = ramUsageHistory.value.toMutableList()
            history.add(newRam.usagePercentage.toFloat().coerceAtLeast(10f))
            if (history.size > 20) {
                history.removeAt(0)
            }
            ramUsageHistory.value = history
        } catch (_: Throwable) {}
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            installedApps.value = appManager.getInstalledApps(customGamePackages.value)
        }
    }

    // Cleaner Actions
    fun startStorageScan() {
        viewModelScope.launch {
            isCleaningScanning.value = true
            cleanExecutionResult.value = null
            val result = storageCleaner.scanStorage()
            cleanScanResult.value = result
            // CRITICAL REQUIREMENT: Default selection must be EMPTY (no auto-selection)
            selectedCleanItemIds.value = emptySet()
            isCleaningScanning.value = false
        }
    }

    fun toggleCleanItem(id: String) {
        val current = selectedCleanItemIds.value.toMutableSet()
        if (current.contains(id)) current.remove(id) else current.add(id)
        selectedCleanItemIds.value = current
    }

    fun toggleCategorySelection(category: CleanCategory, select: Boolean) {
        val result = cleanScanResult.value ?: return
        val group = result.groups.firstOrNull { it.category == category } ?: return
        val current = selectedCleanItemIds.value.toMutableSet()
        val groupIds = group.items.map { it.id }
        if (select) current.addAll(groupIds) else current.removeAll(groupIds.toSet())
        selectedCleanItemIds.value = current
    }

    fun selectAllDetectedItems() {
        val scan = cleanScanResult.value ?: return
        selectedCleanItemIds.value = scan.groups.flatMap { it.items }.map { it.id }.toSet()
    }

    fun clearItemSelection() {
        selectedCleanItemIds.value = emptySet()
    }

    fun executeCleanSelected() {
        val scan = cleanScanResult.value ?: return
        viewModelScope.launch {
            val allItems = scan.groups.flatMap { it.items }
            val toDelete = allItems.filter { selectedCleanItemIds.value.contains(it.id) }
            val executionResult = storageCleaner.executeClean(toDelete)
            cleanExecutionResult.value = executionResult

            // Record to Database
            if (executionResult.bytesRecovered > 0 || executionResult.itemsDeleted > 0) {
                database.cleanHistoryDao().insertRecord(
                    CleanHistoryRecord(
                        bytesFreed = executionResult.bytesRecovered,
                        itemsCleaned = executionResult.itemsDeleted,
                        categoriesSummary = executionResult.categoriesCleaned.joinToString(", ") { it.title }
                    )
                )
            }

            // Refresh storage & cleaner results
            refreshHardwareTelemetry()
            cleanScanResult.value = null
            selectedCleanItemIds.value = emptySet()
        }
    }

    // Memory Actions
    fun purgeNovaBoostMemoryCache(): Long {
        val freed = appManager.clearNovaBoostCache()
        System.gc()
        refreshHardwareTelemetry()
        return freed
    }

    // Gaming Actions
    fun launchGameSession(gameName: String, packageName: String, profileName: String = "Balanced") {
        gamingSessionManager.startSession(gameName, packageName, profileName)
        appManager.launchApp(packageName)
    }

    fun stopGameSession() {
        viewModelScope.launch {
            val record = gamingSessionManager.stopSession()
            lastEndedSessionReport.value = record
        }
    }

    fun dismissSessionReport() {
        lastEndedSessionReport.value = null
    }

    fun runLagDiagnostics() {
        viewModelScope.launch {
            isLagDiagnosing.value = true
            lagDiagnosticReport.value = gamingSessionManager.evaluateLagDiagnostics()
            isLagDiagnosing.value = false
        }
    }

    fun addCustomGame(packageName: String) {
        viewModelScope.launch {
            preferencesManager.addCustomGame(packageName)
            loadInstalledApps()
        }
    }

    fun removeCustomGame(packageName: String) {
        viewModelScope.launch {
            preferencesManager.removeCustomGame(packageName)
            loadInstalledApps()
        }
    }

    // Network Actions
    fun runNetworkPing() {
        viewModelScope.launch {
            isPinging.value = true
            pingResult.value = networkDiagnostics.measureTcpHandshake()
            dnsResult.value = networkDiagnostics.testDnsResolution()
            isPinging.value = false
        }
    }

    fun runSpeedTest() {
        viewModelScope.launch {
            isTestingSpeed.value = true
            networkDiagnostics.runFullSpeedTest { progress ->
                speedTestProgress.value = progress
                if (progress.isFinished) {
                    isTestingSpeed.value = false
                }
            }
        }
    }

    // VPN Real Actions
    fun requestVpnConnect(serverHost: String, serverPort: Int = 1194) {
        if (serverHost.isBlank()) {
            return
        }
        val intent = Intent(context, NovaBoostVpnService::class.java).apply {
            action = NovaBoostVpnService.ACTION_CONNECT
            putExtra(NovaBoostVpnService.EXTRA_SERVER_HOST, serverHost)
            putExtra(NovaBoostVpnService.EXTRA_SERVER_PORT, serverPort)
        }
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (_: Exception) {}
    }

    fun requestVpnDisconnect() {
        val intent = Intent(context, NovaBoostVpnService::class.java).apply {
            action = NovaBoostVpnService.ACTION_DISCONNECT
        }
        context.startService(intent)
    }

    // Central Progressive Auto-Scan
    fun runAutoScan() {
        viewModelScope.launch {
            val initialSteps = listOf(
                AutoScanStep("RAM & Linux zRAM", "Evaluating memory pressure & swap state...", false, ""),
                AutoScanStep("Storage Capacity", "Analyzing internal partitions & availability...", false, ""),
                AutoScanStep("Junk & Temporary Files", "Scanning cache files and logs...", false, ""),
                AutoScanStep("Duplicate & Large Files", "Detecting identical files and high consumers...", false, ""),
                AutoScanStep("CPU & Logical Cores", "Reading architecture, frequencies and load...", false, ""),
                AutoScanStep("GPU Hardware & Vulkan", "Probing graphics pipeline and API...", false, ""),
                AutoScanStep("Battery Health & Voltage", "Inspecting thermal state and power status...", false, ""),
                AutoScanStep("Network & Latency", "Validating connectivity and round-trip ping...", false, "")
            )

            autoScanReport.value = AutoScanReport(
                isScanning = true,
                progressPercent = 10,
                steps = initialSteps,
                overallHealthScore = 0,
                keyFindings = emptyList(),
                recommendations = emptyList()
            )

            // Step 1: RAM
            val ram = withContext(Dispatchers.Default) { hardwareMonitor.getRamInfo() }
            val step1 = AutoScanStep(
                "RAM & Linux zRAM",
                "${ram.usagePercentage}% utilized",
                true,
                "${ram.availableBytes / (1024 * 1024)} MB available of ${ram.totalBytes / (1024 * 1024)} MB. zRAM: ${ram.swapSupportStatus}"
            )
            autoScanReport.value = autoScanReport.value?.copy(progressPercent = 25, steps = listOf(step1) + initialSteps.drop(1))

            // Step 2: Storage
            val storage = withContext(Dispatchers.Default) { hardwareMonitor.getStorageInfo() }
            val step2 = AutoScanStep(
                "Storage Capacity",
                "${storage.usagePercentage}% occupied",
                true,
                "${storage.availableBytes / (1024 * 1024 * 1024)} GB available out of ${storage.totalBytes / (1024 * 1024 * 1024)} GB"
            )
            autoScanReport.value = autoScanReport.value?.copy(progressPercent = 40, steps = listOf(step1, step2) + initialSteps.drop(2))

            // Step 3 & 4: Cleaner
            val scanResult = storageCleaner.scanStorage()
            val step3 = AutoScanStep(
                "Junk & Temporary Files",
                "${scanResult.totalFilesFound} removable objects",
                true,
                "${scanResult.totalBytesFound / (1024 * 1024)} MB identified across accessible cache folders"
            )
            val step4 = AutoScanStep(
                "Duplicate & Large Files",
                "Analyzed",
                true,
                "Duplicates and large files segregated into user-selectable groups (Unselected by default for safety)"
            )
            autoScanReport.value = autoScanReport.value?.copy(progressPercent = 60, steps = listOf(step1, step2, step3, step4) + initialSteps.drop(4))

            // Step 5: CPU
            val cpu = withContext(Dispatchers.Default) { hardwareMonitor.getCpuInfo() }
            val step5 = AutoScanStep(
                "CPU & Logical Cores",
                "${cpu.processorCount} Cores (${cpu.architecture})",
                true,
                if (cpu.isSystemUsageAvailable) "Load: ${cpu.cpuUsagePercent}% (${cpu.cpuUsageMode})"
                else "Available (${cpu.processorCount} Cores) | System usage restricted by SELinux"
            )
            autoScanReport.value = autoScanReport.value?.copy(progressPercent = 75, steps = listOf(step1, step2, step3, step4, step5) + initialSteps.drop(5))

            // Step 6: GPU
            val gpu = gpuDiagnostics.value
            val step6 = AutoScanStep(
                "GPU Hardware & Vulkan",
                gpu.renderer,
                true,
                "${gpu.openGlVersion} | Vulkan: ${gpu.vulkanLevel}"
            )
            autoScanReport.value = autoScanReport.value?.copy(progressPercent = 85, steps = listOf(step1, step2, step3, step4, step5, step6) + initialSteps.drop(6))

            // Step 7: Battery
            val bat = withContext(Dispatchers.Default) { hardwareMonitor.getBatteryInfo() }
            val step7 = AutoScanStep(
                "Battery Health & Voltage",
                if (bat.isAvailable) "${bat.levelPercent}% (${bat.health})" else "Unavailable",
                true,
                if (bat.isAvailable) "${"%.1f".format(bat.temperatureCelsius)}°C | ${bat.voltageMv} mV | ${bat.thermalStatus}" else "Battery info unavailable"
            )
            autoScanReport.value = autoScanReport.value?.copy(progressPercent = 95, steps = listOf(step1, step2, step3, step4, step5, step6, step7) + initialSteps.drop(7))

            // Step 8: Network
            val ping = networkDiagnostics.measureTcpHandshake()
            val step8 = AutoScanStep(
                "Network & Latency",
                if (ping.isSuccess) "${ping.latencyMs} ms" else "Offline / Timeout",
                true,
                "Target: ${ping.targetHost} (${ping.measurementType})"
            )

            // Transparent device health score based on real metrics
            var score = 100
            val findings = mutableListOf<String>()
            val recommendations = mutableListOf<String>()

            if (ram.usagePercentage > 85) {
                score -= 15
                findings.add("Physical RAM usage is high (${ram.usagePercentage}%).")
                recommendations.add("Close heavy background applications from App Info.")
            }
            if (storage.usagePercentage > 90) {
                score -= 20
                findings.add("Storage capacity is over 90% full.")
                recommendations.add("Review and clean temporary files in the Cleaner tab.")
            }
            if (scanResult.totalBytesFound > 50L * 1024 * 1024) {
                score -= 10
                findings.add("${scanResult.totalBytesFound / (1024 * 1024)} MB of cache files identified.")
                recommendations.add("Select and remove obsolete cache items in the Cleaner tab.")
            }
            if (bat.isAvailable && bat.temperatureCelsius > 41f) {
                score -= 15
                findings.add("Device temperature is elevated at ${"%.1f".format(bat.temperatureCelsius)}°C.")
                recommendations.add("Pause heavy 3D gaming or disconnect fast charger.")
            }
            if (!ping.isSuccess || ping.latencyMs > 150) {
                score -= 10
                findings.add("Network latency is higher than optimal.")
                recommendations.add("Verify Wi-Fi or cellular reception in Network tab.")
            }

            if (findings.isEmpty()) {
                findings.add("All hardware subsystems are operating within optimal operating parameters.")
                recommendations.add("System is well maintained. Routine scans recommended weekly.")
            }

            autoScanReport.value = AutoScanReport(
                isScanning = false,
                progressPercent = 100,
                steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8),
                overallHealthScore = score.coerceIn(20, 100),
                keyFindings = findings,
                recommendations = recommendations
            )
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            database.cleanHistoryDao().clearAll()
            database.gamingSessionDao().clearAll()
        }
    }
}
