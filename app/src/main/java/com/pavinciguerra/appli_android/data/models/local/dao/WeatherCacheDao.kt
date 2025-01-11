package com.pavinciguerra.appli_android.data.models.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavinciguerra.appli_android.data.models.local.FavoriteCity
import com.pavinciguerra.appli_android.data.models.local.entities.WeatherCache

interface WeatherCacheDao {
    @Query("SELECT * FROM weather_cache WHERE cityName = :cityName")
    suspend fun getWeatherCache(cityName: String): WeatherCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheWeatherData(weatherCache: WeatherCache)

    @Query("DELETE FROM weather_cache WHERE cityName = :cityName")
    suspend fun clearCache(cityName: String)

    @Query("DELETE FROM weather_cache WHERE timestamp < :timestamp")
    suspend fun clearOldCache(timestamp: Long)
}