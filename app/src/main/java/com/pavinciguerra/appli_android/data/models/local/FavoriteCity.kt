package com.pavinciguerra.appli_android.data.models.local

// faudrait voir is je peux mettre une bdd sqlite ça serait pas mal en vrai
data class FavoriteCity(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val region: String?
)