package com.pavinciguerra.appli_android.data.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCache(
    @PrimaryKey
    val cityName: String,
    val weatherData: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
)