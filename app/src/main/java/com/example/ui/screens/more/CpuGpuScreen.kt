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
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tv
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
            title = "CPU & GPU",
            subtitle = "",
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
                    text = "CPU",
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
                    text = "GPU",
                    color = if (selectedTab == "GPU") Color.White else NovaTextGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // CPU Card with Waveform
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cyber CPU Icon Badge
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
                    Text(text = "CPU", color = NovaTextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${cpu.cpuUsagePercent}%",
                            color = NovaTextWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        val maxFreq = cpu.coreFrequencies.maxOfOrNull { it.maxFreqKHz } ?: 0L
                        val curFreq = cpu.coreFrequencies.maxOfOrNull { it.curFreqKHz } ?: 0L
                        val clockStr = if (curFreq > 0) com.example.core.util.FormatUtils.formatCpuClock(curFreq)
                            else if (maxFreq > 0) com.example.core.util.FormatUtils.formatCpuClock(maxFreq)
                            else cpu.architecture
                        Text(
                            text = "$clockStr • ${cpu.processorCount} Cores",
                            color = NovaTextGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            NeonWaveGraph(color = NovaNeonCyan)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // GPU Card with Purple Waveform
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cyber GPU Icon Badge in Purple
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
                    Text(text = "GPU", color = NovaTextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = if (gpu.vulkanSupported) "Active" else "Ready",
                            color = NovaTextWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = gpu.renderer.take(18).ifBlank { "System GPU" },
                            color = NovaTextGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            NeonWaveGraph(color = NovaNeonPurple)
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
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF3D71).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Thermostat, contentDescription = null, tint = Color(0xFFFF3D71), modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(text = "Temperature", color = NovaTextGray, fontSize = 12.sp)
                    Text(
                        text = "${battery.temperatureCelsius.toInt()}°C",
                        color = NovaTextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Detailed Info Pill Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(NovaCardBorder.copy(alpha = 0.5f))
                    .clickable { showDetailedInfo = !showDetailedInfo },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showDetailedInfo) "Hide Details" else "Detailed Info",
                    color = NovaTextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (showDetailedInfo) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Architecture: ${cpu.architecture}", color = NovaTextGray, fontSize = 11.sp)
                    Text(text = "Hardware SoC: ${cpu.hardwareName} (${cpu.socModel})", color = NovaTextGray, fontSize = 11.sp)
                    Text(text = "OpenGL ES: ${gpu.openGlVersion}", color = NovaTextGray, fontSize = 11.sp)
                    Text(text = "Vulkan Level: ${gpu.vulkanLevel}", color = NovaTextGray, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
