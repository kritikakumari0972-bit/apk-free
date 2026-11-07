package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        GamingSessionRecord::class,
        CleanHistoryRecord::class,
        PerformanceSnapshotRecord::class,
        SmartScanRecord::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NovaDatabase : RoomDatabase() {
    abstract fun gamingSessionDao(): GamingSessionDao
    abstract fun cleanHistoryDao(): CleanHistoryDao
    abstract fun performanceSnapshotDao(): PerformanceSnapshotDao
    abstract fun smartScanDao(): SmartScanDao

    companion object {
        @Volatile
        private var INSTANCE: NovaDatabase? = null

        fun getInstance(context: Context): NovaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NovaDatabase::class.java,
                    "novaboost_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
