package com.rittmann.leagueoflegendschamps.data.repositories

import com.google.gson.JsonElement
import com.rittmann.leagueoflegendschamps.data.model.RepositoryResource
import com.rittmann.leagueoflegendschamps.data.model.genericFlowHandle
import com.rittmann.leagueoflegendschamps.data.network.ChampionAPI
import kotlinx.coroutines.flow.Flow

interface ChampionRepository {

    suspend fun getChampion(id: String): Flow<RepositoryResource<JsonElement>>

    suspend fun getAllChampions(): Flow<RepositoryResource<JsonElement>>
}

class ChampionRepositoryImpl(private val api: ChampionAPI) : ChampionRepository {

    override suspend fun getChampion(id: String): Flow<RepositoryResource<JsonElement>> {
        return genericFlowHandle {
            api.getChampion(id)
        }
    }

    override suspend fun getAllChampions(): Flow<RepositoryResource<JsonElement>> {
        return genericFlowHandle {
            api.getAllChampions()
        }
    }
}