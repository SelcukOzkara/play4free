package com.example.play4free.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games_table")
data class Games(
    @PrimaryKey
    val id: Long,
    val title: String,
    val thumbnail: String,
    val short_description: String,
    val game_url: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    val release_date: String,
    var isLiked: Boolean = false
)