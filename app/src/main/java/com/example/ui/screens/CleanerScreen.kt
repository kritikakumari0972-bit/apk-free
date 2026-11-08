package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.storage.CleanCategory
import com.example.core.storage.CleanItem
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonTopBar
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaCardBorder
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaNeonYellow
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun CleanerScreen(
    viewModel: NovaMainViewModel,
    onNavigateToAppCache: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scanResult by viewModel.cleanScanResult.collectAsState()
    val isScanning by viewModel.isCleaningScanning.collectAsState()
    val selectedIds by viewModel.selectedCleanItemIds.collectAsState()
    val executionResult by viewModel.cleanExecutionResult.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf<CleanCategory?>(null) }

    val safFolderLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            viewModel.scanCustomSafFolder(uri)
        }
    }

    LaunchedEffect(Unit) {
        if (scanResult == null && !isScanning) {
            viewModel.startStorageScan()
        }
    }

    val totalBytesFound = remember(scanResult) {
        scanResult?.totalBytesFound ?: 0L
    }

    val selectedBytes = remember(scanResult, selectedIds) {
        val items = scanResult?.groups?.flatMap { it.items } ?: emptyList()
        items.filter { selectedIds.contains(it.id) }.sumOf { it.sizeBytes }
    }

    val displayGb = remember(totalBytesFound, isScanning) {
        if (isScanning) "Scanning..."
        else com.example.core.util.FormatUtils.formatBytes(totalBytesFound)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBgDark)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        NeonTopBar(
            icon = Icons.Default.DeleteOutline,
            title = "Storage Cleaner",
            subtitle = "Safe verified cache cleaning",
            rightIcon = Icons.Default.Menu,
            onRightClick = onNavigateToAppCache
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Center Cyber Trash Can on Glowing Pedestal
        Box(
            modifier = Modifier
                .size(190.dp)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center

                // Glowing blue circles beneath trashcan
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NovaNeonBlue.copy(alpha = 0.35f), Color.Transparent),
                        center = center,
                        radius = size.minDimension / 2f
                    )
                )

                // Concentric cyan rings
                drawCircle(
                    color = NovaNeonCyan.copy(alpha = 0.6f),
                    radius = size.minDimension * 0.36f,
                    style = Stroke(width = 2.5f)
                )
                drawCircle(
                    color = NovaNeonBlue.copy(alpha = 0.4f),
                    radius = size.minDimension * 0.45f,
                    style = Stroke(width = 1.5f)
                )
            }

            // Trash Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(NovaNeonBlue.copy(alpha = 0.2f))
                    .border(2.dp, NovaNeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = NovaNeonCyan,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Real Discovered Size Display
        Text(
            text = displayGb,
            color = NovaTextWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = if (isScanning) "Analyzing accessible app directories..." else "Junk & Cache Files Discovered",
            color = NovaTextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Clean Action Button: Shows exact selected bytes (Empty by default for safety)
        NeonActionButton(
            text = if (selectedIds.isEmpty()) "Select Items to Clean (0 Selected)" else "Clean Selected (${com.example.core.util.FormatUtils.formatBytes(selectedBytes)})",
            onClick = {
                if (selectedIds.isNotEmpty()) {
                    showConfirmDialog = true
                }
            },
            enabled = selectedIds.isNotEmpty() && !isScanning,
            testTag = "clean_now_button"
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NeonActionButton(
                text = "Select Folder (SAF)",
                onClick = { safFolderLauncher.launch(null) },
                enabled = !isScanning,
                modifier = Modifier.weight(1f),
                testTag = "saf_folder_picker_button"
            )
            NeonActionButton(
                text = "Rescan",
                onClick = { viewModel.startStorageScan() },
                enabled = !isScanning,
                modifier = Modifier.weight(1f),
                testTag = "cleaner_rescan_button"
            )
        }

        // Select All / Deselect All Controls
        if (scanResult != null && !isScanning && totalBytesFound > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedIds.size} of ${scanResult!!.totalFilesFound} items selected",
                    color = NovaTextGray,
                    fontSize = 11.sp
                )
                Row {
                    Text(
                        text = "Select All",
                        color = NovaNeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.selectAllDetectedItems() }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Clear All",
                        color = NovaTextGray,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .clickable { viewModel.clearItemSelection() }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Execution feedback message
        if (executionResult != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NovaNeonGreen.copy(alpha = 0.15f))
                    .border(1.dp, NovaNeonGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NovaNeonGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cleaned ${com.example.core.util.FormatUtils.formatBytes(executionResult!!.bytesRecovered)} across ${executionResult!!.itemsDeleted} files successfully. (${executionResult!!.failedCount} skipped)",
                        color = NovaTextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Scan Categories Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Scan Categories & Review",
                color = NovaTextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Render each category with expandable item checklist
        val groups = scanResult?.groups ?: emptyList()
        if (groups.isEmpty() && !isScanning) {
            NeonCard {
                Text(
                    text = "No junk or cache files found in accessible folders. Storage is clean!",
                    color = NovaNeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        groups.forEach { group ->
            val isExpanded = expandedCategory == group.category
            val groupSelectedCount = group.items.count { selectedIds.contains(it.id) }
            val iconColor = when (group.category) {
                CleanCategory.TEMP_FILES -> NovaNeonCyan
                CleanCategory.DUPLICATE_FILES -> Color(0xFFFF9100)
                CleanCategory.LARGE_FILES -> NovaNeonYellow
                CleanCategory.EMPTY_FOLDERS -> NovaNeonGreen
                CleanCategory.OBSOLETE_APKS -> NovaNeonBlue
                CleanCategory.OLD_DOWNLOADS -> Color(0xFFE040FB)
                CleanCategory.CUSTOM_SAF_FOLDER -> NovaNeonCyan
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NovaCardBg)
                    .border(1.dp, NovaCardBorder, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedCategory = if (isExpanded) null else group.category
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(iconColor.copy(alpha = 0.16f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = group.category.title,
                                    color = NovaTextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${group.items.size} files • $groupSelectedCount selected",
                                    color = NovaTextGray,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = com.example.core.util.FormatUtils.formatBytes(group.totalBytes),
                                color = NovaTextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = NovaTextGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Expanded list of reviewable items
                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = if (groupSelectedCount == group.items.size) "Deselect Group" else "Select Group",
                                    color = NovaNeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.toggleCategorySelection(group.category, groupSelectedCount != group.items.size)
                                        }
                                        .padding(vertical = 4.dp, horizontal = 6.dp)
                                )
                            }

                            group.items.forEach { item ->
                                val isChecked = selectedIds.contains(item.id)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.toggleCleanItem(item.id) }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { viewModel.toggleCleanItem(item.id) },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = NovaNeonCyan,
                                            uncheckedColor = NovaTextGray
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.name, color = NovaTextWhite, fontSize = 12.sp, maxLines = 1)
                                        Text(text = item.path, color = NovaTextGray, fontSize = 9.sp, maxLines = 1)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = com.example.core.util.FormatUtils.formatBytes(item.sizeBytes),
                                        color = NovaTextGray,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = NovaNeonYellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Confirm Safe Deletion", color = NovaTextWhite, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to permanently delete the selected items?",
                        color = NovaTextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Items selected: ${selectedIds.size}\n• Total size: ${com.example.core.util.FormatUtils.formatBytes(selectedBytes)}\n\nOnly the explicitly checked files will be removed. System and personal files are preserved.",
                        color = NovaTextGray,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                NeonActionButton(
                    text = "Delete ${selectedIds.size} Items",
                    onClick = {
                        showConfirmDialog = false
                        viewModel.executeCleanSelected()
                    },
                    modifier = Modifier.width(160.dp),
                    testTag = "dialog_clean_confirm_button"
                )
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel", color = NovaTextGray)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(18.dp)
        )
    }
}
