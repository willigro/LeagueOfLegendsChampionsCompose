package com.rittmann.leagueoflegendschamps.data.mock

import com.rittmann.leagueoflegendschamps.data.config.Constants.CHAMPIONS_NAMES
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampion

object MockController {

    fun getAllChampionsName(): List<ResumedChampion> {
        val arr = arrayListOf<ResumedChampion>()

        CHAMPIONS_NAMES.forEach {
            arr.add(ResumedChampion(id = it))
        }

        return arr
    }

    fun getResumeChampion(): ResumedChampion = ResumedChampion()
}