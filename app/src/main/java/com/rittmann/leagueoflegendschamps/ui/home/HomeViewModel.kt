package com.rittmann.leagueoflegendschamps.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rittmann.leagueoflegendschamps.base.BaseViewModel
import com.rittmann.leagueoflegendschamps.data.config.Constants.CHAMPIONS_NAMES
import com.rittmann.leagueoflegendschamps.data.local.LocalChampionInfo
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampion
import com.rittmann.leagueoflegendschamps.data.repositories.ChampionRepository
import java.util.*
import kotlinx.coroutines.flow.collect
import kotlin.concurrent.schedule

class HomeViewModel(private val repository: ChampionRepository) : BaseViewModel() {

    private var timer: Timer? = null
    private var completeList: List<ResumedChampion> = emptyList()
    var championList by mutableStateOf(listOf<ResumedChampion>())
        private set

    fun getAllChampions() {
        executeAsync {
            repository.getAllChampions().collect { response ->
                if (tryHandleResponse(response)) return@collect

                val json = response.data

                json?.apply {
                    val data = json.asJsonObject["data"]

                    val champions = arrayListOf<ResumedChampion>()
                    CHAMPIONS_NAMES.forEach { name ->
                        champions.add(
                            Gson().fromJson(
                                (data as JsonObject).get(name),
                                ResumedChampion::class.java
                            )
                        )
                    }

                    championList = champions
                    completeList = champions
                }
            }
        }
    }

    fun filterListByChampionId(id: String) {
        timer = newTimer()

        timer?.schedule(300L) {
            championList = if (id.isEmpty()) completeList
            else {
                val arr = arrayListOf<ResumedChampion>()
                completeList.forEach {
                    if (it.id.toLowerCase(Locale.getDefault())
                            .contains(id.toLowerCase(Locale.getDefault()))
                    )
                        arr.add(it)
                }
                arr
            }
        }
    }

    fun filterListByChampionTag(tags: List<LocalChampionInfo.TagWithColor>) {
        championList = if (tags.isNotEmpty()) {
            val arr = arrayListOf<ResumedChampion>()
            for (champion in completeList) {
                var containsAmount = 0
                for (tagFromChampion in champion.resumedChampionTags) {
                    for (tag in tags) {
                        if (tag.tag == tagFromChampion)
                            containsAmount++
                    }
                }

                if (containsAmount == tags.size)
                    arr.add(champion)
            }
            arr
        } else {
            completeList
        }
    }

    private fun newTimer(): Timer {
        timer?.cancel()
        return Timer("Timer to update content", false)
    }
}