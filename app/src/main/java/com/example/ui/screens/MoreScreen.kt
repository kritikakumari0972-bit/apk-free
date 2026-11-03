package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonTopBar
import com.example.ui.screens.more.AppCacheScreen
import com.example.ui.screens.more.AutoScanScreen
import com.example.ui.screens.more.BatteryThermalScreen
import com.example.ui.screens.more.CpuGpuScreen
import com.example.ui.screens.more.DeviceScreen
import com.example.ui.screens.more.NetworkVpnScreen
import com.example.ui.screens.more.SettingsScreen
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaNeonPurple
import com.example.ui.theme.NovaNeonYellow
import com.example.ui.theme.NovaTextDim
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

enum class MoreDestination {
    MENU,
    CPU_GPU,
    BATTERY_THERMAL,
    NETWORK_VPN,
    DEVICE_INFO,
    SETTINGS,
    PERFORMANCE_REPORT,
    APP_CACHE,
    AUTO_SCAN
}

@Composable
fun MoreScreen(
    viewModel: NovaMainViewModel,
    initialDestination: MoreDestination = MoreDestination.MENU,
    modifier: Modifier = Modifier
) {
    var destination by remember(initialDestination) { mutableStateOf(initialDestination) }

    when (destination) {
        MoreDestination.CPU_GPU -> CpuGpuScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.BATTERY_THERMAL -> BatteryThermalScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.NETWORK_VPN -> NetworkVpnScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.DEVICE_INFO -> DeviceScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.SETTINGS -> SettingsScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.PERFORMANCE_REPORT -> PerformanceReportScreen(
            viewModel = viewModel,
            onDone = { destination = MoreDestination.MENU }
        )
        MoreDestination.APP_CACHE -> AppCacheScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.AUTO_SCAN -> AutoScanScreen(
            viewModel = viewModel,
            onBack = { destination = MoreDestination.MENU }
        )
        MoreDestination.MENU -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(NovaBgDark)
                    .padding(horizontal = 18.dp)
            ) {
                NeonTopBar(
                    icon = Icons.Default.MoreHoriz,
                    title = "More Tools",
                    subtitle = "System intelligence, benchmarks & reports.",
                    iconTint = NovaNeonCyan
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        NeonNavTile(
                            title = "CPU & GPU",
                            subtitle = "Real-time clock frequencies, load wave & OpenGL/Vulkan specs",
                            icon = Icons.Default.Speed,
                            accentColor = NovaNeonCyan,
                            testTag = "nav_to_cpu_gpu",
                            onClick = { destination = MoreDestination.CPU_GPU }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Battery & Thermal",
                            subtitle = "Live battery level gauge, cell temperature & power telemetry",
                            icon = Icons.Default.BatteryChargingFull,
                            accentColor = NovaNeonGreen,
                            testTag = "nav_to_battery",
                            onClick = { destination = MoreDestination.BATTERY_THERMAL }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Network & VPN",
                            subtitle = "Download/Upload throughput, ping & VPN status",
                            icon = Icons.Default.NetworkCheck,
                            accentColor = NovaNeonBlue,
                            testTag = "nav_to_network",
                            onClick = { destination = MoreDestination.NETWORK_VPN }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Device Information",
                            subtitle = "Real device specifications, SoC architecture & hardware table",
                            icon = Icons.Default.Info,
                            accentColor = NovaNeonPurple,
                            testTag = "nav_to_device",
                            onClick = { destination = MoreDestination.DEVICE_INFO }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Performance Report",
                            subtitle = "System health audit, live score, free RAM & storage recovery",
                            icon = Icons.Default.EmojiEvents,
                            accentColor = NovaNeonYellow,
                            testTag = "nav_to_report",
                            onClick = { destination = MoreDestination.PERFORMANCE_REPORT }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Settings",
                            subtitle = "Dark mode, auto scan interval, gaming mode & permissions",
                            icon = Icons.Default.Settings,
                            accentColor = NovaNeonCyan,
                            testTag = "nav_to_settings",
                            onClick = { destination = MoreDestination.SETTINGS }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Application Storage & Cache",
                            subtitle = "Per-app cache inspection & Android system storage links",
                            icon = Icons.Default.Apps,
                            accentColor = NovaNeonPurple,
                            testTag = "nav_to_app_cache",
                            onClick = { destination = MoreDestination.APP_CACHE }
                        )
                    }

                    item {
                        NeonNavTile(
                            title = "Auto Scan Diagnostic",
                            subtitle = "Full system hardware, battery & junk file audit",
                            icon = Icons.Default.Radar,
                            accentColor = NovaNeonGreen,
                            testTag = "nav_to_auto_scan",
                            onClick = { destination = MoreDestination.AUTO_SCAN }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun NeonNavTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = NovaTextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = NovaTextGray,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = NovaTextDim,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
