package com.example.ui.screens.more

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.example.ui.theme.NovaNeonPurple
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun CpuGpuScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val cpu by viewModel.cpuInfo.collectAsState()
    val gpu by viewModel.gpuDiagnostics.collectAsState()
    val battery by viewModel.batteryInfo.collectAsState()
    var selectedTab by remember { mutableStateOf("CPU") }
    var showDetailedInfo by remember { mutableStateOf(false) }

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
            title = "CPU & GPU Telemetry",
            subtitle = "Direct silicon hardware diagnostics",
            rightIcon = Icons.Default.Menu,
            onRightClick = { onBack() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Top Segmented Control (CPU / GPU)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NovaCardBg)
                .border(1.dp, NovaCardBorder, RoundedCornerShape(20.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedTab == "CPU") NovaNeonBlue else Color.Transparent)
                    .clickable { selectedTab = "CPU" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CPU Telemetry",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedTab == "GPU") NovaNeonBlue else Color.Transparent)
                    .clickable { selectedTab = "GPU" }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "GPU Hardware",
                    color = if (selectedTab == "GPU") Color.White else NovaTextGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (selectedTab == "CPU") {
            // CPU Card with Waveform
            NeonCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NovaNeonCyan.copy(alpha = 0.15f))
                            .border(1.5.dp, NovaNeonCyan, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "CPU", color = NovaNeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(text = "Processor Architecture", color = NovaTextGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text(
                            text = if (cpu.isSystemUsageAvailable) "${cpu.cpuUsagePercent}% System Load" else "Usage Restricted (SELinux)",
                            color = NovaTextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        val maxFreq = cpu.coreFrequencies.maxOfOrNull { it.maxFreqKHz } ?: 0L
                        val curFreq = cpu.coreFrequencies.maxOfOrNull { it.curFreqKHz } ?: 0L
                        val clockStr = if (curFreq > 0) com.example.core.util.FormatUtils.formatCpuClock(curFreq)
                        else if (maxFreq > 0) com.example.core.util.FormatUtils.formatCpuClock(maxFreq)
                        else cpu.architecture
                        Text(
                            text = "$clockStr • ${cpu.processorCount} Cores • ${cpu.architecture}",
                            color = NovaTextGray,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "NovaBoost Process Load: ${cpu.processCpuUsagePercent}%",
                            color = NovaNeonCyan,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                NeonWaveGraph(color = NovaNeonCyan)
            }
        } else {
            // GPU Card
            NeonCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NovaNeonPurple.copy(alpha = 0.15f))
                            .border(1.5.dp, NovaNeonPurple, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "GPU", color = NovaNeonPurple, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(text = "Graphics Renderer", color = NovaTextGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text(
                            text = gpu.renderer.take(24),
                            color = NovaTextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "${gpu.openGlVersion} • Vulkan: ${gpu.vulkanLevel}",
                            color = NovaTextGray,
                            fontSize = 11.sp
                        )
                        Text(
                            text = gpu.realTimeLoadStatus,
                            color = NovaNeonPurple,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                NeonWaveGraph(color = NovaNeonPurple)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Temperature Card
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF9100).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Thermostat, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Thermal State", color = NovaTextGray, fontSize = 11.sp)
                    val tempStr = if (battery.isAvailable) "${"%.1f".format(battery.temperatureCelsius)}°C" else "Unavailable"
                    Text(text = "$tempStr (${battery.thermalStatus})", color = NovaTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detailed Hardware Specs Button
        NeonActionButton(
            text = "Detailed Hardware Telemetry",
            onClick = { showDetailedInfo = true },
            testTag = "cpu_gpu_details_button"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDetailedInfo) {
        AlertDialog(
            onDismissRequest = { showDetailedInfo = false },
            title = {
                Text(text = "Hardware Architecture Info", color = NovaTextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(text = "• CPU Architecture: ${cpu.architecture}", color = NovaTextWhite, fontSize = 12.sp)
                    Text(text = "• Logical Processors: ${cpu.processorCount}", color = NovaTextWhite, fontSize = 12.sp)
                    Text(text = "• SoC Model: ${cpu.socModel}", color = NovaTextWhite, fontSize = 12.sp)
                    Text(text = "• Telemetry Mode: ${cpu.cpuUsageMode}", color = NovaTextGray, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• GPU Vendor: ${gpu.vendor}", color = NovaTextWhite, fontSize = 12.sp)
                    val texStr = if (gpu.maxTextureSize > 0) "${gpu.maxTextureSize} px" else "Unavailable (EGL Query Failed)"
                    Text(text = "• Max Texture Dimension: $texStr", color = NovaTextWhite, fontSize = 12.sp)
                    Text(text = "• Vulkan Level: ${gpu.vulkanLevel}", color = NovaTextWhite, fontSize = 12.sp)
                    Text(text = "• ASTC Compression: ${if (gpu.astcSupported) "Supported" else "No"}", color = NovaTextGray, fontSize = 11.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showDetailedInfo = false }) {
                    Text("Close", color = NovaNeonCyan)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
