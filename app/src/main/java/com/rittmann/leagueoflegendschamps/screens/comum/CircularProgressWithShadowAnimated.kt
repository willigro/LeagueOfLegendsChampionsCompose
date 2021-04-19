package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

private val ShadowColor = Color(0xFF5A4D4D)
private const val animationDelay = 1000

@Composable
fun CircularProgressWithShadowAnimated(
    /*@FloatRange(from = 0.0, to = 100.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    val borderInitialAnimationTrigger = remember { mutableStateOf(false) }
    val dataProgress = updateTransition(borderInitialAnimationTrigger).animateFloat(
        transitionSpec = {
            tween(durationMillis = animationDelay)
        }
    ) {
        if (it.value) progress else 0f
    }

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
    }

    Canvas(
        modifier.focusable()
    ) {
        // Start at 12 O'clock
        val startAngle = 270f
        val sweep = dataProgress.value * 360f
        val shadow = (1.0 - dataProgress.value).toFloat() * 360f
        drawCircularIndicatorWithShadow(startAngle, sweep, shadow, color, stroke)
    }

    if (progress > 0f && borderInitialAnimationTrigger.value.not()) {
        borderInitialAnimationTrigger.value = true
    }
}

private fun DrawScope.drawCircularIndicatorWithShadow(
    startAngle: Float,
    sweep: Float,
    shadow: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )

    if (shadow > 0f) {
        drawArc(
            color = ShadowColor,
            startAngle = sweep - 90f,
            sweepAngle = shadow,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )
    }
}