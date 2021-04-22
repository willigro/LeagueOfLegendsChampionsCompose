package com.rittmann.leagueoflegendschamps.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.LocalContext
import com.rittmann.leagueoflegendschamps.base.BaseActivity
import com.rittmann.leagueoflegendschamps.screens.HomeScreen
import com.rittmann.leagueoflegendschamps.ui.championdetails.ChampionDetailsActivity
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

class HomeActivity : BaseActivity(), KodeinAware {

    private val homeViewModelFactory: HomeViewModelFactory by instance()
    private val homeViewModel by viewModels<HomeViewModel>(factoryProducer = {
        homeViewModelFactory
    })

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current

            HomeScreen(
                window,
                errorConnection = homeViewModel.errorCon,
                errorConnectionRetry = ::getAllChampions,
                list = homeViewModel.championList,
                onFilterTagsSelected = {
                    homeViewModel.filterListByChampionTag(it)
                },
                onChampionNameChanged = {
                    homeViewModel.filterListByChampionId(it)
                },
                onClickSelectedChampion = { champion ->
                    ChampionDetailsActivity.startActivity(context, champion)
                }
            )
        }

        getAllChampions()
    }

    private fun getAllChampions() {
        homeViewModel.getAllChampions()
    }
}