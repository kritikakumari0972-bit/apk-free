package com.example.ui.screens

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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonGlowingCircleGauge
import com.example.ui.components.NeonTopBar
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaCardBorder
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun HomeScreen(
    viewModel: NovaMainViewModel,
    onNavigateToSettings: () -> Unit,
    onShowPerformanceReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ram by viewModel.ramInfo.collectAsState()
    val storage by viewModel.storageInfo.collectAsState()
    val battery by viewModel.batteryInfo.collectAsState()

    val healthScore by viewModel.healthScore.collectAsState()

    val totalRamGb = remember(ram) {
        val gb = ram.totalBytes / (1024.0 * 1024.0 * 1024.0)
        "%.1f".format(gb)
    }
    val usedRamGb = remember(ram) {
        val gb = ram.usedBytes / (1024.0 * 1024.0 * 1024.0)
        "%.1f".format(gb)
    }
    val totalStorageGb = remember(storage) {
        val gb = storage.totalBytes / (1024.0 * 1024.0 * 1024.0)
        "%.1f".format(gb)
    }
    val usedStorageGb = remember(storage) {
        val gb = storage.usedBytes / (1024.0 * 1024.0 * 1024.0)
        "%.1f".format(gb)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBgDark)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        NeonTopBar(
            icon = Icons.Default.RocketLaunch,
            title = "NovaBoost",
            subtitle = "One App. Real Optimization.",
            iconTint = NovaNeonCyan,
            rightIcon = Icons.Default.Settings,
            onRightClick = onNavigateToSettings
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Center Glowing Health Score Gauge
        NeonGlowingCircleGauge(
            score = healthScore,
            label = "Health Score",
            statusText = if (healthScore > 85) "Excellent" else "Good",
            icon = Icons.Default.RocketLaunch,
            sizeDp = 185
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Primary Action: Smart Memory Diagnostics & Trim
        NeonActionButton(
            text = "Smart Memory Diagnostics ⚡",
            onClick = {
                viewModel.purgeNovaBoostMemoryCache()
                onShowPerformanceReport()
            },
            testTag = "home_smart_boost_button"
        )

        Spacer(modifier = Modifier.height(22.dp))

        // 3 Metrics Horizontal Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // RAM Card
            MetricMiniCard(
                icon = Icons.Default.Memory,
                label = "RAM",
                primaryValue = "$usedRamGb GB",
                secondaryValue = "/ $totalRamGb GB",
                modifier = Modifier.weight(1f)
            )
            // Storage Card
            MetricMiniCard(
                icon = Icons.Default.Storage,
                label = "Storage",
                primaryValue = "$usedStorageGb GB",
                secondaryValue = "/ $totalStorageGb GB",
                modifier = Modifier.weight(1f)
            )
            // Battery Card
            MetricMiniCard(
                icon = Icons.Default.BatteryChargingFull,
                label = "Battery",
                primaryValue = if (battery.isAvailable) "${battery.levelPercent}%" else "Unavailable",
                secondaryValue = if (battery.isAvailable) (if (battery.status.contains("Charging", ignoreCase = true)) "Charging" else "Normal") else "Disconnected",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bottom Device Status Card
        NeonCard(
            borderColor = NovaNeonGreen.copy(alpha = 0.35f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NovaNeonGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = NovaNeonGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Device Status",
                        color = NovaTextGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "All Systems Optimal ✔",
                        color = NovaNeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MetricMiniCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    primaryValue: String,
    secondaryValue: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NovaCardBg)
            .border(1.dp, NovaCardBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NovaNeonCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = NovaTextGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = primaryValue,
                color = NovaTextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = secondaryValue,
                color = NovaTextGray,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
