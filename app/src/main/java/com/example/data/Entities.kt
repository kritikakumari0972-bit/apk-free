package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gaming_sessions")
data class GamingSessionRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameName: String,
    val packageName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Long,
    val peakTemperatureCelsius: Float,
    val avgAvailableRamMb: Long,
    val avgPingMs: Int,
    val profileName: String
)

@Entity(tableName = "clean_history")
data class CleanHistoryRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val bytesFreed: Long,
    val itemsCleaned: Int,
    val categoriesSummary: String
)

@Entity(tableName = "performance_snapshots")
data class PerformanceSnapshotRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val healthScore: Int,
    val ramUsagePercentage: Int,
    val ramAvailableMb: Long,
    val cpuUsagePercentage: Int,
    val batteryTempCelsius: Float,
    val batteryLevel: Int,
    val storageUsagePercentage: Int
)

@Entity(tableName = "smart_scan_history")
data class SmartScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val stagesCompleted: Int,
    val healthScore: Int,
    val summary: String
)
