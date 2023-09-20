package com.example.play4free

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.play4free.data.AppRepository
import com.example.play4free.data.datamodels.GameDetail
import com.example.play4free.data.local.getData
import com.example.play4free.data.remote.GamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(application: Application): AndroidViewModel(application) {

    private var database = getData(application)
    private val repo = AppRepository(GamesApi,database)
    val gameList = repo.gameList
    private val _gameDetail: MutableLiveData<GameDetail> = MutableLiveData()
    val gameDetail: LiveData<GameDetail>
        get() = _gameDetail



    fun loadGameList(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getGameList()
        }
    }

    fun getGameDetail(id: Long) {
        viewModelScope.launch (Dispatchers.IO) {
            _gameDetail.postValue(repo.getGameDetail(id)!!)
        }
    }
}