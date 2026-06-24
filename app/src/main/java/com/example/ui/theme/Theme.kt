package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkOchre,
    secondary = DarkGray,
    background = DarkCharcoal,
    surface = DarkWarmCream,
    onPrimary = DarkCharcoal,
    onSecondary = DarkAlabaster,
    onBackground = DarkAlabaster,
    onSurface = DarkAlabaster
)

private val LightColorScheme = lightColorScheme(
    primary = ShowroomOchre,
    secondary = ShowroomGray,
    background = ShowroomAlabaster,
    surface = ShowroomWarmCream,
    onPrimary = ShowroomAlabaster,
    onSecondary = ShowroomCharcoal,
    onBackground = ShowroomCharcoal,
    onSurface = ShowroomCharcoal
)

@Composable
fun BhatiaTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
