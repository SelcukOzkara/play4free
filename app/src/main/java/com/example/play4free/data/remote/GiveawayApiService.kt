package com.example.play4free.data.remote


import com.example.play4free.data.datamodels.Giveaways
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL2 = "https://www.gamerpower.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL2)
    .build()

interface GiveawayApiService {
    @GET("giveaways")
    suspend fun getGiveawayList(): List<Giveaways>
}

object GiveawayApi {
    val retrofitService: GiveawayApiService by lazy { retrofit.create(GiveawayApiService::class.java) }
}