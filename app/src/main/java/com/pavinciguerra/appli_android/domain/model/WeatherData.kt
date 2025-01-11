package com.pavinciguerra.appli_android.domain.model

import android.health.connect.datatypes.units.Temperature

data class WeatherData( // je pense que c'est logique tut ça
    val cityName: String,
    val currentWeather: CurrentWeather,
    val hourlyForecast: List<HourlyWeather>
)

data class CurrentWeather(
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Double,
    val rain: Double,
    val windSpeed: Double
)

data class HourlyWeather(
    val time: String,
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Double,
    val rain: Double,
    val windSpeed: Double
)

// TODO : modiifer au cas où exemple faire plus de classes parce qu'on en a jamais assez #POO