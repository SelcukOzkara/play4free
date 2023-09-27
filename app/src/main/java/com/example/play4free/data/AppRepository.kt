package com.example.play4free.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.play4free.data.datamodels.GameDetail
import com.example.play4free.data.datamodels.Games
import com.example.play4free.data.datamodels.Giveaways
import com.example.play4free.data.local.GameDatabase
import com.example.play4free.data.remote.GamesApi
import com.example.play4free.data.remote.GiveawayApi

const val TAG = "AppRepo"

class AppRepository(private val api: GamesApi,private val giveawayApi: GiveawayApi, private val database: GameDatabase) {

    var newFavList = mutableListOf<Games>()

    private val _gameList = database.gameDao.getAllGames()
    val gameList: LiveData<List<Games>>
        get() = _gameList

    private val _favList = MutableLiveData<List<Games>>()
    val favList: LiveData<List<Games>>
        get() = _favList

    private val _giveawayList = database.gameDao.getGiveaways()
    val giveawayList: LiveData<List<Giveaways>>
        get() = _giveawayList


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

    fun addFav(newGame: Games){
        newFavList.add(newGame)
        _favList.postValue(newFavList)
    }

    fun remFav(oldGame: Games){
        newFavList.remove(oldGame)
        _favList.postValue(newFavList)
    }
    suspend fun getGiveaway(){
        try {
            val giveawayList = giveawayApi.retrofitService.getGiveawayList()
            database.gameDao.insertGiveawayList(giveawayList)
        }catch (e:Exception){
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    fun updateFav(like: Boolean, id: Long) = database.gameDao.updateFav(like, id)
}