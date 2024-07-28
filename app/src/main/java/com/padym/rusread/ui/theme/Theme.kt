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

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    secondary = AppColors.PurpleGrey40,
    tertiary = AppColors.Pink40,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    */
)

@Composable
fun RusreadTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}