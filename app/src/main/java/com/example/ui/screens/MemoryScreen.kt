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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonTopBar
import com.example.ui.components.NeonWaveGraph
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
fun MemoryScreen(
    viewModel: NovaMainViewModel,
    modifier: Modifier = Modifier
) {
    val ram by viewModel.ramInfo.collectAsState()
    var optimizedMessage by remember { mutableStateOf<String?>(null) }

    val totalRamGb = remember(ram) {
        com.example.core.util.FormatUtils.formatBytes(ram.totalBytes)
    }
    val availRamGb = remember(ram) {
        com.example.core.util.FormatUtils.formatBytes(ram.availableBytes)
    }
    val zRamGb = remember(ram) {
        if (ram.swapTotalBytes > 0) com.example.core.util.FormatUtils.formatBytes(ram.swapTotalBytes)
        else if (ram.zRamAccessible) "Kernel Active"
        else "OEM Protected"
    }
    val zRamActive = remember(ram) {
        ram.swapTotalBytes > 0 || ram.zRamAccessible
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
            icon = Icons.Default.Memory,
            title = "Virtual RAM",
            subtitle = "More RAM. More Power."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Center Cyber Processor Chip Graphic with Circuit Lines
        Box(
            modifier = Modifier
                .size(190.dp)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center

                // Glow radial
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NovaNeonCyan.copy(alpha = 0.25f), Color.Transparent),
                        center = center,
                        radius = size.minDimension / 2f
                    )
                )

                // Circuit traces radiating out
                val traceColor = NovaNeonCyan.copy(alpha = 0.4f)
                drawLine(traceColor, Offset(center.x - 70f, center.y - 30f), Offset(center.x - 90f, center.y - 50f), strokeWidth = 2f)
                drawLine(traceColor, Offset(center.x + 70f, center.y - 30f), Offset(center.x + 90f, center.y - 50f), strokeWidth = 2f)
                drawLine(traceColor, Offset(center.x - 70f, center.y + 30f), Offset(center.x - 90f, center.y + 50f), strokeWidth = 2f)
                drawLine(traceColor, Offset(center.x + 70f, center.y + 30f), Offset(center.x + 90f, center.y + 50f), strokeWidth = 2f)
            }

            // Realistic Chip Rectangle
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0D2550), Color(0xFF08132B))
                        )
                    )
                    .border(2.dp, NovaNeonCyan, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "RAM+",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(5) {
                            Box(
                                modifier = Modifier
                                    .size(width = 6.dp, height = 3.dp)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(NovaNeonCyan)
                            )
                        }
                    }
                }
            }
        }

        // Swap Support Compatible Pill Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(NovaNeonGreen.copy(alpha = 0.12f))
                .border(1.dp, NovaNeonGreen.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NovaNeonGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Swap Support", color = NovaTextWhite, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Compatible", color = NovaNeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Dual Metrics Card: Physical RAM & zRAM
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Physical RAM Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Physical RAM", color = NovaTextGray, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = totalRamGb, color = NovaTextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Available: $availRamGb", color = NovaTextGray, fontSize = 11.sp)
                }

                // zRAM Column
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "zRAM", color = NovaTextGray, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = zRamGb, color = NovaTextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (zRamActive) NovaNeonGreen.copy(alpha = 0.15f) else NovaTextGray.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (zRamActive) "Active" else "Restricted",
                            color = if (zRamActive) NovaNeonGreen else NovaTextGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Swap File (Without Root) Card
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFB300).copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Swap File", color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Not Active ", color = Color(0xFFFF9100), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "(Without Root)", color = NovaTextGray, fontSize = 11.sp)
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = NovaNeonCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Optimize Memory Action Button
        NeonActionButton(
            text = "Optimize Memory",
            onClick = {
                val freed = viewModel.purgeNovaBoostMemoryCache()
                optimizedMessage = "Cleaned internal buffers. Available RAM refreshed."
            },
            testTag = "optimize_memory_button"
        )

        if (optimizedMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = optimizedMessage!!, color = NovaNeonGreen, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Memory Usage Waveform Card
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Memory Usage", color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = NovaTextGray, modifier = Modifier.size(12.dp))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "${ram.usagePercentage}%", color = NovaTextWhite, fontSize = 24.sp, fontWeight = FontWeight.Black)

            Spacer(modifier = Modifier.height(8.dp))
            NeonWaveGraph(color = NovaNeonCyan)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
