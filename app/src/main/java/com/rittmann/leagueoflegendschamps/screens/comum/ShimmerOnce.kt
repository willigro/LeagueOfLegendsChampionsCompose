package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import com.rittmann.leagueoflegendschamps.themes.ShimmerColors

class ShimmerOnce(val offset: Offset, val alpha: Float, val execute: (Size) -> Unit) {
    var started = false
}

@Stable
fun Modifier.shimmer(shimmer: ShimmerOnce) =
    drawBehind {
        if (size.width > 0 && shimmer.started.not()) {
            shimmer.started = true
            shimmer.execute(size)
        }

        val linearGradientBrush = Brush.linearGradient(
            colors = ShimmerColors
        )
        drawRect(
            linearGradientBrush,
            topLeft = shimmer.offset,
            size = size.copy(width = shimmer.offset.x),
            alpha = shimmer.alpha
        )
    }