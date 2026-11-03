package com.example.ui.screens.more

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
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
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: NovaMainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var gamingModeToggle by remember { mutableStateOf(true) }
    var overlayToggle by remember { mutableStateOf(true) }
    var tempAlertsToggle by remember { mutableStateOf(true) }

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var historyToast by remember { mutableStateOf(false) }

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
            icon = Icons.Default.Settings,
            title = "Settings",
            subtitle = "Customize NovaBoost.",
            rightIcon = Icons.Default.Tune,
            onRightClick = { onBack() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Section: General
        SectionHeader(text = "General")
        Spacer(modifier = Modifier.height(8.dp))
        NeonCard {
            SettingsNavigationRow(
                icon = Icons.Default.Brightness2,
                title = "Theme",
                value = "Dark Mode",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsNavigationRow(
                icon = Icons.Default.Scanner,
                title = "Auto Scan",
                value = "Daily",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsNavigationRow(
                icon = Icons.Default.Refresh,
                title = "Refresh Interval",
                value = "5 min",
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section: Features (with toggles)
        SectionHeader(text = "Features")
        Spacer(modifier = Modifier.height(8.dp))
        NeonCard {
            SettingsToggleRow(
                icon = Icons.Default.Gamepad,
                title = "Gaming Mode",
                checked = gamingModeToggle,
                onCheckedChange = { gamingModeToggle = it }
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsToggleRow(
                icon = Icons.Default.Layers,
                title = "Floating Overlay",
                checked = overlayToggle,
                onCheckedChange = { overlayToggle = it }
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsToggleRow(
                icon = Icons.Default.NotificationsActive,
                title = "Temperature Alerts",
                checked = tempAlertsToggle,
                onCheckedChange = { tempAlertsToggle = it }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section: Privacy & Data
        SectionHeader(text = "Privacy & Data")
        Spacer(modifier = Modifier.height(8.dp))
        NeonCard {
            SettingsNavigationRow(
                icon = Icons.Default.DeleteSweep,
                title = "Clear History",
                value = "",
                onClick = { showClearHistoryDialog = true }
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsNavigationRow(
                icon = Icons.Default.Security,
                title = "Permission Manager",
                value = "",
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                }
            )
        }

        if (historyToast) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Stored history cleared successfully.", color = NovaNeonGreen, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = {
                Text(text = "Clear History", color = NovaTextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Clear all saved gaming session telemetry records and cleaner history?", color = NovaTextGray, fontSize = 13.sp)
            },
            confirmButton = {
                NeonActionButton(
                    text = "Clear All",
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearHistoryDialog = false
                        historyToast = true
                    },
                    modifier = Modifier.width(110.dp)
                )
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("Cancel", color = NovaTextGray)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            color = NovaTextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(NovaNeonBlue.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = NovaNeonBlue, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value.isNotEmpty()) {
                Text(text = value, color = NovaTextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = NovaTextGray, modifier = Modifier.size(11.dp))
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(NovaNeonBlue.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = NovaNeonBlue, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NovaNeonBlue,
                uncheckedThumbColor = NovaTextGray,
                uncheckedTrackColor = NovaCardBorder
            )
        )
    }
}
