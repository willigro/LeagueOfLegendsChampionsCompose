package com.rittmann.leagueoflegendschamps.base

import androidx.activity.ComponentActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseActivity : ComponentActivity(), KodeinAware {
    override val kodein by kodein()
}