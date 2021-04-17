package com.rittmann.leagueoflegendschamps.ui.championdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.leagueoflegendschamps.data.repositories.ChampionRepository


@Suppress("UNCHECKED_CAST")
class ChampionDetailsViewModelFactory(private val repository: ChampionRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChampionDetailsViewModel(repository) as T
    }
}