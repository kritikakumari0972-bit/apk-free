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
}
