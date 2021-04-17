package com.rittmann.leagueoflegendschamps.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.leagueoflegendschamps.repositories.ChampionRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: ChampionRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}