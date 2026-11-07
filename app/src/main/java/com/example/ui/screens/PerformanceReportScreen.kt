package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.PerformanceSnapshotRecord
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonTopBar
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaCardBorder
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaNeonYellow
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun PerformanceReportScreen(
    viewModel: NovaMainViewModel,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ram by viewModel.ramInfo.collectAsState()
    val storage by viewModel.storageInfo.collectAsState()
    val battery by viewModel.batteryInfo.collectAsState()

    val healthScore by viewModel.healthScore.collectAsState()
    val performanceHistory by viewModel.performanceHistory.collectAsState()
    var showClearHistoryDialog by remember { mutableStateOf(false) }

    val ramFreeGb = remember(ram) {
        com.example.core.util.FormatUtils.formatBytes(ram.availableBytes)
    }
    val storageFreeGb = remember(storage) {
        com.example.core.util.FormatUtils.formatBytes(storage.availableBytes)
    }
    val tempStr = remember(battery) {
        "${battery.temperatureCelsius.toInt()}°C"
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
            icon = Icons.Default.CheckCircle,
            title = "Performance Report",
            subtitle = "",
            iconTint = NovaNeonGreen
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Center Glowing Trophy with Celebratory Rays
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center

                // Festive confetti bursts
                val colors = listOf(NovaNeonCyan, NovaNeonYellow, NovaNeonGreen, Color(0xFFFF4081))
                for (i in 0 until 12) {
                    val angle = (i * 30.0) * (Math.PI / 180.0)
                    val x1 = center.x + (Math.cos(angle) * 55f).toFloat()
                    val y1 = center.y + (Math.sin(angle) * 55f).toFloat()
                    val x2 = center.x + (Math.cos(angle) * 72f).toFloat()
                    val y2 = center.y + (Math.sin(angle) * 72f).toFloat()
                    drawLine(colors[i % colors.size], Offset(x1, y1), Offset(x2, y2), strokeWidth = 3f)
                }

                // Glowing green/gold radial behind trophy
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF00E676).copy(alpha = 0.25f), Color.Transparent),
                        center = center,
                        radius = 80f
                    )
                )
            }

            // Big Glowing Trophy Icon
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color(0xFFFFD600),
                modifier = Modifier.size(76.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Dynamic Status Headline based on real score
        val headline = if (healthScore >= 85) "Excellent" else if (healthScore >= 70) "Good" else "Optimal"
        Text(
            text = headline,
            color = NovaNeonGreen,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Device Performance!",
            color = Color(0xFFFFD600),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2x2 Stats Grid (Health Score, RAM Free, Storage Free, Temp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ReportMetricCard(
                icon = Icons.Default.Security,
                label = "Health Score",
                value = "$healthScore%",
                iconColor = NovaNeonCyan,
                modifier = Modifier.weight(1f)
            )
            ReportMetricCard(
                icon = Icons.Default.Memory,
                label = "RAM Free",
                value = ramFreeGb,
                iconColor = NovaNeonCyan,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ReportMetricCard(
                icon = Icons.Default.Storage,
                label = "Storage Free",
                value = storageFreeGb,
                iconColor = NovaNeonCyan,
                modifier = Modifier.weight(1f)
            )
            ReportMetricCard(
                icon = Icons.Default.Thermostat,
                label = "Temp",
                value = tempStr,
                iconColor = Color(0xFFFF3D71),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Performance History Card (Section 23)
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Performance History (Local Room)",
                    color = NovaTextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${performanceHistory.size} Snapshots",
                    color = NovaNeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (performanceHistory.isEmpty()) {
                Text(
                    text = "No snapshots recorded yet. Tap 'Record Snapshot' or run an Auto-Scan to save genuine device measurements.",
                    color = NovaTextGray,
                    fontSize = 11.sp
                )
            } else {
                // Genuine Historical Chart: Bar visualization based strictly on saved database values
                Text(
                    text = "Health Score Timeline (Past Measurements)",
                    color = NovaTextGray,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val recent = performanceHistory.take(16).reversed()
                    recent.forEach { snapshot ->
                        val barHeightRatio = (snapshot.healthScore / 100f).coerceIn(0.1f, 1f)
                        val barColor = if (snapshot.healthScore >= 85) NovaNeonGreen else if (snapshot.healthScore >= 70) NovaNeonCyan else NovaNeonYellow

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height((60 * barHeightRatio).dp)
                                .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                .background(barColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    performanceHistory.take(4).forEach { item ->
                        val date = java.text.SimpleDateFormat("MMM dd, HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(item.timestamp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = date, color = NovaTextGray, fontSize = 10.sp)
                            Text(text = "Score: ${item.healthScore}% • RAM: ${item.ramUsagePercentage}% • ${item.batteryTempCelsius.toInt()}°C", color = NovaTextWhite, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = { viewModel.recordPerformanceSnapshot() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Record Snapshot", color = NovaNeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                if (performanceHistory.isNotEmpty()) {
                    TextButton(
                        onClick = { showClearHistoryDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear History", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Status: All systems are optimal!
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NovaNeonGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "All systems are optimal!",
                color = NovaNeonGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Done Action Button
        NeonActionButton(
            text = "Done ✔",
            onClick = onDone,
            testTag = "performance_report_done_button"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showClearHistoryDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = {
                Text(text = "Clear All History?", color = NovaTextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "This will erase all recorded performance snapshots, cleaning history, and gaming session summaries from local storage.",
                    color = NovaTextGray,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearHistoryDialog = false
                    }
                ) {
                    Text("Clear All", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("Cancel", color = NovaNeonCyan)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun ReportMetricCard(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NovaCardBg)
            .border(1.dp, NovaCardBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = NovaTextGray, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = NovaTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
