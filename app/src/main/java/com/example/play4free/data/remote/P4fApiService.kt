package com.example.play4free.data.remote

import com.example.play4free.data.datamodels.GameDetail
import com.example.play4free.data.datamodels.Games
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://www.freetogame.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface P4fApiService {
    @GET("games")
    suspend fun getGamesList(): List<Games>

    @GET("game")
    suspend fun getGameDetail(@Query("id") id: Long): GameDetail
}

object GamesApi {
    val retrofitService: P4fApiService by lazy { retrofit.create(P4fApiService::class.java) }
}