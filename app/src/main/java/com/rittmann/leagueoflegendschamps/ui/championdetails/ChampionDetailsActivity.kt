package com.rittmann.leagueoflegendschamps.ui.championdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.rittmann.leagueoflegendschamps.base.BaseActivity
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampion
import com.rittmann.leagueoflegendschamps.screens.ChampionDetailsScreen
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.erased.instance

class ChampionDetailsActivity : BaseActivity() {

    private val resumedChampion: ResumedChampion by lazy {
        intent!!.extras!!.getSerializable(RESUMED_CHAMP) as ResumedChampion
    }

    private val championDetailsViewModelFactory: ChampionDetailsViewModelFactory by instance()
    private val championDetailsViewModel by viewModels<ChampionDetailsViewModel>(factoryProducer = {
        championDetailsViewModelFactory
    })

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        championDetailsViewModel.getChampionBy(resumedChampion)

        setContent {
            ChampionDetailsScreen(window, championDetailsViewModel.champion)
        }
    }

    companion object {
        private const val RESUMED_CHAMP = "argsRC"

        fun startActivity(context: Context, resumedChampion: ResumedChampion) {
            Intent(context, ChampionDetailsActivity::class.java).apply {
                putExtra(RESUMED_CHAMP, resumedChampion)
                context.startActivity(this)
            }
        }
    }
}