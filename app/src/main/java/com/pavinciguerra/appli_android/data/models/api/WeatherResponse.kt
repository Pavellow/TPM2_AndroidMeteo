package com.pavinciguerra.appli_android.data.models.api

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val hourly_units: HourlyUnits, // doublement chiant
    val hourly: Hourly // chiant
)

data class HourlyUnits(
    val time: String,
    val temperature_2m: String,
    val relative_humidity_2m: String,
    val apparent_temperature: String,
    val rain: String,
    val wind_speed_10m: String
    //chiant
)

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double?>,
    val relative_humidity_2m: List<Int?>,
    val rain: List<Double?>,
    val wind_speed_10m: List<Double?>,
)