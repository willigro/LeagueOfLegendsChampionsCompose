package com.rittmann.leagueoflegendschamps.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ResumedChampion(
    @SerializedName("version") val version: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("key") val key: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("blurb") val blurb: String = "",
    @SerializedName("info") val resumedChampionData: ResumedChampionData = ResumedChampionData(),
    @SerializedName("image") val resumedChampionImage: ResumedChampionImage = ResumedChampionImage(),
    @SerializedName("tags") val resumedChampionTags: List<String> = emptyList(),
    @SerializedName("partype") val partype: String = "",
    @SerializedName("stats") val resumedChampionStats: ResumedChampionStats = ResumedChampionStats()
) : Serializable

class ResumedChampionData(
    @SerializedName("attack") val attack: Int = 0,
    @SerializedName("defense") val defense: Int = 0,
    @SerializedName("magic") val magic: Int = 0,
    @SerializedName("difficulty") val difficulty: Int = 0
) : Serializable

class ResumedChampionImage(
    @SerializedName("full") val full: String = "",
    @SerializedName("sprite") val sprite: String = "",
    @SerializedName("group") val group: String = "",
    @SerializedName("x") val x: Int = 0,
    @SerializedName("y") val y: Int = 0,
    @SerializedName("w") val w: Int = 0,
    @SerializedName("h") val h: Int = 0
) : Serializable

class ResumedChampionStats(
    @SerializedName("hp") val hp: Float = 0f,
    @SerializedName("hpperlevel") val hpperlevel: Float = 0f,
    @SerializedName("mp") val mp: Float = 0f,
    @SerializedName("mpperlevel") val mpperlevel: Float = 0f,
    @SerializedName("movespeed") val movespeed: Float = 0f,
    @SerializedName("armor") val armor: Float = 0f,
    @SerializedName("armorperlevel") val armorperlevel: Float = 0f,
    @SerializedName("spellblock") val spellblock: Float = 0f,
    @SerializedName("spellblockperlevel") val spellblockperlevel: Float = 0f,
    @SerializedName("attackrange") val attackrange: Float = 0f,
    @SerializedName("hpregen") val hpregen: Float = 0f,
    @SerializedName("hpregenperlevel") val hpregenperlevel: Float = 0f,
    @SerializedName("mpregen") val mpregen: Float = 0f,
    @SerializedName("mpregenperlevel") val mpregenperlevel: Float = 0f,
    @SerializedName("crit") val crit: Float = 0f,
    @SerializedName("critperlevel") val critperlevel: Float = 0f,
    @SerializedName("attackdamage") val attackdamage: Float = 0f,
    @SerializedName("attackdamageperlevel") val attackdamageperlevel: Float = 0f,
    @SerializedName("attackspeedperlevel") val attackspeedperlevel: Float = 0f,
    @SerializedName("attackspeed") val attackspeed: Float = 0f,
) : Serializable {

    @Transient
    var currentLevel = 1

    fun setLevel(level: Int) {
        currentLevel = if (level < 0) 1 else level
    }

    fun Float.getByLevel(factor: Float) = this + (factor * currentLevel)
}