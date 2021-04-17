package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun Rows(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content,
        modifier,
        { measurables, constraints ->
            // Don't constrain child views further, measure them with given constraints
            // List of measured children
            val placeables = measurables.map { measurable ->
                // Measure each children
                measurable.measure(constraints.copy(minHeight = 0))
            }

            // Set the size of the layout as big as it can
            layout(constraints.maxWidth, constraints.maxHeight) {
                var yPosition = 0
                var xPosition = 0

                for (i in placeables.indices) {
                    val placeable = placeables[i]

                    val next = if (i < placeables.size - 1) placeables[i + 1] else null

                    placeable.place(x = xPosition, y = yPosition)

                    if (xPosition + placeable.width + (next?.width ?: 0) > constraints.maxWidth) {
                        yPosition += placeable.height
                        xPosition = 0
                    } else {
                        xPosition += placeable.width
                    }
                }
            }
        }
    )
}