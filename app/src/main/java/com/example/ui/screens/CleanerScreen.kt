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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
    val executionResult by viewModel.cleanExecutionResult.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (scanResult == null && !isScanning) {
            viewModel.startStorageScan()
        }
    }

    val totalBytesFound = remember(scanResult) {
        scanResult?.totalBytesFound ?: 0L
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
            title = "Cleaner",
            subtitle = "Free up space, boost performance.",
            rightIcon = Icons.Default.Menu,
            onRightClick = onNavigateToAppCache
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Center Cyber Trash Can on Glowing Pedestal
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center
                val radius = size.minDimension / 2f

                // Outer cyan glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NovaNeonCyan.copy(alpha = 0.22f), Color.Transparent),
                        center = center,
                        radius = radius
                    )
                )

                // Concentric circles
                drawCircle(
                    color = NovaNeonBlue.copy(alpha = 0.35f),
                    radius = radius * 0.9f,
                    style = Stroke(width = 2.5f)
                )
                drawCircle(
                    color = NovaNeonCyan.copy(alpha = 0.6f),
                    radius = radius * 0.72f,
                    style = Stroke(width = 3.5f)
                )
                drawCircle(
                    color = NovaNeonCyan.copy(alpha = 0.85f),
                    radius = radius * 0.52f,
                    style = Stroke(width = 3f)
                )
            }

            // Trash Can Icon Emblem
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(NovaNeonCyan.copy(alpha = 0.3f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = NovaNeonCyan,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        // Amount of Junk Files Found
        Text(
            text = displayGb,
            color = NovaTextWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Junk Files Found",
            color = NovaTextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Clean Now Action Button
        NeonActionButton(
            text = "Clean Now ⚡",
            onClick = { showConfirmDialog = true },
            testTag = "clean_now_action_button"
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Scan Categories Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Scan Categories",
                color = NovaTextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4 Category Cards matching mockup exactly with real scanned values
        val tempBytes = remember(scanResult) {
            scanResult?.groups?.firstOrNull { it.category == CleanCategory.TEMP_FILES }?.totalBytes ?: 0L
        }
        val dupBytes = remember(scanResult) {
            scanResult?.groups?.firstOrNull { it.category == CleanCategory.DUPLICATE_FILES }?.totalBytes ?: 0L
        }
        val largeBytes = remember(scanResult) {
            scanResult?.groups?.firstOrNull { it.category == CleanCategory.LARGE_FILES }?.totalBytes ?: 0L
        }
        val emptyCount = remember(scanResult) {
            scanResult?.groups?.firstOrNull { it.category == CleanCategory.EMPTY_FOLDERS }?.items?.size ?: 0
        }

        CategoryRowCard(
            iconColor = NovaNeonCyan,
            title = "Temporary Files",
            sizeText = if (isScanning) "Scanning..." else com.example.core.util.FormatUtils.formatBytes(tempBytes),
            onClick = onNavigateToAppCache
        )
        Spacer(modifier = Modifier.height(8.dp))
        CategoryRowCard(
            iconColor = Color(0xFFFF9100),
            title = "Duplicate Files",
            sizeText = if (isScanning) "Scanning..." else com.example.core.util.FormatUtils.formatBytes(dupBytes),
            onClick = onNavigateToAppCache
        )
        Spacer(modifier = Modifier.height(8.dp))
        CategoryRowCard(
            iconColor = NovaNeonYellow,
            title = "Large Files",
            sizeText = if (isScanning) "Scanning..." else com.example.core.util.FormatUtils.formatBytes(largeBytes),
            onClick = onNavigateToAppCache
        )
        Spacer(modifier = Modifier.height(8.dp))
        CategoryRowCard(
            iconColor = NovaNeonGreen,
            title = "Empty Folders",
            sizeText = if (isScanning) "Scanning..." else "$emptyCount Folders",
            onClick = onNavigateToAppCache
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = NovaNeonYellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Confirm Safe Cleanup", color = NovaTextWhite, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Clean $displayGb of safe accessible temporary logs and cached files now?",
                    color = NovaTextGray,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                NeonActionButton(
                    text = "Clean Now",
                    onClick = {
                        showConfirmDialog = false
                        viewModel.executeCleanSelected()
                    },
                    modifier = Modifier.width(130.dp),
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

@Composable
private fun CategoryRowCard(
    iconColor: Color,
    title: String,
    sizeText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NovaCardBg)
            .border(1.dp, NovaCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = NovaTextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = sizeText,
                    color = NovaTextGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = NovaTextGray,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
