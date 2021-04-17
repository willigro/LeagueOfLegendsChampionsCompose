package com.rittmann.leagueoflegendschamps.ui.championdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rittmann.leagueoflegendschamps.base.BaseViewModel
import com.rittmann.leagueoflegendschamps.data.model.Champion
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampion
import com.rittmann.leagueoflegendschamps.data.repositories.ChampionRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class ChampionDetailsViewModel(private val repository: ChampionRepository) : BaseViewModel() {

    var champion by mutableStateOf(Champion())
        private set

    @InternalCoroutinesApi
    fun getChampionBy(resumedChampion: ResumedChampion) {
        executeAsync {
            repository.getChampion(resumedChampion.id).collect {
                val json = it.data

                json?.apply {
                    val data = json.asJsonObject["data"]

                    champion = Gson().fromJson(
                        (data as JsonObject).get(resumedChampion.id),
                        Champion::class.java
                    )
                }
            }
        }
    }
}