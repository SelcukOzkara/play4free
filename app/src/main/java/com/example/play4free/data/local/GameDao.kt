package com.example.play4free.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.play4free.data.datamodels.Games

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameList(gameList: List<Games>)

    @Query("SELECT * FROM games_table")
    fun getAllGames():LiveData<List<Games>>

}