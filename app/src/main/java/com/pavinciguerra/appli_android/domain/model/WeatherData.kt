package com.pavinciguerra.appli_android.domain.model

data class WeatherData(
    val cityName: String,
    val currentWeather: CurrentWeather,
    val hourlyForecast: List<HourlyWeather>,
)

data class CurrentWeather(
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Comparable<*>?,
    val rain: Double,
    val windSpeed: Double
)

data class HourlyWeather(
    val time: String,
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Comparable<*>?,
    val rain: Double,
    val windSpeed: Double
)