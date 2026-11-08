package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `performance_snapshots` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `healthScore` INTEGER NOT NULL,
                        `ramUsagePercentage` INTEGER NOT NULL,
                        `ramAvailableMb` INTEGER NOT NULL,
                        `cpuUsagePercentage` INTEGER NOT NULL,
                        `batteryTempCelsius` REAL NOT NULL,
                        `batteryLevel` INTEGER NOT NULL,
                        `storageUsagePercentage` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `smart_scan_history` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `stagesCompleted` INTEGER NOT NULL,
                        `healthScore` INTEGER NOT NULL,
                        `summary` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): NovaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NovaDatabase::class.java,
                    "novaboost_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
