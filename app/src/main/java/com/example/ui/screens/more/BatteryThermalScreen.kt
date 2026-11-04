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

        val context = androidx.compose.ui.platform.LocalContext.current
        val powerManager = remember { context.getSystemService(android.content.Context.POWER_SERVICE) as? android.os.PowerManager }
        val isSystemPowerSave = powerManager?.isPowerSaveMode == true

        // Real Telemetry & Power Diagnostics Card
        NeonCard {
            Text(
                text = "Hardware Telemetry & Diagnostics",
                color = NovaTextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Voltage Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Voltage", color = NovaTextGray, fontSize = 12.sp)
                val voltStr = if (battery.voltageMv > 0) {
                    "${battery.voltageMv} mV (${String.format(java.util.Locale.US, "%.2f", battery.voltageMv / 1000f)} V)"
                } else "Reading unavailable"
                Text(text = voltStr, color = NovaNeonCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            // Technology Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Technology", color = NovaTextGray, fontSize = 12.sp)
                Text(text = battery.technology, color = NovaTextWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            // Power Source Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Power Source", color = NovaTextGray, fontSize = 12.sp)
                Text(text = battery.powerSource, color = NovaNeonYellow, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            // Thermal Governor / Status Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Thermal Status", color = NovaTextGray, fontSize = 12.sp)
                Text(
                    text = battery.thermalStatus,
                    color = if (battery.isOverheating) Color(0xFFFF3D71) else NovaNeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // OS Battery Saver Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "OS Battery Saver", color = NovaTextGray, fontSize = 12.sp)
                Text(
                    text = if (isSystemPowerSave) "Active (Restricted)" else "Off (Full Performance)",
                    color = if (isSystemPowerSave) NovaNeonYellow else NovaNeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            NeonActionButton(
                text = "Open System Battery Settings",
                onClick = {
                    try {
                        val intent = android.content.Intent(android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS).apply {
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        try {
                            val fallback = android.content.Intent(android.content.Intent.ACTION_POWER_USAGE_SUMMARY).apply {
                                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(fallback)
                        } catch (_: Exception) {}
                    }
                },
                testTag = "open_system_power_settings"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

