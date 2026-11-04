package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaButtonGradientEnd
import com.example.ui.theme.NovaButtonGradientStart
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaCardBgSecondary
import com.example.ui.theme.NovaCardBorder
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaNeonYellow
import com.example.ui.theme.NovaNeonGreen
import com.example.ui.theme.NovaNeonPurple
import com.example.ui.theme.NovaPurpleGradientEnd
import com.example.ui.theme.NovaPurpleGradientStart
import com.example.ui.theme.NovaTextDim
import com.example.ui.theme.NovaTextGray
import com.example.ui.theme.NovaTextWhite

@Composable
fun NeonTopBar(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconTint: Color = NovaNeonCyan,
    rightIcon: ImageVector? = null,
    onRightClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(iconTint.copy(alpha = 0.25f), NovaNeonBlue.copy(alpha = 0.1f))
                        )
                    )
                    .border(1.dp, iconTint.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = NovaTextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        color = NovaTextGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (rightIcon != null && onRightClick != null) {
            IconButton(
                onClick = onRightClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = NovaTextGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    borderColor: Color = NovaCardBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickMod = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .then(clickMod),
        color = NovaCardBg,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun NeonActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPurple: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    testTag: String = "action_button"
) {
    val brush = if (!enabled) {
        Brush.horizontalGradient(listOf(Color(0xFF1E2838), Color(0xFF151D2A)))
    } else if (isPurple) {
        Brush.horizontalGradient(listOf(NovaPurpleGradientStart, NovaPurpleGradientEnd))
    } else {
        Brush.horizontalGradient(listOf(NovaButtonGradientStart, NovaButtonGradientEnd))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(brush)
            .clickable(enabled = enabled) { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = if (enabled) Color.White else NovaTextGray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = if (enabled) Color.White else NovaTextGray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun NeonGlowingCircleGauge(
    score: Int,
    label: String,
    statusText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    sizeDp: Int = 170
) {
    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Background concentric rings
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = this.center
            val radius = size.minDimension / 2f

            // Outer cyan glow ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NovaNeonCyan.copy(alpha = 0.18f), Color.Transparent),
                    center = center,
                    radius = radius
                )
            )

            // Circular stroke track
            drawCircle(
                color = NovaCardBgSecondary,
                radius = radius - 10f,
                style = Stroke(width = 8f)
            )

            // Neon glowing arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(NovaNeonBlue, NovaNeonCyan, NovaNeonGreen, NovaNeonBlue)
                ),
                startAngle = -90f,
                sweepAngle = 360f * (score / 100f),
                useCenter = false,
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
        }

        // Inner Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NovaNeonCyan,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = label,
                color = NovaTextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$score%",
                color = NovaTextWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = statusText,
                color = NovaNeonGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun NeonWaveGraph(
    color: Color = NovaNeonCyan,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
    ) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(0f, h * 0.7f)
            cubicTo(w * 0.2f, h * 0.9f, w * 0.35f, h * 0.2f, w * 0.5f, h * 0.45f)
            cubicTo(w * 0.65f, h * 0.7f, w * 0.8f, h * 0.1f, w, h * 0.35f)
        }

        // Glow stroke
        drawPath(
            path = path,
            color = color.copy(alpha = 0.35f),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
        // Core crisp stroke
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun NeonUsageBars(modifier: Modifier = Modifier) {
    val barRatios = listOf(0.35f, 0.5f, 0.7f, 0.4f, 0.85f, 0.6f, 0.95f, 0.5f, 0.45f, 0.75f, 0.3f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        barRatios.forEachIndexed { i, ratio ->
            val barColor = if (i % 3 == 0) NovaNeonYellow else NovaNeonCyan
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height((46 * ratio).dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}
