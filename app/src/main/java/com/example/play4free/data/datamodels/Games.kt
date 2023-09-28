package com.example.play4free.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games_table")
data class Games(
    @PrimaryKey
    var id: Long = 0L,
    var title: String = "",
    var thumbnail: String = "",
    var short_description: String = "",
    var game_url: String = "",
    var genre: String = "",
    var platform: String = "",
    var publisher: String = "",
    var developer: String = "",
    var release_date: String = "",
    var isLiked: Boolean = false
)