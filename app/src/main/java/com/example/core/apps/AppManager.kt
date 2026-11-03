package com.example.core.apps

import android.app.AppOpsManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Process
import android.os.storage.StorageManager
import android.provider.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class InstalledAppItem(
    val packageName: String,
    val appName: String,
    val isGame: Boolean,
    val isSystemApp: Boolean,
    val appBytes: Long,
    val cacheBytes: Long,
    val dataBytes: Long,
    val totalBytes: Long,
    val hasUsageStats: Boolean
)

class AppManager(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    suspend fun getInstalledApps(customGamePackages: Set<String> = emptySet()): List<InstalledAppItem> = withContext(Dispatchers.IO) {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos = packageManager.queryIntentActivities(intent, 0)
        val hasPermission = hasUsageStatsPermission()
        val storageStatsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasPermission) {
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as? StorageStatsManager
        } else null

        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as? StorageManager
        val storageUuid = StorageManager.UUID_DEFAULT

        val items = mutableListOf<InstalledAppItem>()

        for (resolveInfo in resolveInfos) {
            val pkg = resolveInfo.activityInfo.packageName
            val label = resolveInfo.loadLabel(packageManager).toString()
            val appInfo = resolveInfo.activityInfo.applicationInfo

            val isGameCategory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appInfo.category == ApplicationInfo.CATEGORY_GAME
            } else {
                (appInfo.flags and ApplicationInfo.FLAG_IS_GAME) != 0
            }
            val isCustomGame = customGamePackages.contains(pkg)
            val isGame = isGameCategory || isCustomGame

            val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

            var appBytes = 0L
            var cacheBytes = 0L
            var dataBytes = 0L
            var statsRetrieved = false

            if (storageStatsManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    val stats = storageStatsManager.queryStatsForPackage(
                        storageUuid,
                        pkg,
                        Process.myUserHandle()
                    )
                    appBytes = stats.appBytes
                    cacheBytes = stats.cacheBytes
                    dataBytes = stats.dataBytes
                    statsRetrieved = true
                } catch (_: Exception) {
                    // Protected or failed query for specific package
                }
            }

            items.add(
                InstalledAppItem(
                    packageName = pkg,
                    appName = label,
                    isGame = isGame,
                    isSystemApp = isSystem,
                    appBytes = appBytes,
                    cacheBytes = cacheBytes,
                    dataBytes = dataBytes,
                    totalBytes = appBytes + cacheBytes + dataBytes,
                    hasUsageStats = statsRetrieved
                )
            )
        }

        // Distinct by package and sort by total size descending
        items.distinctBy { it.packageName }
            .sortedWith(compareByDescending<InstalledAppItem> { it.totalBytes }.thenBy { it.appName })
    }

    fun getAppIcon(packageName: String): Drawable? {
        return try {
            packageManager.getApplicationIcon(packageName)
        } catch (_: Exception) {
            null
        }
    }

    fun launchApp(packageName: String): Boolean {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return false
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
        return true
    }

    fun openAppDetailsSettings(packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun openUsageAccessSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun clearNovaBoostCache(): Long {
        var freed = 0L
        try {
            context.cacheDir?.listFiles()?.forEach { file ->
                freed += file.length()
                file.deleteRecursively()
            }
            context.codeCacheDir?.listFiles()?.forEach { file ->
                freed += file.length()
                file.deleteRecursively()
            }
            context.externalCacheDir?.listFiles()?.forEach { file ->
                freed += file.length()
                file.deleteRecursively()
            }
        } catch (_: Exception) {}
        return freed
    }
}
