package com.rittmann.leagueoflegendschamps.data.network

import com.rittmann.leagueoflegendschamps.data.config.Constants

object ImageUrls {

    fun getSplashUrl(id: String, number: Int) =
        "${Constants.BASE_URL}img/champion/splash/${id}_${number}.jpg"

    fun getChampionSmallImageUrl(id: String) =
        "${Constants.BASE_URL_WITH_VERSION}img/champion/${id}.png"
}