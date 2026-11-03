package com.example

import com.example.core.storage.CleanCategory
import com.example.core.storage.CleanItem
import com.example.core.util.FormatUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

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
    fun testCleanItemModel() {
        val item = CleanItem(
            id = "/storage/emulated/0/cache/tmp1.log",
            name = "tmp1.log",
            path = "/storage/emulated/0/cache/tmp1.log",
            sizeBytes = 2048L,
            category = CleanCategory.TEMP_FILES,
            lastModified = 1700000000L,
            isSelected = true
        )
        assertEquals("tmp1.log", item.name)
        assertEquals(CleanCategory.TEMP_FILES, item.category)
        assertTrue(item.isSelected)
        val deselected = item.copy(isSelected = false)
        assertFalse(deselected.isSelected)
    }
}
