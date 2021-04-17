package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SurfaceScreen(modifier: Modifier = Modifier, composable: @Composable () -> Unit) {
    Surface(
        color = MaterialTheme.colors.onSurface,
        contentColor = MaterialTheme.colors.onSecondary,
        modifier = modifier.fillMaxWidth().fillMaxHeight()
    ) {
        composable()
    }
}