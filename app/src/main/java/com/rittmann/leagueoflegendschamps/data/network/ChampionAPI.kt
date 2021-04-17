package com.rittmann.leagueoflegendschamps.data.network

import android.content.Context
import com.google.gson.JsonElement
import com.rittmann.leagueoflegendschamps.data.config.Constants.BASE_URL_WITH_VERSION
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ChampionAPI {

    @GET("data/en_US/champion/{id}.json")
    suspend fun getChampion(@Path("id") id: String): Response<JsonElement>

    @GET("data/en_US/champion.json")
    suspend fun getAllChampions(): Response<JsonElement>

    companion object {
        operator fun invoke(
            context: Context
        ): ChampionAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_WITH_VERSION)
                .client(RestApiUtil.getOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ChampionAPI::class.java)
        }
    }
}