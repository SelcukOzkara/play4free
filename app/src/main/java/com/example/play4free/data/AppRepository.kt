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

class AppRepository(
    private val api: GamesApi,
    private val giveawayApi: GiveawayApi,
    private val database: GameDatabase
) {

    //region Variable
    private val _gameList: LiveData<List<Games>> = database.gameDao.getAllGames()

    val gameList: LiveData<List<Games>>
        get() = _gameList

    private val _giveawayList = database.gameDao.getGiveaways()
    val giveawayList: LiveData<List<Giveaways>>
        get() = _giveawayList

    private val _searchResult: MutableLiveData<List<Games>> = MutableLiveData()
    val searchtResult: LiveData<List<Games>>
        get() = _searchResult

    //endregion

    suspend fun getGameList() {
        try {
            if (database.gameDao.getCount() == 0) {
                val gameList = api.retrofitService.getGamesList()
                database.gameDao.insertGameList(gameList)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    suspend fun refreshList() {
        try {
            val gameList = api.retrofitService.getGamesList()
            database.gameDao.insertGameList(gameList)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    fun search(search: String) {
        try {
            _searchResult.postValue(database.gameDao.search(search))
            Log.d("ResultTest", _searchResult.value.toString())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading from Search: $e")
        }
    }

    suspend fun getGameDetail(id: Long): GameDetail? {
        return try {
            api.retrofitService.getGameDetail(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Data from API: $e")
            null
        }
    }


    suspend fun getGiveaway() {
        try {
            val giveawayList = giveawayApi.retrofitService.getGiveawayList()
            database.gameDao.insertGiveawayList(giveawayList)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Data from API: $e")
        }
    }

    fun updateFav(like: Boolean, id: Long) = database.gameDao.updateFav(like, id)
}