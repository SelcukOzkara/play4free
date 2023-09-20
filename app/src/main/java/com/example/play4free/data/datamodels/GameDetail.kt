package com.example.play4free.data.datamodels

data class GameDetail (
    val id: Long,
    val title: String,
    val thumbnail: String,
    val description: String,
    val game_url: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    val release_date: String,
    val minimum_system_requirements: SystemRequire,
    val screenshots: List<Screenshot>

)