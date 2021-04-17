package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rittmann.leagueoflegendschamps.themes.DivisorColor
import com.rittmann.leagueoflegendschamps.themes.PatternNormalPadding
import com.rittmann.leagueoflegendschamps.themes.UnselectedColor

@Composable
fun HorizontalDivisor(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(top = PatternNormalPadding)
            .fillMaxWidth()
            .height(1.dp)
            .background(DivisorColor)
    ) {}
}

@Composable
fun HorizontalDivisor(
    modifier: Modifier = Modifier,
    color: Color = DivisorColor,
    width: Dp = 0.dp
) {
    if (width == 0.dp) modifier.fillMaxWidth() else modifier.width(width)
    Row(
        modifier = modifier
            .padding(top = PatternNormalPadding)
            .height(1.dp)
            .background(color)
    ) {}
}

@Composable
fun HorizontalSelector(
    modifier: Modifier = Modifier,
    color: Color = UnselectedColor,
    width: Dp = 0.dp
) {
    Row(
        modifier = modifier
            .width(width)
            .height(1.dp)
            .background(color)
    ) {}
}