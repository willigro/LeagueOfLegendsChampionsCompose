package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.rittmann.leagueoflegendschamps.util.log

/**
 * Code from https://gist.github.com/gildor/ff7f56da7216ae9e4da77368a4beb87a
 * */

/**
 * Border definition can be extended to provide border style or [androidx.compose.ui.graphics.Brush]
 * One more way is make it sealed class and provide different implementations:
 * SolidBorder, DashedBorder etc
 */
data class Border(val strokeWidth: Dp, val color: Color)
data class BorderAnim(val strokeWidth: Dp, val color: Color, val to: Dp)
data class BorderAnimTwo(
    val strokeWidth: Dp,
    val color: Color,
    val path: List<Dp>,
    val draw: Boolean,
    val start: (Float) -> Unit
)

@Stable
fun Modifier.borderBySides(
    start: Border? = null,
    top: Border? = null,
    end: Border? = null,
    bottom: Border? = null,
) =
    drawBehind {
        start?.let {
            drawStartBorder(it, shareTop = top != null, shareBottom = bottom != null)
        }
        top?.let {
            drawTopBorder(it, shareStart = start != null, shareEnd = end != null)
        }
        end?.let {
            drawEndBorder(it, shareTop = top != null, shareBottom = bottom != null)
        }
        bottom?.let {
            drawBottomBorder(border = it, shareStart = start != null, shareEnd = end != null)
        }
    }


@Stable
fun Modifier.borderAnim(
    start: BorderAnim? = null,
    end: BorderAnim? = null,
) =
    drawBehind {
        start?.let {
            drawStartBorder(it)
        }
        end?.let {
            drawEndBorder(it)
        }
    }

private fun DrawScope.drawTopBorder(
    border: Border,
    shareStart: Boolean = true,
    shareEnd: Boolean = true
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            moveTo(0f, 0f)
            lineTo(if (shareStart) strokeWidthPx else 0f, strokeWidthPx)
            val width = size.width
            lineTo(if (shareEnd) width - strokeWidthPx else width, strokeWidthPx)
            lineTo(width, 0f)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawBottomBorder(
    border: Border,
    shareStart: Boolean,
    shareEnd: Boolean
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            val width = size.width
            val height = size.height
            moveTo(0f, height)
            lineTo(if (shareStart) strokeWidthPx else 0f, height - strokeWidthPx)
            lineTo(if (shareEnd) width - strokeWidthPx else width, height - strokeWidthPx)
            lineTo(width, height)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawStartBorder(
    border: BorderAnim
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            val height = size.height
            moveTo(0f, height)
            lineTo(strokeWidthPx, height)
            lineTo(strokeWidthPx, border.to.toPx())
            lineTo(0f, border.to.toPx())
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawStartBorder(
    border: Border,
    shareTop: Boolean = true,
    shareBottom: Boolean = true
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            moveTo(0f, 0f)
            lineTo(strokeWidthPx, if (shareTop) strokeWidthPx else 0f)
            val height = size.height
            lineTo(strokeWidthPx, if (shareBottom) height - strokeWidthPx else height)
            lineTo(0f, height)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawEndBorder(
    border: Border,
    shareTop: Boolean = true,
    shareBottom: Boolean = true
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            val width = size.width
            val height = size.height
            moveTo(width, 0f)
            lineTo(width - strokeWidthPx, if (shareTop) strokeWidthPx else 0f)
            lineTo(width - strokeWidthPx, if (shareBottom) height - strokeWidthPx else height)
            lineTo(width, height)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawEndBorder(
    border: BorderAnim
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            val width = size.width
            val height = size.height
            moveTo(width, height)
            lineTo(width - strokeWidthPx, height)
            lineTo(width - strokeWidthPx, border.to.toPx())
            lineTo(width, border.to.toPx())
            close()
        },
        color = border.color
    )
}

@Stable
fun Modifier.borderAnim(
    animTwo: BorderAnimTwo
) = drawBehind {
    animTwo.start(size.height)

    val stroke = Stroke(width = animTwo.strokeWidth.toPx())
    val initialBottom = size.width / 2

    if (animTwo.draw.not()) return@drawBehind

    // mid to Start
    drawPath(
        Path().apply {
            moveTo(initialBottom, size.height)
            lineTo(animTwo.path[0].toPx(), size.height)
            close()
        },
        color = animTwo.color,
        style = stroke
    )

    // mid to End
    drawPath(
        Path().apply {
            moveTo(initialBottom, size.height)
            lineTo(animTwo.path[1].toPx(), size.height)
            close()
        },
        color = animTwo.color,
        style = stroke
    )

    // to Top start
    val hToTop = animTwo.path[2].toPx()
    val height = if(hToTop > size.height) size.height else hToTop

    drawPath(
        Path().apply {
            moveTo(0f, size.height)
            lineTo(0f, height)
            close()
        },
        color = animTwo.color,
        style = stroke
    )

    // to Top end
    drawPath(
        Path().apply {
            moveTo(size.width, size.height)
            lineTo(size.width, height)
            close()
        },
        color = animTwo.color,
        style = stroke
    )

    // start to Mid
    drawPath(
        Path().apply {
            moveTo(0f, 0f)
            lineTo(animTwo.path[3].toPx(), 0f)
            close()
        },
        color = animTwo.color,
        style = stroke
    )

    // end to Mid
    drawPath(
        Path().apply {
            moveTo(size.width, 0f)
            lineTo(animTwo.path[4].toPx(), 0f)
            close()
        },
        color = animTwo.color,
        style = stroke
    )
}