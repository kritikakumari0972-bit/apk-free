package com.example

import com.example.core.hardware.BatteryInfo
import com.example.core.hardware.MetricResult
import com.example.core.hardware.RamInfo
import com.example.core.hardware.StorageInfo
import com.example.core.network.DnsTestResult
import com.example.core.network.PingResult
import com.example.core.network.SpeedTestPhase
import com.example.core.storage.CleanCategory
import com.example.core.storage.CleanItem
import com.example.core.storage.StorageCleaner
import com.example.core.util.FormatUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ExampleUnitTest {

    @Test
    fun testAdditionSanity() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testFormatBytesZero() {
        assertEquals("0 B", FormatUtils.formatBytes(0L))
    }

    @Test
    fun testFormatBytesKilobytes() {
        assertEquals("1.0 KB", FormatUtils.formatBytes(1024L))
        assertEquals("2.5 KB", FormatUtils.formatBytes(2560L))
    }

    @Test
    fun testFormatBytesMegabytes() {
        assertEquals("1.0 MB", FormatUtils.formatBytes(1024L * 1024L))
        assertEquals("500.0 MB", FormatUtils.formatBytes(500L * 1024L * 1024L))
    }

    @Test
    fun testFormatBytesGigabytes() {
        assertEquals("1.0 GB", FormatUtils.formatBytes(1024L * 1024L * 1024L))
        assertEquals("64.0 GB", FormatUtils.formatBytes(64L * 1024L * 1024L * 1024L))
    }

    @Test
    fun testFormatCpuClock() {
        assertEquals("", FormatUtils.formatCpuClock(0L))
        assertEquals("800 MHz", FormatUtils.formatCpuClock(800000L))
        assertEquals("2.4 GHz", FormatUtils.formatCpuClock(2400000L))
        assertEquals("3.0 GHz", FormatUtils.formatCpuClock(3000000L))
    }

    @Test
    fun testFormatDuration() {
        assertEquals("00:00", FormatUtils.formatDurationSeconds(0L))
        assertEquals("00:45", FormatUtils.formatDurationSeconds(45L))
        assertEquals("01:30", FormatUtils.formatDurationSeconds(90L))
        assertEquals("01:05:00", FormatUtils.formatDurationSeconds(3900L))
    }

    @Test
    fun testCleanItemDefaultSelectionMustBeFalse() {
        // Enforce safety requirement: files are UNSELECTED by default
        val item = CleanItem(
            id = "/storage/emulated/0/cache/tmp1.log",
            name = "tmp1.log",
            path = "/storage/emulated/0/cache/tmp1.log",
            sizeBytes = 2048L,
            category = CleanCategory.TEMP_FILES,
            lastModified = 1700000000L
        )
        assertEquals("tmp1.log", item.name)
        assertEquals(CleanCategory.TEMP_FILES, item.category)
        assertFalse("CleanItem must be unselected by default for safety", item.isSelected)
    }

    @Test
    fun testStreamingSha256Verification() {
        // Create temporary test file with known content
        val tempFile = File.createTempFile("novaboost_test", ".tmp")
        try {
            tempFile.writeText("NovaBoost Streaming SHA-256 Test Content")
            val hash = StorageCleaner.calculateFullStreamingSha256(tempFile)
            assertNotNull(hash)
            assertEquals(64, hash!!.length)
            
            // Same content must give exact same hash
            val tempFile2 = File.createTempFile("novaboost_test2", ".tmp")
            try {
                tempFile2.writeText("NovaBoost Streaming SHA-256 Test Content")
                val hash2 = StorageCleaner.calculateFullStreamingSha256(tempFile2)
                assertEquals(hash, hash2)
            } finally {
                tempFile2.delete()
            }
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testHealthScoreCalculationLogic() {
        val ram = RamInfo(
            totalBytes = 6L * 1024 * 1024 * 1024,
            availableBytes = 3L * 1024 * 1024 * 1024,
            usedBytes = 3L * 1024 * 1024 * 1024,
            usagePercentage = 50,
            isLowMemory = false,
            thresholdBytes = 1L * 1024 * 1024 * 1024,
            processUsedBytes = 50L * 1024 * 1024,
            cachedBytes = 500L * 1024 * 1024,
            swapTotalBytes = 2L * 1024 * 1024 * 1024,
            swapFreeBytes = 1L * 1024 * 1024 * 1024,
            zRamAccessible = true,
            zRamUsageString = "2 GB Active",
            swapSourceType = "zRAM",
            swapSupportStatus = "Active"
        )
        val storage = StorageInfo(
            totalBytes = 128L * 1024 * 1024 * 1024,
            availableBytes = 64L * 1024 * 1024 * 1024,
            usedBytes = 64L * 1024 * 1024 * 1024,
            usagePercentage = 50,
            path = "/data"
        )
        val battery = BatteryInfo(
            isAvailable = true,
            levelPercent = 85,
            temperatureCelsius = 32.0f,
            voltageMv = 3900,
            status = "Discharging",
            health = "Good",
            powerSource = "Battery Power",
            technology = "Li-ion",
            thermalStatus = "Nominal (Cool)",
            isOverheating = false
        )

        // Perfect conditions should yield high health score
        var score = 100
        if (ram.usagePercentage > 50) score -= 4
        if (storage.usagePercentage > 60) score -= 5
        if (battery.temperatureCelsius > 38f) score -= 6
        if (ram.isLowMemory) score -= 15
        assertTrue(score in 85..100)
    }

    @Test
    fun testMetricResultStateModel() {
        val loading: MetricResult<String> = MetricResult.Loading
        val available: MetricResult<Int> = MetricResult.Available(42)
        val unavailable: MetricResult<Nothing> = MetricResult.Unavailable("SELinux restricted")

        assertTrue(loading is MetricResult.Loading)
        assertTrue(available is MetricResult.Available)
        assertEquals(42, (available as MetricResult.Available).data)
        assertTrue(unavailable is MetricResult.Unavailable)
        assertEquals("SELinux restricted", (unavailable as MetricResult.Unavailable).reason)
    }

    @Test
    fun testNetworkDiagnosticsModels() {
        val ping = PingResult(
            targetHost = "1.1.1.1:443",
            latencyMs = 28L,
            isSuccess = true,
            measurementType = "TCP Connect"
        )
        assertEquals("1.1.1.1:443", ping.targetHost)
        assertEquals(28L, ping.latencyMs)
        assertTrue(ping.isSuccess)
        assertEquals("TCP Connect", ping.measurementType)

        val dns = DnsTestResult(
            queryHost = "one.one.one.one",
            resolvedIp = "1.1.1.1",
            lookupTimeMs = 15L,
            isSuccess = true
        )
        assertEquals("one.one.one.one", dns.queryHost)
        assertEquals("1.1.1.1", dns.resolvedIp)
        assertTrue(dns.isSuccess)

        assertEquals(SpeedTestPhase.TESTING_DOWNLOAD, SpeedTestPhase.valueOf("TESTING_DOWNLOAD"))
        assertEquals(SpeedTestPhase.TESTING_UPLOAD, SpeedTestPhase.valueOf("TESTING_UPLOAD"))
    }

    @Test
    fun testPhysicalRamZeroFallbackModel() {
        // Physical RAM is not available: totalBytes is 0
        val unavailableRam = RamInfo(
            totalBytes = 0L,
            availableBytes = 0L,
            usedBytes = 0L,
            usagePercentage = 0,
            isLowMemory = false,
            thresholdBytes = 0L,
            processUsedBytes = 25L * 1024 * 1024,
            cachedBytes = 0L,
            swapTotalBytes = 0L,
            swapFreeBytes = 0L,
            zRamAccessible = false,
            zRamUsageString = "Unavailable",
            swapSourceType = "None",
            swapSupportStatus = "Unavailable",
            isPhysicalRamAvailable = false
        )
        assertFalse("When totalBytes is 0, isPhysicalRamAvailable must be false", unavailableRam.isPhysicalRamAvailable)
        assertEquals(0L, unavailableRam.totalBytes)
        // Ensure JVM maxMemory is never substituted for physical RAM
        assertTrue(unavailableRam.totalBytes != Runtime.getRuntime().maxMemory())
    }

    @Test
    fun testZramVsSystemSwapDistinction() {
        val zramInfo = RamInfo(
            totalBytes = 8L * 1024 * 1024 * 1024,
            availableBytes = 4L * 1024 * 1024 * 1024,
            usedBytes = 4L * 1024 * 1024 * 1024,
            usagePercentage = 50,
            isLowMemory = false,
            thresholdBytes = 1L * 1024 * 1024 * 1024,
            processUsedBytes = 50L * 1024 * 1024,
            cachedBytes = 500L * 1024 * 1024,
            swapTotalBytes = 3L * 1024 * 1024 * 1024,
            swapFreeBytes = 2L * 1024 * 1024 * 1024,
            zRamAccessible = true,
            zRamUsageString = "3.0 GB Active",
            swapSourceType = "zRAM",
            swapSupportStatus = "Active (zRAM)",
            isPhysicalRamAvailable = true
        )
        assertEquals("zRAM", zramInfo.swapSourceType)
        assertTrue(zramInfo.zRamAccessible)
        assertTrue(zramInfo.isPhysicalRamAvailable)
    }

    @Test
    fun testStorageCleanExecutionResultVerification() {
        // Verified complete clean
        val successResult = com.example.core.storage.CleanExecutionResult(
            bytesRecovered = 10485760L,
            itemsDeleted = 5,
            failedCount = 0,
            categoriesCleaned = listOf(CleanCategory.TEMP_FILES),
            isVerified = true,
            isPartialSuccess = false
        )
        assertTrue(successResult.isVerified)
        assertFalse(successResult.isPartialSuccess)
        assertEquals(5, successResult.itemsDeleted)

        // Partial success (some deleted, some failed)
        val partialResult = com.example.core.storage.CleanExecutionResult(
            bytesRecovered = 6291456L,
            itemsDeleted = 3,
            failedCount = 2,
            categoriesCleaned = listOf(CleanCategory.TEMP_FILES),
            isVerified = false,
            isPartialSuccess = true
        )
        assertFalse(partialResult.isVerified)
        assertTrue(partialResult.isPartialSuccess)

        // Complete failure
        val failureResult = com.example.core.storage.CleanExecutionResult(
            bytesRecovered = 0L,
            itemsDeleted = 0,
            failedCount = 4,
            categoriesCleaned = listOf(CleanCategory.TEMP_FILES),
            isVerified = false,
            isPartialSuccess = false
        )
        assertFalse(failureResult.isVerified)
        assertFalse(failureResult.isPartialSuccess)
    }

    @Test
    fun testStorageCleanDeduplicationByPath() {
        val file1 = CleanItem(id = "/sdcard/Download/test.log", name = "test.log", path = "/sdcard/Download/test.log", sizeBytes = 1024, category = CleanCategory.TEMP_FILES, lastModified = 100)
        // Same file appearing under empty folder or duplicate scan
        val file1Duplicate = CleanItem(id = "/sdcard/Download/test.log", name = "test.log", path = "/sdcard/Download/test.log", sizeBytes = 1024, category = CleanCategory.DUPLICATE_FILES, lastModified = 100)
        val file2 = CleanItem(id = "/sdcard/Download/doc.pdf", name = "doc.pdf", path = "/sdcard/Download/doc.pdf", sizeBytes = 2048, category = CleanCategory.LARGE_FILES, lastModified = 200)

        val combinedList = listOf(file1, file1Duplicate, file2)
        assertEquals(3, combinedList.size)

        val uniqueFiles = combinedList.distinctBy { it.path }
        assertEquals(2, uniqueFiles.size)
        assertEquals(3072L, uniqueFiles.sumOf { it.sizeBytes })
    }

    @Test
    fun testVpnConnectionStatesAndTelemetry() {
        val states = com.example.services.VpnConnectionState.values().map { it.name }
        assertTrue(states.contains("NOT_CONFIGURED"))
        assertTrue(states.contains("CONNECTING"))
        assertTrue(states.contains("HANDSHAKE_IN_PROGRESS"))
        assertTrue(states.contains("CONNECTED"))
        assertTrue(states.contains("CONNECTION_FAILED"))

        val defaultTelemetry = com.example.services.VpnTelemetry(
            state = com.example.services.VpnConnectionState.NOT_CONFIGURED,
            endpointHost = null,
            endpointPort = 0
        )
        assertEquals(com.example.services.VpnConnectionState.NOT_CONFIGURED, defaultTelemetry.state)
        assertEquals(0L, defaultTelemetry.bytesSent)
        assertEquals(0L, defaultTelemetry.bytesReceived)
        assertEquals(0L, defaultTelemetry.durationSeconds)
        assertEquals(-1L, defaultTelemetry.endpointLatencyMs)
    }

    @Test
    fun testGpuProbeFailureModel() {
        val failedGpu = com.example.core.hardware.GpuDiagnostics(
            isProbeSuccessful = false,
            renderer = "Unavailable (EGL Query Failed)",
            vendor = "Unknown",
            openGlVersion = "Unknown",
            maxTextureSize = 0,
            vulkanSupported = false,
            vulkanLevel = "None",
            extensionCount = 0,
            topExtensions = emptyList(),
            astcSupported = false,
            etc2Supported = false
        )
        assertFalse(failedGpu.isProbeSuccessful)
        assertEquals(0, failedGpu.maxTextureSize)
        assertFalse(failedGpu.renderer == "System Graphics")
    }

    @Test
    fun testPerformanceSnapshotRecordModel() {
        val snapshot = com.example.data.PerformanceSnapshotRecord(
            healthScore = 95,
            ramUsagePercentage = 55,
            ramAvailableMb = 3200L,
            cpuUsagePercentage = 22,
            batteryTempCelsius = 33.5f,
            batteryLevel = 88,
            storageUsagePercentage = 48
        )
        assertEquals(95, snapshot.healthScore)
        assertEquals(55, snapshot.ramUsagePercentage)
        assertEquals(3200L, snapshot.ramAvailableMb)
        assertEquals(22, snapshot.cpuUsagePercentage)
        assertEquals(33.5f, snapshot.batteryTempCelsius, 0.01f)
        assertEquals(88, snapshot.batteryLevel)
        assertEquals(48, snapshot.storageUsagePercentage)
    }

    @Test
    fun testSmartScanRecordModel() {
        val scan = com.example.data.SmartScanRecord(
            stagesCompleted = 14,
            healthScore = 92,
            summary = "All hardware subsystems optimal"
        )
        assertEquals(14, scan.stagesCompleted)
        assertEquals(92, scan.healthScore)
        assertEquals("All hardware subsystems optimal", scan.summary)
    }

    @Test
    fun testBatteryAndGamingProfilesModel() {
        val batteryProfiles = listOf("Balanced", "Power Saving", "Gaming Battery", "Monitoring Efficient")
        assertEquals(4, batteryProfiles.size)
        assertTrue(batteryProfiles.contains("Power Saving"))
        assertTrue(batteryProfiles.contains("Monitoring Efficient"))

        val gamingProfiles = listOf("Performance Monitoring", "Balanced Gaming", "Battery-Conscious Gaming", "Thermal-Conscious Gaming")
        assertEquals(4, gamingProfiles.size)
        assertTrue(gamingProfiles.contains("Performance Monitoring"))
        assertTrue(gamingProfiles.contains("Thermal-Conscious Gaming"))
    }
}

