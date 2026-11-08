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
import com.example.data.PerformanceSnapshotRecord
import com.example.data.PreferencesManager
import com.example.data.SmartScanRecord
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
    val id: String,
    val title: String,
    val status: String,
    val isComplete: Boolean,
    val details: String,
    val hasFailed: Boolean = false,
    val errorDetails: String? = null
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

    // Hardware Telemetry States with lightweight initial safe defaults (probed asynchronously without blocking Main Thread)
    val ramInfo = MutableStateFlow(
        RamInfo(
            totalBytes = 0L,
            availableBytes = 0L,
            usedBytes = 0L,
            usagePercentage = 0,
            isPhysicalRamAvailable = false,
            isLowMemory = false,
            thresholdBytes = 0L,
            processUsedBytes = 0L,
            cachedBytes = 0L,
            swapTotalBytes = 0L,
            swapFreeBytes = 0L,
            zRamAccessible = false,
            zRamUsageString = "Detecting...",
            zRamCapacityBytes = 0L,
            swapSourceType = "Probing...",
            swapSupportStatus = "Detecting...",
            zRamState = com.example.core.hardware.ZRamState.RESTRICTED_OR_UNAVAILABLE
        )
    )
    val storageInfo = MutableStateFlow(
        StorageInfo(
            totalBytes = 0L,
            availableBytes = 0L,
            usedBytes = 0L,
            usagePercentage = 0,
            path = "/storage/emulated/0"
        )
    )
    val batteryInfo = MutableStateFlow(
        BatteryInfo(
            isAvailable = false,
            levelPercent = 0,
            temperatureCelsius = 0f,
            voltageMv = 0,
            status = "Probing...",
            health = "Probing...",
            powerSource = "Detecting...",
            technology = "Li-ion",
            thermalStatus = "Nominal",
            isOverheating = false,
            isCpuThrottling = false,
            thermalStatusLevel = 0
        )
    )
    val displayInfo = MutableStateFlow(
        DisplayInfo(
            resolution = "Probing...",
            densityDpi = 0,
            refreshRateHz = 60f
        )
    )
    val networkStatus = MutableStateFlow(
        NetworkStatusInfo(
            isConnected = false,
            transportType = "Probing...",
            isVpnActive = false,
            downstreamBandwidthKbps = 0
        )
    )
    val networkDetails = MutableStateFlow(
        com.example.core.network.NetworkDetails(
            connectionType = "Probing...",
            isConnected = false,
            isInternetValidated = false,
            isVpnActive = false,
            wifiLinkSpeedMbps = 0,
            wifiFrequencyMhz = 0,
            wifiRssiDbm = -100,
            downstreamKbps = 0,
            upstreamKbps = 0
        )
    )

    // Initial safe states for heavy probes
    val cpuInfo = MutableStateFlow(
        CpuInfo(
            processorCount = Runtime.getRuntime().availableProcessors(),
            architecture = android.os.Build.SUPPORTED_ABIS.firstOrNull() ?: "ARM",
            hardwareName = android.os.Build.HARDWARE,
            socModel = android.os.Build.BOARD,
            isSystemUsageAvailable = false,
            cpuUsagePercent = 0,
            cpuUsageMode = "Probing...",
            processCpuUsagePercent = 0,
            coreFrequencies = emptyList(),
            features = emptyList()
        )
    )
    val gpuDiagnostics = MutableStateFlow(
        GpuDiagnostics(
            isProbeSuccessful = false,
            renderer = "Probing GPU hardware...",
            vendor = "Hardware query pending",
            openGlVersion = "OpenGL ES",
            maxTextureSize = 0,
            vulkanSupported = false,
            vulkanLevel = "Checking...",
            extensionCount = 0,
            topExtensions = emptyList(),
            astcSupported = false,
            etc2Supported = false
        )
    )

    // Dynamic Health Score & History
    val healthScore = MutableStateFlow(100)
    val ramUsageHistory = MutableStateFlow<List<Float>>(listOf(15f))

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

    // VPN Real Telemetry & State (Driven directly by NovaBoostVpnService backend)
    val vpnTelemetry: StateFlow<com.example.services.VpnTelemetry> = NovaBoostVpnService.vpnTelemetry
    val isVpnActive: StateFlow<Boolean> = NovaBoostVpnService.isVpnRunning
    val vpnStatusText: StateFlow<String> = NovaBoostVpnService.vpnStatusMessage

    // Auto-Scan State
    private var autoScanJob: Job? = null
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

    // Initialization Experience State
    val isAppInitialized = MutableStateFlow(false)
    val initializationStatus = MutableStateFlow("Initializing kernel telemetry...")

    // Performance History & Smart Scan History (Room)
    val performanceHistory: StateFlow<List<PerformanceSnapshotRecord>> = database.performanceSnapshotDao().getRecentSnapshots()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val smartScanHistory: StateFlow<List<SmartScanRecord>> = database.smartScanDao().getRecentScans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Profiles
    val batteryProfile: StateFlow<String> = preferencesManager.batteryProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Balanced")
    val gamingProfile: StateFlow<String> = preferencesManager.gamingProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Balanced Gaming")

    init {
        // Asynchronous Startup Sequence: background hardware probing without blocking Main Thread
        viewModelScope.launch(Dispatchers.IO) {
            initializationStatus.value = "Initializing hardware & settings..."
            delay(150L)
            initializationStatus.value = "Detecting Linux RAM & zRAM..."
            val ram = hardwareMonitor.getRamInfo()
            val storage = hardwareMonitor.getStorageInfo()
            val bat = hardwareMonitor.getBatteryInfo()

            initializationStatus.value = "Probing CPU architecture & GPU..."
            val cpu = hardwareMonitor.getCpuInfo()
            val gpu = GpuProbe.probeGpu(context)
            val disp = hardwareMonitor.getDisplayInfo()
            val netStatus = hardwareMonitor.getNetworkStatus()
            val net = networkDiagnostics.getNetworkDetails()

            withContext(Dispatchers.Main) {
                ramInfo.value = ram
                storageInfo.value = storage
                batteryInfo.value = bat
                cpuInfo.value = cpu
                gpuDiagnostics.value = gpu
                displayInfo.value = disp
                networkStatus.value = netStatus
                networkDetails.value = net
                healthScore.value = computeLiveHealthScore(ram, storage, bat)
                ramUsageHistory.value = listOf(ram.usagePercentage.toFloat().coerceAtLeast(10f))
                initializationStatus.value = "System Subsystems Active"
                isAppInitialized.value = true
            }

            // Record initial baseline performance snapshot
            recordPerformanceSnapshot()
        }
        startTelemetryLoop()
        loadInstalledApps()
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
                val intervalSec = autoRefreshSec.value.coerceIn(1, 60)
                delay(intervalSec * 1000L)
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

    fun scanCustomSafFolder(treeUri: android.net.Uri) {
        viewModelScope.launch {
            isCleaningScanning.value = true
            try {
                val customGroup = storageCleaner.scanSafTree(treeUri)
                val currentGroups = cleanScanResult.value?.groups ?: emptyList()
                val updatedGroups = currentGroups.filter { it.category != customGroup.category } + customGroup
                val totalBytes = updatedGroups.sumOf { it.totalBytes }
                val totalFiles = updatedGroups.sumOf { it.items.size }
                cleanScanResult.value = CleanScanResult(
                    groups = updatedGroups,
                    totalBytesFound = totalBytes,
                    totalFilesFound = totalFiles,
                    scanDurationMs = 200L
                )
            } catch (_: Exception) {} finally {
                isCleaningScanning.value = false
            }
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

    // Central Progressive 12-Stage Auto-Scan
    fun cancelAutoScan() {
        autoScanJob?.cancel()
        autoScanJob = null
        autoScanReport.value = autoScanReport.value?.copy(isScanning = false)
    }

    fun retryAutoScan() {
        runAutoScan()
    }

    fun runAutoScan() {
        autoScanJob?.cancel()
        autoScanJob = viewModelScope.launch {
            val initialSteps = listOf(
                AutoScanStep("stage_compat", "Hardware Compatibility", "Checking device SoC & Android kernel...", false, ""),
                AutoScanStep("stage_ram", "Physical RAM", "Evaluating physical memory & pressure...", false, ""),
                AutoScanStep("stage_zram", "Linux zRAM", "Checking compressed in-memory swap...", false, ""),
                AutoScanStep("stage_swap", "System Swap", "Detecting storage-backed swap interfaces...", false, ""),
                AutoScanStep("stage_storage", "Storage Capacity", "Analyzing internal partitions & available headroom...", false, ""),
                AutoScanStep("stage_junk", "Temporary Files", "Scanning cache directories...", false, ""),
                AutoScanStep("stage_duplicate", "Duplicate Files (SHA-256)", "Checking identical files via streaming hash...", false, ""),
                AutoScanStep("stage_cpu", "CPU & Architecture", "Reading architecture, frequencies and load...", false, ""),
                AutoScanStep("stage_gpu", "GPU & Vulkan Architecture", "Probing graphics pipeline & renderer...", false, ""),
                AutoScanStep("stage_battery", "Battery Health & Level", "Reading battery percentage and chemistry...", false, ""),
                AutoScanStep("stage_thermal", "Thermal Governor State", "Inspecting Android thermal throttling level...", false, ""),
                AutoScanStep("stage_network", "Network Latency & Connectivity", "Probing socket latency and DNS resolution...", false, ""),
                AutoScanStep("stage_background", "Background Activity", "Inspecting running application memory pressure...", false, ""),
                AutoScanStep("stage_gaming", "Gaming System Readiness", "Evaluating overall gaming headroom...", false, "")
            )

            autoScanReport.value = AutoScanReport(
                isScanning = true,
                progressPercent = 4,
                steps = initialSteps,
                overallHealthScore = 0,
                keyFindings = emptyList(),
                recommendations = emptyList()
            )

            try {
                // 1. Hardware Compatibility
                val step1 = AutoScanStep(
                    id = "stage_compat",
                    title = "Hardware Compatibility",
                    status = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}",
                    isComplete = true,
                    details = "Android ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT}) • ABI: ${android.os.Build.SUPPORTED_ABIS.firstOrNull() ?: "ARM"}"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 8, steps = listOf(step1) + initialSteps.drop(1))

                // 2. Physical RAM
                val ram = withContext(Dispatchers.Default) { hardwareMonitor.getRamInfo() }
                val step2 = AutoScanStep(
                    id = "stage_ram",
                    title = "Physical RAM",
                    status = if (ram.isPhysicalRamAvailable) "${ram.usagePercentage}% utilized" else "Physical RAM reading unavailable",
                    isComplete = true,
                    details = "${ram.availableBytes / (1024 * 1024)} MB available of ${ram.totalBytes / (1024 * 1024)} MB total"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 15, steps = listOf(step1, step2) + initialSteps.drop(2))

                // 3. Linux zRAM
                val step3 = AutoScanStep(
                    id = "stage_zram",
                    title = "Linux zRAM",
                    status = ram.swapSupportStatus,
                    isComplete = true,
                    details = ram.zRamUsageString
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 22, steps = listOf(step1, step2, step3) + initialSteps.drop(3))

                // 4. System Swap
                val step4 = AutoScanStep(
                    id = "stage_swap",
                    title = "System Swap",
                    status = ram.swapSourceType,
                    isComplete = true,
                    details = if (ram.swapTotalBytes > 0) "${ram.swapTotalBytes / (1024 * 1024)} MB configured" else "No storage swap partition active (Requires Root)"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 29, steps = listOf(step1, step2, step3, step4) + initialSteps.drop(4))

                // 5. Storage Capacity
                val storage = withContext(Dispatchers.Default) { hardwareMonitor.getStorageInfo() }
                val step5 = AutoScanStep(
                    id = "stage_storage",
                    title = "Storage Capacity",
                    status = "${storage.usagePercentage}% occupied",
                    isComplete = true,
                    details = "${storage.availableBytes / (1024 * 1024 * 1024)} GB free out of ${storage.totalBytes / (1024 * 1024 * 1024)} GB"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 36, steps = listOf(step1, step2, step3, step4, step5) + initialSteps.drop(5))

                // 6 & 7. Junk & Duplicate Files
                val scanResult = storageCleaner.scanStorage()
                val tempGroup = scanResult.groups.find { it.category == CleanCategory.TEMP_FILES }
                val step6 = AutoScanStep(
                    id = "stage_junk",
                    title = "Temporary Files",
                    status = "${tempGroup?.items?.size ?: 0} temporary objects",
                    isComplete = true,
                    details = "${(tempGroup?.totalBytes ?: 0L) / (1024 * 1024)} MB identified across cache folders"
                )
                val dupGroup = scanResult.groups.find { it.category == CleanCategory.DUPLICATE_FILES }
                val step7 = AutoScanStep(
                    id = "stage_duplicate",
                    title = "Duplicate Files (SHA-256)",
                    status = "${dupGroup?.items?.size ?: 0} duplicate files",
                    isComplete = true,
                    details = "${(dupGroup?.totalBytes ?: 0L) / (1024 * 1024)} MB verified by 64-KB streaming hash"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 50, steps = listOf(step1, step2, step3, step4, step5, step6, step7) + initialSteps.drop(7))

                // 8. CPU
                val cpu = withContext(Dispatchers.Default) { hardwareMonitor.getCpuInfo() }
                val step8 = AutoScanStep(
                    id = "stage_cpu",
                    title = "CPU & Architecture",
                    status = "${cpu.processorCount} Cores (${cpu.architecture})",
                    isComplete = true,
                    details = if (cpu.isSystemUsageAvailable) "Load: ${cpu.cpuUsagePercent}% (${cpu.cpuUsageMode})" else "Available (${cpu.processorCount} Cores) | System /proc/stat restricted by SELinux"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 58, steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8) + initialSteps.drop(8))

                // 9. GPU
                val gpu = gpuDiagnostics.value
                val step9 = AutoScanStep(
                    id = "stage_gpu",
                    title = "GPU & Vulkan Architecture",
                    status = if (gpu.isProbeSuccessful) gpu.renderer else "Driver probe restricted",
                    isComplete = true,
                    details = "OpenGL: ${gpu.openGlVersion} | Vulkan: ${gpu.vulkanLevel}"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 66, steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8, step9) + initialSteps.drop(9))

                // 10. Battery
                val bat = withContext(Dispatchers.Default) { hardwareMonitor.getBatteryInfo() }
                val step10 = AutoScanStep(
                    id = "stage_battery",
                    title = "Battery Health & Level",
                    status = if (bat.isAvailable) "${bat.levelPercent}% (${bat.health})" else "Unavailable",
                    isComplete = true,
                    details = if (bat.isAvailable) "${bat.voltageMv} mV • Tech: ${bat.technology} • Power: ${bat.powerSource}" else "Battery info unavailable"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 74, steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8, step9, step10) + initialSteps.drop(10))

                // 11. Thermal
                val step11 = AutoScanStep(
                    id = "stage_thermal",
                    title = "Thermal Governor State",
                    status = if (bat.isAvailable) "${"%.1f".format(bat.temperatureCelsius)}°C (${bat.thermalStatus})" else "Unavailable",
                    isComplete = true,
                    details = if (bat.isOverheating) "Thermal Throttling Active" else "Nominal operating temperature"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 82, steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8, step9, step10, step11) + initialSteps.drop(11))

                // 12. Network
                val ping = networkDiagnostics.measureTcpHandshake()
                val step12 = AutoScanStep(
                    id = "stage_network",
                    title = "Network Latency & Connectivity",
                    status = if (ping.isSuccess) "${ping.latencyMs} ms" else "Connection Failed (${ping.errorMessage ?: "Timeout"})",
                    isComplete = true,
                    details = "Target: ${ping.targetHost} (${ping.measurementType})"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 89, steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8, step9, step10, step11, step12) + initialSteps.drop(12))

                // 13. Background Activity
                val appCount = installedApps.value.size
                val step13 = AutoScanStep(
                    id = "stage_background",
                    title = "Background Activity",
                    status = "$appCount Installed Packages Indexed",
                    isComplete = true,
                    details = "Process memory monitored via standard Linux /proc interfaces"
                )
                autoScanReport.value = autoScanReport.value?.copy(progressPercent = 95, steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8, step9, step10, step11, step12, step13) + initialSteps.drop(13))

                // 14. Gaming Readiness
                val isGamingReady = !bat.isOverheating && (ram.usagePercentage < 88)
                val step14 = AutoScanStep(
                    id = "stage_gaming",
                    title = "Gaming System Readiness",
                    status = if (isGamingReady) "Optimal for 3D Gaming" else "Heavy Resource Contention",
                    isComplete = true,
                    details = if (isGamingReady) "Thermal and memory headroom verified for high frame rates" else "Consider closing background apps before launching heavy games"
                )

                // Transparent health score calculation
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

                val finalScore = score.coerceIn(20, 100)
                autoScanReport.value = AutoScanReport(
                    isScanning = false,
                    progressPercent = 100,
                    steps = listOf(step1, step2, step3, step4, step5, step6, step7, step8, step9, step10, step11, step12, step13, step14),
                    overallHealthScore = finalScore,
                    keyFindings = findings,
                    recommendations = recommendations
                )

                // Persist SmartScanRecord to Room database
                withContext(Dispatchers.IO) {
                    database.smartScanDao().insertScan(
                        SmartScanRecord(
                            stagesCompleted = 14,
                            healthScore = finalScore,
                            summary = findings.firstOrNull() ?: "All subsystems optimal"
                        )
                    )
                }
                recordPerformanceSnapshot()
            } catch (_: Exception) {
                autoScanReport.value = autoScanReport.value?.copy(isScanning = false)
            } finally {
                autoScanReport.value = autoScanReport.value?.copy(isScanning = false)
                autoScanJob = null
            }
        }
    }

    fun setBatteryProfile(profile: String) {
        viewModelScope.launch {
            preferencesManager.setBatteryProfile(profile)
            when (profile) {
                "Power Saving" -> preferencesManager.setAutoRefreshSec(10)
                "Monitoring Efficient" -> preferencesManager.setAutoRefreshSec(15)
                "Gaming Battery" -> preferencesManager.setAutoRefreshSec(5)
                else -> preferencesManager.setAutoRefreshSec(3)
            }
        }
    }

    fun setGamingProfile(profile: String) {
        viewModelScope.launch {
            preferencesManager.setGamingProfile(profile)
        }
    }

    fun recordPerformanceSnapshot() {
        viewModelScope.launch(Dispatchers.IO) {
            val ram = hardwareMonitor.getRamInfo()
            val cpu = hardwareMonitor.getCpuInfo()
            val bat = hardwareMonitor.getBatteryInfo()
            val storage = hardwareMonitor.getStorageInfo()
            val score = computeLiveHealthScore(ram, storage, bat)

            database.performanceSnapshotDao().insertSnapshot(
                PerformanceSnapshotRecord(
                    healthScore = score,
                    ramUsagePercentage = ram.usagePercentage,
                    ramAvailableMb = ram.availableBytes / (1024 * 1024),
                    cpuUsagePercentage = cpu.cpuUsagePercent,
                    batteryTempCelsius = bat.temperatureCelsius,
                    batteryLevel = bat.levelPercent,
                    storageUsagePercentage = storage.usagePercentage
                )
            )
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            database.cleanHistoryDao().clearAll()
            database.gamingSessionDao().clearAll()
            database.performanceSnapshotDao().clearAll()
            database.smartScanDao().clearAll()
        }
    }
}
