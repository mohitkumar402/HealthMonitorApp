package com.healthmonitor.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary            = OrangeAccent,
    onPrimary          = Color.White,
    secondary          = TealAccent,
    onSecondary        = Color.White,
    background         = LightBackground,
    onBackground       = TextPrimary,
    surface            = CardBackground,
    onSurface          = TextPrimary,
    surfaceVariant     = Color(0xFFEEEEEE),
    onSurfaceVariant   = TextSecondary,
)

@Composable
fun HealthMonitorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = Typography,
        content     = content
    )
}
