package com.example.ui.screens.more

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Thermostat
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonGlowingCircleGauge
import com.example.ui.components.NeonTopBar
import com.example.ui.components.NeonUsageBars
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
fun BatteryThermalScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val battery by viewModel.batteryInfo.collectAsState()
    var powerSaveActive by remember { mutableStateOf(false) }

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
            icon = Icons.Default.BatteryChargingFull,
            title = "Battery & Thermal",
            subtitle = "Power That Lasts.",
            iconTint = NovaNeonGreen
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Center Big Glowing Battery Gauge
        NeonGlowingCircleGauge(
            score = battery.levelPercent,
            label = "Battery",
            statusText = "",
            icon = Icons.Default.BatteryChargingFull,
            sizeDp = 180
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Battery Status Pill Badge
        val isCharging = battery.status.equals("Charging", ignoreCase = true)
        val isFull = battery.status.equals("Full", ignoreCase = true)
        val badgeText = when {
            isCharging -> "⚡ Charging ⚡"
            isFull -> "Full Charged ✔"
            else -> battery.status
        }
        val badgeColor = when {
            isCharging -> NovaNeonYellow
            isFull -> NovaNeonGreen
            else -> NovaNeonCyan
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(badgeColor.copy(alpha = 0.14f))
                .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = badgeText,
                color = badgeColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dual Cards Row: Temperature & Health
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Temperature Card
            NeonCard(modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF3D71).copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Thermostat, contentDescription = null, tint = Color(0xFFFF3D71), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Temperature", color = NovaTextGray, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${battery.temperatureCelsius.toInt()}°C",
                        color = NovaTextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Health Card
            val isHealthy = battery.health.equals("Good", ignoreCase = true) || battery.health.equals("Normal", ignoreCase = true)
            NeonCard(modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isHealthy) NovaNeonGreen.copy(alpha = 0.16f) else Color(0xFFFF9100).copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = if (isHealthy) NovaNeonGreen else Color(0xFFFF9100),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Health", color = NovaTextGray, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${battery.health} ✔",
                        color = if (isHealthy) NovaNeonGreen else Color(0xFFFF9100),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Battery Usage Histogram Card
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Battery Usage", color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "Last 24h", color = NovaTextGray, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            NeonUsageBars()

            Spacer(modifier = Modifier.height(16.dp))

            NeonActionButton(
                text = if (powerSaveActive) "Power Saving Active ✔" else "Power Saving Mode",
                onClick = { powerSaveActive = !powerSaveActive },
                testTag = "toggle_power_saving_mode"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
