package com.rittmann.leagueoflegendschamps

import android.app.Application
import com.rittmann.leagueoflegendschamps.data.network.ChampionAPI
import com.rittmann.leagueoflegendschamps.repositories.ChampionRepository
import com.rittmann.leagueoflegendschamps.repositories.ChampionRepositoryImpl
import com.rittmann.leagueoflegendschamps.ui.championdetails.ChampionDetailsViewModelFactory
import com.rittmann.leagueoflegendschamps.ui.home.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton


class ApplicationChamps : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        bindApiServices()

        bindRepositories()

        bindModelFactories()
    }

    private fun Kodein.MainBuilder.bindModelFactories() {
        bind() from provider {
            HomeViewModelFactory(instance())
        }

        bind() from provider {
            ChampionDetailsViewModelFactory(instance())
        }
    }

    private fun Kodein.MainBuilder.bindApiServices() {
        bind<ChampionAPI>() with singleton {
            ChampionAPI.invoke(applicationContext)
        }
    }

    private fun Kodein.MainBuilder.bindRepositories() {
        bind<ChampionRepository>() with singleton {
            ChampionRepositoryImpl(instance())
        }
    }
}