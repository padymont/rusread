package com.padym.rusread.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppColors.IndianRed,
    background = AppColors.Linen,
    surface = AppColors.Linen,
    onBackground = AppColors.Charcoal,
    onSurface = AppColors.Charcoal,
)

@Composable
fun RusreadTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}