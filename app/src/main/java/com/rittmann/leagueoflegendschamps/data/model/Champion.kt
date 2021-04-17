package com.rittmann.leagueoflegendschamps.data.model

import com.google.gson.annotations.SerializedName

class Champion(
    version: String = "",
    id: String = "",
    key: String = "",
    title: String = "",
    blurb: String = "",
    resumedChampionData: ResumedChampionData = ResumedChampionData(),
    resumedChampionImage: ResumedChampionImage = ResumedChampionImage(),
    resumedChampionTags: List<String> = emptyList(),
    partype: String = "",
    stats: ResumedChampionStats = ResumedChampionStats(),
    @SerializedName("skins") val skins: List<ChampionSkins> = emptyList(),
    @SerializedName("lore") val lore: String = "",
//    @SerializedName("spells") val spells: String = "",
//    @SerializedName("passive") val passive: String = "",
//    @SerializedName("recommended") val recommended: String = "",
) : ResumedChampion(
    version,
    id,
    key,
    title,
    blurb,
    resumedChampionData,
    resumedChampionImage,
    resumedChampionTags,
    partype,
    stats
)

class ChampionSkins(
    @SerializedName("id") val id: String = "",
    @SerializedName("num") val num: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("chromas") val chromas: Boolean = false
)