package com.example.play4free.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "giveaways_table")
data class Giveaways(
    @PrimaryKey
    val id: Long,
    val title: String,
    val worth: String,
    val image: String,
    val instructions: String,
    val open_giveaway_url: String,
    val published_date: String,
    val status: String
    )