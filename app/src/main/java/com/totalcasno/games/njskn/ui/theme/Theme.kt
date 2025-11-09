package com.totalcasno.games.njskn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkBlueColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = LightBlue,
    tertiary = BrightBlue,
    background = DarkBlue,
    surface = MediumBlue,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun TotalCasnoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkBlueColorScheme,
        typography = Typography,
        content = content
    )
}