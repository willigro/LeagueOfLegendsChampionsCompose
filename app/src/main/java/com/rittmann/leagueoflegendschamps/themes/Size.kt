package com.rittmann.leagueoflegendschamps.themes

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val TabPadding = 16.dp

val PatternNormalPadding = 15.dp
val PatternSmallPadding = 8.dp
val PatternSmallPadding_X = 4.dp
val TopTextToIconPadding = 8.dp
val ClickableNormalPadding = 10.dp

val TopSmallPadding = 10.dp
val BottomSmallPadding = 10.dp
val LeftSmallPadding = 8.dp
val RightSmallPadding = 8.dp

val ChampionAvatarImageDefaultSize = 48.dp

val ChampionAvatarBorder = 2.dp
val DefaultBorderStrokeWidth = 1.dp
val ChampionDataBorderStrokeWidth = 10.dp

val TextFieldIconSize = 18.dp

val ToolbarHeight = 80.dp

fun Modifier.allSmallPaddings() = this.padding(
    LeftSmallPadding,
    TopSmallPadding,
    RightSmallPadding,
    BottomSmallPadding
)