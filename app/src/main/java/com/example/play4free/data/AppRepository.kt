package com.example.play4free.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.play4free.data.datamodels.GameDetail
import com.example.play4free.data.datamodels.Games
import com.example.play4free.data.local.GameDatabase
import com.example.play4free.data.remote.GamesApi

const val TAG = "AppRepo"

class AppRepository(private val api: GamesApi, private val database: GameDatabase) {

    private val _gameList = database.gameDao.getAllGames()
    val gameList: LiveData<List<Games>>
        get() = _gameList

    suspend fun getGameList(){
        try {
            val gameList = api.retrofitService.getGamesList()
            database.gameDao.insertGameList(gameList)
        }catch (e: Exception) {
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    suspend fun getGameDetail(id: Long): GameDetail?{
        try {
            return api.retrofitService.getGameDetail(id)
        }catch (e:Exception){
            Log.e(TAG, "Error loading Data from API: $e")
            return null
        }
    }

}