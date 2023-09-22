package com.example.play4free.data.datamodels

data class Profile (
    val pb: String = "",
    val username: String = "",
    val favList: List<Games> = listOf()
)