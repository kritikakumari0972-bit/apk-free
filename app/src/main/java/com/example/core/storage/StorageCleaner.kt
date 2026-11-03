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
    DUPLICATE_FILES("Duplicate Files", "Identical duplicate files discovered in accessible storage"),
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
    val isSelected: Boolean = true
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
    val categoriesCleaned: List<CleanCategory>
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

        // 2. Identify duplicate files among accessible files
        val duplicateItems = detectDuplicates(allScannedFiles)

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
        val totalBytes = groups.sumOf { it.totalBytes }
        val totalCount = groups.sumOf { it.items.size }

        CleanScanResult(
            groups = groups,
            totalBytesFound = totalBytes,
            totalFilesFound = totalCount,
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
                    isSelected = true
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
                            lastModified = file.lastModified()
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
                            lastModified = file.lastModified()
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
                            lastModified = file.lastModified()
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
                            lastModified = file.lastModified()
                        )
                    )
                }
            }
        }
    }

    private fun detectDuplicates(files: List<File>): List<CleanItem> {
        val duplicates = mutableListOf<CleanItem>()
        // Group by exact file size first to avoid unnecessary hashing
        val bySize = files.filter { it.length() > 1024 }.groupBy { it.length() }

        for ((_, group) in bySize) {
            if (group.size < 2) continue
            // Check 4KB hash
            val byHash = mutableMapOf<String, File>()
            for (file in group) {
                val hash = calculateQuickHash(file) ?: continue
                if (byHash.containsKey(hash)) {
                    // It's a duplicate!
                    duplicates.add(
                        CleanItem(
                            id = file.absolutePath,
                            name = file.name,
                            path = file.absolutePath,
                            sizeBytes = file.length(),
                            category = CleanCategory.DUPLICATE_FILES,
                            lastModified = file.lastModified()
                        )
                    )
                } else {
                    byHash[hash] = file
                }
            }
        }
        return duplicates
    }

    private fun calculateQuickHash(file: File): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            FileInputStream(file).use { fis ->
                val buffer = ByteArray(4096)
                val read = fis.read(buffer)
                if (read > 0) md.update(buffer, 0, read)
            }
            md.digest().joinToString("") { "%02x".format(it) }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun executeClean(itemsToDelete: List<CleanItem>): CleanExecutionResult = withContext(Dispatchers.IO) {
        var freedBytes = 0L
        var deletedCount = 0
        val affectedCategories = mutableSetOf<CleanCategory>()

        for (item in itemsToDelete) {
            try {
                val file = File(item.path)
                if (file.exists()) {
                    val size = if (file.isDirectory) 0L else file.length()
                    val deleted = if (file.isDirectory) {
                        file.deleteRecursively()
                    } else {
                        file.delete()
                    }
                    if (deleted) {
                        freedBytes += size
                        deletedCount++
                        affectedCategories.add(item.category)
                    }
                }
            } catch (_: Exception) {
                // Skip protected files
            }
        }

        // Also purge NovaBoost's own volatile app cache
        try {
            context.cacheDir?.listFiles()?.forEach { f ->
                val s = f.length()
                if (f.deleteRecursively()) {
                    freedBytes += s
                }
            }
        } catch (_: Exception) {}

        CleanExecutionResult(
            bytesRecovered = freedBytes,
            itemsDeleted = deletedCount,
            categoriesCleaned = affectedCategories.toList()
        )
    }
}
