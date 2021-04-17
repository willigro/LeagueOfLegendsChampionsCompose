package com.rittmann.leagueoflegendschamps.screens.comum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.rittmann.leagueoflegendschamps.themes.ChampionAvatarImageDefaultSize
import com.rittmann.leagueoflegendschamps.themes.LoadingBackground

@Composable
fun AvatarImageLoading() {
    Box(
        modifier = Modifier
            .size(ChampionAvatarImageDefaultSize)
            .background(LoadingBackground)
            .alpha(ContentAlpha.disabled)
    )
}

@Composable
fun ChampionImageLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(LoadingBackground)
            .alpha(ContentAlpha.disabled)
    )
}
// CircularProgressIndicator(Modifier.align(Alignment.Center))