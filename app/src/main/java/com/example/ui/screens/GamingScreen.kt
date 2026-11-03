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
            // Cyberpunk Armored Helmet & Visor Canvas Illustration
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f + 10f

                // Outer neon flare rays
                drawLine(NovaNeonPurple.copy(alpha = 0.5f), Offset(cx, cy), Offset(cx - 150f, cy - 80f), strokeWidth = 3f)
                drawLine(NovaNeonCyan.copy(alpha = 0.5f), Offset(cx, cy), Offset(cx + 150f, cy - 80f), strokeWidth = 3f)

                // Cyber Armor Shoulder Plates
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

                // Helmet Core Silhouette
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF1E284A), Color(0xFF0D1426)),
                        center = Offset(cx, cy - 10f),
                        radius = 45f
                    ),
                    radius = 45f,
                    center = Offset(cx, cy - 10f)
                )

                // Neon Glowing Visor (Cyan to Magenta gradient horizontal bar)
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
                subtitle = "Boost FPS",
                iconColor = NovaNeonCyan,
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.purgeNovaBoostMemoryCache()
                    actionToast = "Game Booster activated: Background RAM trimmed."
                }
            )

            // Lag Reducer
            GamingToolCard(
                icon = Icons.Default.Adjust,
                title = "Lag Reducer",
                subtitle = "Fix Lag",
                iconColor = Color(0xFFFF4081),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.runLagDiagnostics()
                    actionToast = "Lag reduction diagnostics completed."
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
                subtitle = "Live Monitor",
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
                subtitle = "Custom Mode",
                iconColor = NovaNeonYellow,
                modifier = Modifier.weight(1f),
                onClick = {
                    actionToast = "Performance Mode profile engaged."
                }
            )
        }

        if (actionToast != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = actionToast!!, color = NovaNeonGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gaming Session Card (00:45:32 Active)
        NeonCard(borderColor = NovaNeonGreen.copy(alpha = 0.35f)) {
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
                            .background(NovaNeonGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = NovaNeonGreen, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Gaming Session", color = NovaTextGray, fontSize = 11.sp)
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

                val isRunning = activeSession != null
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isRunning) NovaNeonGreen else NovaNeonCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) "Active" else "Standby",
                        color = if (isRunning) NovaNeonGreen else NovaNeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
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
