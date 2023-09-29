package com.example.play4free.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.play4free.data.datamodels.Games
import com.example.play4free.data.datamodels.Giveaways

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameList(gameList: List<Games>)

    @Query("SELECT * FROM games_table")
    fun getAllGames():LiveData<List<Games>>

    @Query("SELECT COUNT (*) FROM games_table")
    fun getCount():Int

    @Query("UPDATE games_table SET isLiked = :liked WHERE id = :id")
    fun updateFav(liked: Boolean, id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGiveawayList(giveawayList: List<Giveaways>)

    @Query("SELECT * FROM giveaways_table")
    fun getGiveaways():LiveData<List<Giveaways>>
}