package com.rittmann.leagueoflegendschamps.themes

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

private val LightColors = lightColors(
    primary = Red700,
    primaryVariant = Red900,
    onPrimary = Color.White,
    secondary = Red700,
    secondaryVariant = Red900,
    onSecondary = Color.White,
    error = Red800
)

@SuppressLint("ConflictingOnColor")
private val DarkColors = darkColors(
    primary = Red300,
    primaryVariant = Red700,
    onPrimary = Color.Black,
    secondary = Red300,
    onSecondary = Color.White,
    error = Red200,
    onSurface = BlackBackground,
    secondaryVariant = Color(0xFF0AC9F0)
)

@Composable
fun LeagueOfLegendsChampionsTheme(
    windows: Window,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
//        colors = if (darkTheme) DarkColors else LightColors,
        colors = DarkColors,
        typography = JetnewsTypography,
        shapes = JetnewsShapes
    ) {
        windows.statusBarColor = MaterialTheme.colors.surface.toArgb()
        windows.navigationBarColor = MaterialTheme.colors.surface.toArgb()

        @Suppress("DEPRECATION")
        if (MaterialTheme.colors.surface.luminance() > 0.5f) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                windows.decorView.systemUiVisibility = windows.decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        @Suppress("DEPRECATION")
        if (MaterialTheme.colors.surface.luminance() > 0.5f) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                windows.decorView.systemUiVisibility = windows.decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }

        content()
    }
}