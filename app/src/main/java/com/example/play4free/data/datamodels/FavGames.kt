package com.example.play4free.data.datamodels

import com.google.firebase.firestore.DocumentId
data class FavGames (
    @DocumentId val id: String = "",
    val favList: List<Games>
)