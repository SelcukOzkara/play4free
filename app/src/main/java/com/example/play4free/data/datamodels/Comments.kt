package com.example.play4free.data.datamodels

import java.sql.Timestamp

data class Comments (
    val uid : String? = "",
    val pb : String? = "",
    val content: String = "",
    val timestamp: Long = 0,
)