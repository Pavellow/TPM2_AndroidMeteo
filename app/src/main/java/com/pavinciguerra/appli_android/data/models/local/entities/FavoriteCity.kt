package com.pavinciguerra.appli_android.data.models.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

// faudrait voir is je peux mettre une bdd sqlite ça serait pas mal en vrai
// note : room EST une bdd sqlite
@Entity(tableName = "favorite_cities")
data class FavoriteCity(
    @PrimaryKey
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    val region: String?
)