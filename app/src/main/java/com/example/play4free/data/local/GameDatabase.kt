package com.example.play4free.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.play4free.data.datamodels.Games
import com.example.play4free.data.datamodels.Giveaways

@Database(entities = [Games::class,Giveaways::class], version = 1)
abstract class GameDatabase : RoomDatabase() {

    abstract val gameDao:  GameDao
}

private lateinit var INSTANCE: GameDatabase

fun getData(context: Context): GameDatabase {
    synchronized(GameDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GameDatabase::class.java,
                "game_database"
            ).build()
        }
    }
    return INSTANCE
}