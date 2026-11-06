package com.example.core.storage

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

enum class CleanCategory(val title: String, val description: String) {
    TEMP_FILES("Temporary & Cache Files", "Redundant log files, temporary build traces, and cache objects"),
    DUPLICATE_FILES("Duplicate Files", "Identical duplicate files verified by streaming SHA-256"),
    LARGE_FILES("Large Files (> 10MB)", "Oversized files consuming significant local storage capacity"),
    EMPTY_FOLDERS("Empty Directories", "Leftover zero-byte folders from uninstalled or removed apps"),
    OBSOLETE_APKS("APK Installers", "Residual Android installation packages that are no longer needed"),
    OLD_DOWNLOADS("Old Downloaded Caches", "Temporary downloads and aged files older than 30 days")
}

data class CleanItem(
    val id: String,
    val name: String,
    val path: String,
    val sizeBytes: Long,
    val category: CleanCategory,
    val lastModified: Long,
    val isSelected: Boolean = false // CRITICAL: Default selection MUST be false (manual user selection required)
)

data class CleanCategoryGroup(
    val category: CleanCategory,
    val items: List<CleanItem>,
    val totalBytes: Long,
    val selectedBytes: Long
)

data class CleanScanResult(
    val groups: List<CleanCategoryGroup>,
    val totalBytesFound: Long,
    val totalFilesFound: Int,
    val scanDurationMs: Long
)

data class CleanExecutionResult(
    val bytesRecovered: Long,
    val itemsDeleted: Int,
    val failedCount: Int,
    val categoriesCleaned: List<CleanCategory>,
    val isVerified: Boolean,
    val isPartialSuccess: Boolean = false
)

class StorageCleaner(private val context: Context) {

    suspend fun scanStorage(): CleanScanResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val tempItems = mutableListOf<CleanItem>()
        val largeItems = mutableListOf<CleanItem>()
        val emptyDirs = mutableListOf<CleanItem>()
        val apkItems = mutableListOf<CleanItem>()
        val oldDownloadItems = mutableListOf<CleanItem>()
        val allScannedFiles = mutableListOf<File>()

        // 1. Scan app's internal cache & external cache
        val scanDirs = mutableListOf<File>()
        context.cacheDir?.let { scanDirs.add(it) }
        context.codeCacheDir?.let { scanDirs.add(it) }
        context.externalCacheDir?.let { scanDirs.add(it) }
        context.getExternalFilesDirs(null)?.filterNotNull()?.forEach { scanDirs.add(it) }

        // Also check accessible public directories like Downloads if available
        try {
            val publicDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (publicDownloads != null && publicDownloads.exists() && publicDownloads.canRead()) {
                scanDirs.add(publicDownloads)
            }
        } catch (_: Exception) {}

        val thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)

        for (baseDir in scanDirs.distinctBy { it.absolutePath }) {
            crawlDirectory(
                dir = baseDir,
                allFiles = allScannedFiles,
                tempItems = tempItems,
                largeItems = largeItems,
                emptyDirs = emptyDirs,
                apkItems = apkItems,
                oldDownloadItems = oldDownloadItems,
                thirtyDaysAgo = thirtyDaysAgo,
                depth = 0
            )
        }

        // 2. Identify duplicate files with streaming full-file SHA-256
        val duplicateItems = detectDuplicatesStreaming(allScannedFiles)

        val groups = mutableListOf<CleanCategoryGroup>()

        fun addGroup(cat: CleanCategory, items: List<CleanItem>) {
            if (items.isNotEmpty()) {
                val total = items.sumOf { it.sizeBytes }
                val selected = items.filter { it.isSelected }.sumOf { it.sizeBytes }
                groups.add(CleanCategoryGroup(cat, items, total, selected))
            }
        }

        addGroup(CleanCategory.TEMP_FILES, tempItems)
        addGroup(CleanCategory.DUPLICATE_FILES, duplicateItems)
        addGroup(CleanCategory.LARGE_FILES, largeItems)
        addGroup(CleanCategory.EMPTY_FOLDERS, emptyDirs)
        addGroup(CleanCategory.OBSOLETE_APKS, apkItems)
        addGroup(CleanCategory.OLD_DOWNLOADS, oldDownloadItems)

        val duration = System.currentTimeMillis() - startTime
        val allDiscoveredItems = (tempItems + duplicateItems + largeItems + emptyDirs + apkItems + oldDownloadItems)
            .distinctBy { it.path }
        val uniqueTotalBytes = allDiscoveredItems.sumOf { it.sizeBytes }
        val uniqueTotalCount = allDiscoveredItems.size

        CleanScanResult(
            groups = groups,
            totalBytesFound = uniqueTotalBytes,
            totalFilesFound = uniqueTotalCount,
            scanDurationMs = duration
        )
    }

    private fun crawlDirectory(
        dir: File,
        allFiles: MutableList<File>,
        tempItems: MutableList<CleanItem>,
        largeItems: MutableList<CleanItem>,
        emptyDirs: MutableList<CleanItem>,
        apkItems: MutableList<CleanItem>,
        oldDownloadItems: MutableList<CleanItem>,
        thirtyDaysAgo: Long,
        depth: Int
    ) {
        if (depth > 6 || !dir.exists() || !dir.canRead()) return

        val children = dir.listFiles() ?: return

        if (children.isEmpty() && dir != context.cacheDir && dir != context.filesDir) {
            emptyDirs.add(
                CleanItem(
                    id = dir.absolutePath,
                    name = dir.name.ifBlank { "Empty Directory" },
                    path = dir.absolutePath,
                    sizeBytes = 0L,
                    category = CleanCategory.EMPTY_FOLDERS,
                    lastModified = dir.lastModified(),
                    isSelected = false
                )
            )
            return
        }

        for (file in children) {
            if (file.isDirectory) {
                crawlDirectory(file, allFiles, tempItems, largeItems, emptyDirs, apkItems, oldDownloadItems, thirtyDaysAgo, depth + 1)
            } else if (file.isFile) {
                val size = file.length()
                val name = file.name
                val ext = file.extension.lowercase()
                allFiles.add(file)

                // Check Temp
                if (ext in listOf("tmp", "temp", "log", "bak", "chk") || name.startsWith("cache_") || name.startsWith("~")) {
                    tempItems.add(
                        CleanItem(
                            id = file.absolutePath,
                            name = name,
                            path = file.absolutePath,
                            sizeBytes = size,
                            category = CleanCategory.TEMP_FILES,
                            lastModified = file.lastModified(),
                            isSelected = false
                        )
                    )
                }

                // Check APK
                if (ext == "apk") {
                    apkItems.add(
                        CleanItem(
                            id = file.absolutePath,
                            name = name,
                            path = file.absolutePath,
                            sizeBytes = size,
                            category = CleanCategory.OBSOLETE_APKS,
                            lastModified = file.lastModified(),
                            isSelected = false
                        )
                    )
                }

                // Check Large Files (> 10MB)
                if (size > 10L * 1024 * 1024) {
                    largeItems.add(
                        CleanItem(
                            id = file.absolutePath,
                            name = name,
                            path = file.absolutePath,
                            sizeBytes = size,
                            category = CleanCategory.LARGE_FILES,
                            lastModified = file.lastModified(),
                            isSelected = false
                        )
                    )
                }

                // Check Old Downloads
                if (file.lastModified() < thirtyDaysAgo && ext in listOf("zip", "gz", "tar", "tmp", "part")) {
                    oldDownloadItems.add(
                        CleanItem(
                            id = file.absolutePath,
                            name = name,
                            path = file.absolutePath,
                            sizeBytes = size,
                            category = CleanCategory.OLD_DOWNLOADS,
                            lastModified = file.lastModified(),
                            isSelected = false
                        )
                    )
                }
            }
        }
    }

    /**
     * Reliable streaming duplicate verification:
     * 1. Compare file sizes.
     * 2. Group candidates with equal sizes.
     * 3. Compute full-file SHA-256 digest using streaming reads (64KB buffer, low memory).
     * 4. Exclude unreadable files.
     */
    private fun detectDuplicatesStreaming(files: List<File>): List<CleanItem> {
        val duplicates = mutableListOf<CleanItem>()
        // Filter out zero-byte or tiny files (< 1KB) to avoid false noise
        val bySize = files.filter { it.length() > 1024L }.groupBy { it.length() }

        for ((_, group) in bySize) {
            if (group.size < 2) continue

            val hashToFileMap = mutableMapOf<String, File>()
            for (file in group) {
                val fullHash = calculateFullStreamingSha256(file) ?: continue
                if (hashToFileMap.containsKey(fullHash)) {
                    // Confirmed duplicate with matching size and full SHA-256
                    duplicates.add(
                        CleanItem(
                            id = file.absolutePath,
                            name = file.name,
                            path = file.absolutePath,
                            sizeBytes = file.length(),
                            category = CleanCategory.DUPLICATE_FILES,
                            lastModified = file.lastModified(),
                            isSelected = false
                        )
                    )
                } else {
                    hashToFileMap[fullHash] = file
                }
            }
        }
        return duplicates
    }

    companion object {
        /**
         * Computes complete streaming SHA-256 hash without loading entire file into RAM.
         */
        fun calculateFullStreamingSha256(file: File): String? {
            return try {
                if (!file.exists() || !file.canRead() || file.isDirectory) return null
                val digest = MessageDigest.getInstance("SHA-256")
                val buffer = ByteArray(65536) // 64 KB streaming buffer
                FileInputStream(file).use { fis ->
                    var bytesRead: Int
                    while (fis.read(buffer).also { bytesRead = it } != -1) {
                        if (bytesRead > 0) {
                            digest.update(buffer, 0, bytesRead)
                        }
                    }
                }
                digest.digest().joinToString("") { "%02x".format(it) }
            } catch (_: Exception) {
                null
            }
        }
    }

    /**
     * Safe verified deletion workflow:
     * SCAN -> ANALYZE -> REVIEW -> SELECT -> CONFIRM -> DELETE -> VERIFY
     */
    suspend fun executeClean(itemsToDelete: List<CleanItem>): CleanExecutionResult = withContext(Dispatchers.IO) {
        var freedBytes = 0L
        var deletedCount = 0
        var failedCount = 0
        val affectedCategories = mutableSetOf<CleanCategory>()

        // Canonical deduplication of deletion targets by path
        val distinctItems = itemsToDelete.distinctBy { it.path }

        for (item in distinctItems) {
            try {
                val file = File(item.path)
                // Re-verify existence and metadata before deletion
                if (file.exists() && file.canWrite()) {
                    val size = if (file.isDirectory) 0L else file.length()
                    val deleted = if (file.isDirectory) {
                        file.deleteRecursively()
                    } else {
                        file.delete()
                    }
                    if (deleted && !file.exists()) {
                        freedBytes += size
                        deletedCount++
                        affectedCategories.add(item.category)
                    } else {
                        failedCount++
                    }
                } else {
                    failedCount++
                }
            } catch (_: Exception) {
                failedCount++
            }
        }

        // NO hidden cache deletion: unselected app cache is NOT deleted silently.

        val isFullyVerified = (failedCount == 0 && deletedCount > 0)
        val isPartial = (failedCount > 0 && deletedCount > 0)

        CleanExecutionResult(
            bytesRecovered = freedBytes,
            itemsDeleted = deletedCount,
            failedCount = failedCount,
            categoriesCleaned = affectedCategories.toList(),
            isVerified = isFullyVerified,
            isPartialSuccess = isPartial
        )
    }
}
