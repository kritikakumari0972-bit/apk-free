package com.example.core.util

import java.util.Locale

object FormatUtils {

    /**
     * Formats bytes into human-readable B, KB, MB, GB, TB
     */
    fun formatBytes(bytes: Long): String {
        if (bytes <= 0L) return "0 B"
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        val tb = gb / 1024.0

        return when {
            tb >= 1.0 -> String.format(Locale.US, "%.1f TB", tb)
            gb >= 1.0 -> String.format(Locale.US, "%.1f GB", gb)
            mb >= 1.0 -> String.format(Locale.US, "%.1f MB", mb)
            kb >= 1.0 -> String.format(Locale.US, "%.1f KB", kb)
            else -> "$bytes B"
        }
    }

    /**
     * Formats bytes without unit for combined strings like "3.2 GB / 6.0 GB"
     */
    fun formatGigabytesOnly(bytes: Long): String {
        if (bytes <= 0L) return "0.0"
        val gb = bytes / (1024.0 * 1024.0 * 1024.0)
        return String.format(Locale.US, "%.1f", gb)
    }

    /**
     * Formats frequency from kHz
     */
    fun formatCpuClock(freqKHz: Long): String {
        if (freqKHz <= 0L) return ""
        val mhz = freqKHz / 1000.0
        val ghz = mhz / 1000.0
        return if (ghz >= 1.0) {
            String.format(Locale.US, "%.1f GHz", ghz)
        } else {
            String.format(Locale.US, "%.0f MHz", mhz)
        }
    }

    /**
     * Formats duration in seconds into MM:SS or HH:MM:SS
     */
    fun formatDurationSeconds(seconds: Long): String {
        if (seconds <= 0L) return "00:00"
        val hrs = seconds / 3600
        val mins = (seconds % 3600) / 60
        val secs = seconds % 60
        return if (hrs > 0) {
            String.format(Locale.US, "%02d:%02d:%02d", hrs, mins, secs)
        } else {
            String.format(Locale.US, "%02d:%02d", mins, secs)
        }
    }
}
