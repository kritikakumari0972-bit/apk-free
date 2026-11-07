package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GamingSessionDao {
    @Query("SELECT * FROM gaming_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<GamingSessionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: GamingSessionRecord): Long

    @Query("DELETE FROM gaming_sessions")
    suspend fun clearAll()
}

@Dao
interface CleanHistoryDao {
    @Query("SELECT * FROM clean_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<CleanHistoryRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CleanHistoryRecord): Long

    @Query("SELECT SUM(bytesFreed) FROM clean_history")
    fun getTotalBytesFreed(): Flow<Long?>

    @Query("DELETE FROM clean_history")
    suspend fun clearAll()
}

@Dao
interface PerformanceSnapshotDao {
    @Query("SELECT * FROM performance_snapshots ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSnapshots(limit: Int = 30): Flow<List<PerformanceSnapshotRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: PerformanceSnapshotRecord): Long

    @Query("DELETE FROM performance_snapshots")
    suspend fun clearAll()
}

@Dao
interface SmartScanDao {
    @Query("SELECT * FROM smart_scan_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentScans(limit: Int = 20): Flow<List<SmartScanRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(record: SmartScanRecord): Long

    @Query("DELETE FROM smart_scan_history")
    suspend fun clearAll()
}
