package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.services.FloatingOverlayService
import com.example.ui.components.NeonActionButton
import com.example.ui.components.NeonCard
import com.example.ui.components.NeonTopBar
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaCardBorder
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaNeonPurple
import com.example.ui.theme.NovaNeonYellow
import com.example.ui.theme.NovaPurpleGradientEnd
import com.example.ui.theme.NovaPurpleGradientStart
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun GamingScreen(
    viewModel: NovaMainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeSession by viewModel.activeGamingSession.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()
    val customGamePackages by viewModel.customGamePackages.collectAsState()

    var showMyGamesDialog by remember { mutableStateOf(false) }
    var actionToast by remember { mutableStateOf<String?>(null) }

    var showProfileDialog by remember { mutableStateOf(false) }
    val currentGamingProfile by viewModel.gamingProfile.collectAsState()
    val displayInfo by viewModel.displayInfo.collectAsState()
    val endedSessionReport by viewModel.lastEndedSessionReport.collectAsState()
    val lagReport by viewModel.lagDiagnosticReport.collectAsState()

    val myGames = remember(installedApps, customGamePackages) {
        installedApps.filter { it.isGame || customGamePackages.contains(it.packageName) }
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
            icon = Icons.Default.Gamepad,
            title = "Gaming Center",
            subtitle = "Play Smooth. Win Big."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Cyberpunk Gamer Hero Graphic Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF260D4D), Color(0xFF090D1C)),
                        radius = 400f
                    )
                )
                .border(1.dp, NovaNeonPurple.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f + 10f

                drawLine(NovaNeonPurple.copy(alpha = 0.5f), Offset(cx, cy), Offset(cx - 150f, cy - 80f), strokeWidth = 3f)
                drawLine(NovaNeonCyan.copy(alpha = 0.5f), Offset(cx, cy), Offset(cx + 150f, cy - 80f), strokeWidth = 3f)

                val armorPath = Path().apply {
                    moveTo(cx - 80f, cy + 60f)
                    lineTo(cx - 110f, cy + 90f)
                    lineTo(cx - 50f, cy + 90f)
                    close()
                    moveTo(cx + 80f, cy + 60f)
                    lineTo(cx + 110f, cy + 90f)
                    lineTo(cx + 50f, cy + 90f)
                    close()
                }
                drawPath(armorPath, Brush.linearGradient(listOf(NovaNeonPurple, NovaNeonBlue)))

                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF1E284A), Color(0xFF0D1426)),
                        center = Offset(cx, cy - 10f),
                        radius = 45f
                    ),
                    radius = 45f,
                    center = Offset(cx, cy - 10f)
                )

                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        listOf(NovaNeonCyan, Color(0xFFE040FB), NovaNeonCyan)
                    ),
                    topLeft = Offset(cx - 32f, cy - 18f),
                    size = androidx.compose.ui.geometry.Size(64f, 16f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // My Games Button (Purple Gradient Pill)
        NeonActionButton(
            text = "My Games ▶",
            onClick = { showMyGamesDialog = true },
            isPurple = true,
            testTag = "my_games_button"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Quick Tools 2x2 Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Game Booster
            GamingToolCard(
                icon = Icons.Default.Speed,
                title = "Game Booster",
                subtitle = "Trim RAM",
                iconColor = NovaNeonCyan,
                modifier = Modifier.weight(1f),
                onClick = {
                    val freed = viewModel.purgeNovaBoostMemoryCache()
                    actionToast = if (freed > 0) "Game Booster: ${freed / 1024} KB internal buffer cleared." else "Game Booster: Buffer clean. GC requested."
                }
            )

            // Lag Reducer
            GamingToolCard(
                icon = Icons.Default.Adjust,
                title = "Lag Reducer",
                subtitle = "Diagnose Lag",
                iconColor = Color(0xFFFF4081),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.runLagDiagnostics()
                    actionToast = "Lag diagnostics completed below."
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Gaming Overlay
            GamingToolCard(
                icon = Icons.Default.Layers,
                title = "Gaming Overlay",
                subtitle = "Live HUD",
                iconColor = NovaNeonCyan,
                modifier = Modifier.weight(1f),
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                        context.startActivity(intent)
                    } else {
                        try {
                            val serviceIntent = Intent(context, FloatingOverlayService::class.java)
                            context.startService(serviceIntent)
                            actionToast = "Floating Gaming HUD enabled."
                        } catch (e: Exception) {
                            actionToast = "Unable to launch HUD: ${e.message}"
                        }
                    }
                }
            )

            // Game Profiles
            GamingToolCard(
                icon = Icons.Default.Tune,
                title = "Game Profiles",
                subtitle = currentGamingProfile.take(12),
                iconColor = NovaNeonYellow,
                modifier = Modifier.weight(1f),
                onClick = {
                    showProfileDialog = true
                }
            )
        }

        if (actionToast != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = actionToast!!, color = NovaNeonGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FPS & Display Refresh Rate Diagnostics Card (Section 14)
        NeonCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Display & Frame Diagnostics",
                        color = NovaTextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Native Refresh: ${displayInfo.refreshRateHz.toInt()} Hz (${displayInfo.resolution})",
                        color = NovaNeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NovaNeonBlue.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${displayInfo.refreshRateHz.toInt()} FPS Cap",
                        color = NovaNeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "FPS DATA UNAVAILABLE for third-party games (Android OS protects external graphics pipelines from unauthorized profiling). NovaBoost provides display hardware refresh diagnostics instead of fabricated readings.",
                color = NovaTextGray,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gaming Session Card (00:45:32 Active)
        NeonCard(borderColor = if (activeSession != null) NovaNeonGreen.copy(alpha = 0.45f) else NovaCardBorder) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (activeSession != null) NovaNeonGreen.copy(alpha = 0.15f) else NovaNeonBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = if (activeSession != null) NovaNeonGreen else NovaNeonCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        val sessionTitle = activeSession?.let { "${it.gameName} (${it.profileName})" } ?: "Gaming Session"
                        Text(text = sessionTitle, color = NovaTextGray, fontSize = 11.sp)
                        val duration = activeSession?.durationSeconds ?: 0L
                        val mins = duration / 60
                        val secs = duration % 60
                        val hrs = mins / 60
                        Text(
                            text = "%02d:%02d:%02d".format(hrs, mins % 60, secs),
                            color = NovaTextWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (activeSession != null) {
                    TextButton(
                        onClick = { viewModel.stopGameSession() },
                        modifier = Modifier.testTag("stop_gaming_session_button")
                    ) {
                        Text("Stop Session", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NovaNeonCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Standby",
                            color = NovaNeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (lagReport != null) {
            Spacer(modifier = Modifier.height(14.dp))
            NeonCard {
                Text(text = "Lag Diagnostic Report", color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "• RAM: ${lagReport!!.memoryPressureStatus}", color = NovaTextGray, fontSize = 11.sp)
                Text(text = "• Thermal: ${lagReport!!.thermalStatus}", color = NovaTextGray, fontSize = 11.sp)
                Text(text = "• Network: ${lagReport!!.networkLatencyStatus}", color = NovaTextGray, fontSize = 11.sp)
                Text(text = "• Storage: ${lagReport!!.storageHeadroomStatus}", color = NovaTextGray, fontSize = 11.sp)
                if (lagReport!!.recommendations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Action: ${lagReport!!.recommendations.first()}", color = NovaNeonYellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showMyGamesDialog) {
        AlertDialog(
            onDismissRequest = { showMyGamesDialog = false },
            title = {
                Text(text = "My Games", color = NovaTextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (myGames.isEmpty()) {
                        Text(text = "No games installed. You can launch any app from the list.", color = NovaTextGray, fontSize = 13.sp)
                    } else {
                        myGames.forEach { app ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showMyGamesDialog = false
                                        viewModel.launchGameSession(app.appName, app.packageName)
                                    }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = app.appName, color = NovaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = NovaNeonCyan)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMyGamesDialog = false }) {
                    Text("Close", color = NovaNeonCyan)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (showProfileDialog) {
        val profiles = listOf("Performance Monitoring", "Balanced Gaming", "Battery-Conscious Gaming", "Thermal-Conscious Gaming")
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = {
                Text(text = "Select Gaming Profile", color = NovaTextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    profiles.forEach { profile ->
                        val isSelected = currentGamingProfile == profile
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NovaNeonCyan.copy(alpha = 0.15f) else NovaCardBg)
                                .border(1.dp, if (isSelected) NovaNeonCyan else NovaCardBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.setGamingProfile(profile)
                                    actionToast = "Engaged profile: $profile"
                                    showProfileDialog = false
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = profile,
                                color = if (isSelected) NovaNeonCyan else NovaTextWhite,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Cancel", color = NovaNeonCyan)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (endedSessionReport != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSessionReport() },
            title = {
                Text(text = "Gaming Session Summary", color = NovaTextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Game: ${endedSessionReport!!.gameName}", color = NovaNeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Profile: ${endedSessionReport!!.profileName}", color = NovaTextGray, fontSize = 12.sp)
                    val mins = endedSessionReport!!.durationSeconds / 60
                    val secs = endedSessionReport!!.durationSeconds % 60
                    Text(text = "Duration: %02d:%02d".format(mins, secs), color = NovaTextWhite, fontSize = 12.sp)
                    Text(text = "Peak Temperature: ${"%.1f".format(endedSessionReport!!.peakTemperatureCelsius)}°C", color = if (endedSessionReport!!.peakTemperatureCelsius > 41f) Color(0xFFFF5252) else NovaNeonGreen, fontSize = 12.sp)
                    Text(text = "Avg RAM Available: ${endedSessionReport!!.avgAvailableRamMb} MB", color = NovaTextWhite, fontSize = 12.sp)
                    if (endedSessionReport!!.avgPingMs > 0) {
                        Text(text = "Avg Ping: ${endedSessionReport!!.avgPingMs} ms", color = NovaTextWhite, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissSessionReport() }) {
                    Text("Done", color = NovaNeonGreen)
                }
            },
            containerColor = NovaCardBg,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
private fun GamingToolCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(NovaCardBg)
            .border(1.dp, NovaCardBorder, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = NovaTextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = NovaTextGray, fontSize = 10.sp)
        }
    }
}
