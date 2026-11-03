package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NovaDarkColorScheme = darkColorScheme(
    primary = NovaNeonCyan,
    onPrimary = NovaBgDark,
    primaryContainer = NovaNeonBlue,
    onPrimaryContainer = NovaTextWhite,
    secondary = NovaNeonPurple,
    onSecondary = NovaTextWhite,
    background = NovaBgDark,
    onBackground = NovaTextWhite,
    surface = NovaCardBg,
    onSurface = NovaTextWhite,
    surfaceVariant = NovaCardBgSecondary,
    onSurfaceVariant = NovaTextGray,
    outline = NovaCardBorder,
    error = NovaNeonRed,
    onError = NovaTextWhite
)

@Composable
fun NovaBoostTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NovaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
