package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaAmber
import com.example.ui.theme.NovaBorder
import com.example.ui.theme.NovaCobalt
import com.example.ui.theme.NovaDarkSurface
import com.example.ui.theme.NovaDarkSurfaceVariant
import com.example.ui.theme.NovaElectricBlue
import com.example.ui.theme.NovaEmerald
import com.example.ui.theme.NovaPurple
import com.example.ui.theme.NovaRose
import com.example.ui.theme.NovaTextMuted
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary

@Composable
fun NovaCard(
    modifier: Modifier = Modifier,
    borderColor: Color = NovaBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .then(clickModifier),
        color = NovaDarkSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun MetricGauge(
    percent: Int,
    label: String,
    subLabel: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 120,
    accentColor: Color? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (percent.coerceIn(0, 100) / 100f),
        animationSpec = tween(durationMillis = 600),
        label = "gauge_progress"
    )

    val color = accentColor ?: when {
        percent < 60 -> NovaEmerald
        percent < 82 -> NovaAmber
        else -> NovaRose
    }

    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(sizeDp.dp),
            color = NovaDarkSurfaceVariant,
            strokeWidth = 10.dp,
        )
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(sizeDp.dp),
            color = color,
            strokeWidth = 10.dp,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$percent%",
                color = NovaTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = NovaTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            if (subLabel.isNotBlank()) {
                Text(
                    text = subLabel,
                    color = NovaTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun MetricBar(
    percent: Int,
    label: String,
    detailText: String,
    modifier: Modifier = Modifier,
    accentColor: Color? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (percent.coerceIn(0, 100) / 100f),
        animationSpec = tween(durationMillis = 600),
        label = "bar_progress"
    )

    val color = accentColor ?: when {
        percent < 60 -> NovaEmerald
        percent < 82 -> NovaAmber
        else -> NovaRose
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = NovaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = detailText, color = NovaTextSecondary, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = NovaDarkSurfaceVariant,
        )
    }
}

@Composable
fun StatRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = NovaTextPrimary,
    leadingIcon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = NovaElectricBlue,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 6.dp)
                )
            }
            Text(
                text = title,
                color = NovaTextSecondary,
                fontSize = 13.sp
            )
        }
        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusBadge(
    text: String,
    color: Color = NovaEmerald,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HonestyNotice(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NovaCobalt.copy(alpha = 0.12f))
            .border(1.dp, NovaCobalt.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = NovaElectricBlue,
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 1.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    color = NovaElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = message,
                    color = NovaTextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun NovaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "action_button",
    icon: ImageVector? = null,
    isSecondary: Boolean = false,
    isDanger: Boolean = false,
    enabled: Boolean = true
) {
    if (isSecondary) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.testTag(testTag),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = NovaElectricBlue
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(NovaCobalt, NovaElectricBlue)))
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    } else {
        val bgColors = when {
            isDanger -> listOf(NovaRose, Color(0xFFB91C1C))
            else -> listOf(NovaCobalt, NovaElectricBlue)
        }
        Button(
            onClick = onClick,
            modifier = modifier.testTag(testTag),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDanger) NovaRose else NovaCobalt,
                contentColor = Color.White
            )
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}
