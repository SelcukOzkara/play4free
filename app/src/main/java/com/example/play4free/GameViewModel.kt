package com.example.play4free

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.play4free.data.AppRepository
import com.example.play4free.data.local.getData
import com.example.play4free.data.remote.GamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(application: Application): AndroidViewModel(application) {

    private var database = getData(application)
    private val repo = AppRepository(GamesApi,database)
    val gameList = repo.gameList


    fun loadGameList(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getGameList()
        }
    }
}