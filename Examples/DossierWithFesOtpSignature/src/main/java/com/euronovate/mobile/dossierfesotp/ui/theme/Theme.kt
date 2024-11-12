package com.euronovate.mobile.dossierfesotp.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColorScheme(
    primary = ENBackgroundBlack,
    onPrimary = ENSoftGray,
    secondary = ENPrimaryOrange,
    secondaryContainer = ENPrimaryOrange,
    onSecondary = ENSoftMedGray,
    onSecondaryContainer = ENSoftMedGray,
    background = ENBackgroundBlack
)

private val LightColorPalette = lightColorScheme(
    primary = ENMediumGray,
    onPrimary = ENSoftGray,
    secondary = ENPrimaryOrange,
    secondaryContainer = ENPrimaryOrange,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    background = ENMediumGray
)

val LocalColors = staticCompositionLocalOf { DarkColorPalette }

@Composable
fun ENMobileSdkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}