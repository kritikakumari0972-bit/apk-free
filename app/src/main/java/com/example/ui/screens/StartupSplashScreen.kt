package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonPurple
import com.example.ui.theme.NovaTextDim
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite
import com.example.ui.viewmodel.NovaMainViewModel

@Composable
fun StartupSplashScreen(
    viewModel: NovaMainViewModel,
    onFinish: () -> Unit
) {
    val isInitialized by viewModel.isAppInitialized.collectAsState()
    val statusText by viewModel.initializationStatus.collectAsState()

    val scaleAnim = remember { Animatable(0.75f) }
    val alphaAnim = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1.0f, tween(600))
        alphaAnim.animateTo(1.0f, tween(500))
    }

    LaunchedEffect(isInitialized) {
        if (isInitialized) {
            kotlinx.coroutines.delay(450L)
            onFinish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030712),
                        Color(0xFF070E20),
                        Color(0xFF130924),
                        Color(0xFF030712)
                    )
                )
            )
            .clickable { onFinish() }, // Tap to skip immediately
        contentAlignment = Alignment.Center
    ) {
        // Background cyber particle orbits
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = center.x
            val cy = center.y

            // Radial cyber ambient halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NovaNeonPurple.copy(alpha = 0.25f * pulseGlow),
                        NovaNeonCyan.copy(alpha = 0.15f * pulseGlow),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.minDimension * 0.45f
                )
            )

            // Outer decorative segmented orbit ring
            drawCircle(
                color = NovaNeonBlue.copy(alpha = 0.2f),
                radius = 140f,
                style = Stroke(width = 1.5f)
            )
            drawCircle(
                color = NovaNeonCyan.copy(alpha = 0.12f),
                radius = 175f,
                style = Stroke(width = 1f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Animated NovaBoost Core Emblem
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(scaleAnim.value),
                contentAlignment = Alignment.Center
            ) {
                // Outer glowing gradient ring
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(
                                    NovaNeonCyan,
                                    NovaNeonPurple,
                                    NovaNeonBlue,
                                    NovaNeonCyan
                                )
                            )
                        )
                )

                // Inner core midnight disk
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF020617))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = "NovaBoost",
                        tint = NovaNeonCyan,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Branding Typography
            Text(
                text = "NOVABOOST",
                color = NovaTextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "One App. Real Optimization.\nComplete Device Intelligence.",
                color = NovaTextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Initialization Live Status Readout
            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = NovaNeonCyan,
                trackColor = NovaNeonBlue.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = statusText,
                color = NovaNeonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap to enter",
                color = NovaTextDim,
                fontSize = 10.sp
            )
        }
    }
}
