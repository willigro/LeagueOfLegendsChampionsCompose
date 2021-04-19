package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.rittmann.leagueoflegendschamps.util.log

private val ShadowColor = Color(0xFF5A4D4D)

@Composable
fun CircularProgressWithShadow(
    /*@FloatRange(from = 0.0, to = 100.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    "$progress".log()
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
    }
    Canvas(
        modifier.focusable()
    ) {
        // Start at 12 O'clock
        val startAngle = 270f
        val sweep = progress * 360f
        val shadow = (1.0 - progress).toFloat() * 360f
        drawCircularIndicatorWithShadow(startAngle, sweep, shadow, color, stroke)
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