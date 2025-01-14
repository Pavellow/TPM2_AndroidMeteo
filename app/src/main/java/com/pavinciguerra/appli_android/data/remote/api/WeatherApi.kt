package com.pavinciguerra.appli_android.data.remote.api

import com.pavinciguerra.appli_android.data.models.api.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast/")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,apparent_temperature,rain,wind_speed_10m",
        @Query("models") models: String = "meteofrance_seamless"
    ): WeatherResponse
}