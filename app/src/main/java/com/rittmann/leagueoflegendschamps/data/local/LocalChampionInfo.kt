package com.rittmann.leagueoflegendschamps.data.local

import androidx.compose.ui.graphics.Color
import com.rittmann.leagueoflegendschamps.themes.*

object LocalChampionInfo {

    val levels = arrayListOf<String>().apply {
        for (i in 1..18) {
            add(i.toString())
        }
    }

    val allAvailableTagsWithColor = arrayListOf(
        TagWithColor("Fighter", ChampionTagFighterColor),
        TagWithColor("Support", ChampionTagSupportColor),
        TagWithColor("Mage", ChampionTagMageColor),
        TagWithColor("Assassin", ChampionTagAssassinColor),
        TagWithColor("Tank", ChampionTagTankColor),
        TagWithColor("Marksman", ChampionTagMarksmanColor)
    )

    fun tagsToTagsWithColor(tags: List<String>): ArrayList<TagWithColor> {
        return arrayListOf<TagWithColor>().apply {
            tags.forEach {
                for (tagWithColor in allAvailableTagsWithColor) {
                    if (it == tagWithColor.tag)
                        add(tagWithColor)
                }
            }
        }
    }

    data class TagWithColor(val tag: String, val color: Color)
}