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
