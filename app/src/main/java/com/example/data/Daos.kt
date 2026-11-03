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
