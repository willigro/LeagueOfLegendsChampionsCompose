package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rittmann.leagueoflegendschamps.themes.DivisorColor
import com.rittmann.leagueoflegendschamps.themes.PatternNormalPadding

@Composable
fun HorizontalDivisor(modifier: Modifier = Modifier){
    Row(modifier = modifier
        .padding(top = PatternNormalPadding)
        .fillMaxWidth()
        .height(1.dp)
        .background(DivisorColor)) {}
}