package com.pavinciguerra.appli_android.data.repository

import com.google.gson.Gson
import com.pavinciguerra.appli_android.data.models.api.WeatherResponse
import com.pavinciguerra.appli_android.data.models.local.entities.FavoriteCity
import com.pavinciguerra.appli_android.data.models.local.dao.FavoriteCityDao
import com.pavinciguerra.appli_android.data.models.local.dao.WeatherCacheDao
import com.pavinciguerra.appli_android.data.models.local.entities.WeatherCache
import com.pavinciguerra.appli_android.data.remote.api.WeatherApi
import com.pavinciguerra.appli_android.domain.model.CurrentWeather
import com.pavinciguerra.appli_android.domain.model.HourlyWeather
import com.pavinciguerra.appli_android.domain.model.WeatherData

class WeatherRepository(
    private val weatherCacheDao: WeatherCacheDao,
    private val favoriteCityDao: FavoriteCityDao,
    private val weatherApi: WeatherApi
) {
    private val cacheExpirationTime = 30 * 60 * 1000L; // 30 minutes imo c'est pas mal

    private fun isWeatherCacheValid(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp < cacheExpirationTime
    }

    private fun String.toFormattedTime(): String {
        return substringAfter('T').substringBefore(":00")
    }

    // fonction de merde qui marche pas
    // à refaire
    private fun convertResponseToWeatherData(cityName: String, response: WeatherResponse): WeatherData {
        val hourlyForecasts = response.hourly.time.mapIndexed { index, time ->
            HourlyWeather(
                time = time.toFormattedTime(),
                temperature = response.hourly.temperature_2m[index] ?: 0.0,
                humidity = response.hourly.relative_humidity_2m[index] ?: 0,
                apparentTemperature = response.hourly.apparent_temperature[index] ?: 0.0,
                rain = response.hourly.rain[index] ?: 0.0,
                windSpeed = response.hourly.wind_speed_10m[index] ?: 0.0
            )
        }

        val currentWeather = CurrentWeather(
            temperature = response.hourly.temperature_2m.firstOrNull() ?: 0.0,
            humidity = response.hourly.relative_humidity_2m.firstOrNull() ?: 0,
            apparentTemperature = response.hourly.apparent_temperature.firstOrNull() ?: 0.0,
            rain = response.hourly.rain.firstOrNull() ?: 0.0,
            windSpeed = response.hourly.wind_speed_10m.firstOrNull() ?: 0.0
        )

        return WeatherData(
            cityName = cityName,
            currentWeather = currentWeather,
            hourlyForecast = hourlyForecasts
        )
    }

    private suspend fun getWeatherForCity(cityName: String, lat: Double, lon: Double): WeatherData {
        //en gros là on vériife si on a en cache des données qui datent pas trop
        val cachedWeather = weatherCacheDao.getWeatherCache(cityName)

        if (cachedWeather != null && isWeatherCacheValid(cachedWeather.timestamp)) {
            return Gson().fromJson(cachedWeather.weatherData, WeatherData::class.java)
        }

        // si on a pas on fait uje requete api
        try {
            val response = weatherApi.getWeather(lat, lon)
            val weatherData = convertResponseToWeatherData(cityName, response)

            // et on le fout dans le cache
            weatherCacheDao.cacheWeatherData(
                WeatherCache(
                    cityName = cityName,
                    weatherData = Gson().toJson(weatherData),
                    timestamp = System.currentTimeMillis(),
                    latitude = lat,
                    longitude = lon
                )
            )

            return convertResponseToWeatherData(cityName, response)
        } catch (e: Exception) {
            // si on arrive pas à faire la requête api (ex hors ligne) on prend le cache même si ça date
            cachedWeather?.let {
                return Gson().fromJson(it.weatherData, WeatherData::class.java)
            }
            throw e
        }
    }

    suspend fun getFavoriteWeather(): List<WeatherData> {
        val favoriteCities = favoriteCityDao.getAllFavoriteCities().sortedBy { it.name }
        return favoriteCities.map { city ->
            getWeatherForCity(city.name, city.latitude, city.longitude)
        }
    }

    suspend fun addFavoriteCity(city: FavoriteCity) {
        favoriteCityDao.addFavoriteCity(city)
    }

    suspend fun removeFavoriteCity(city: FavoriteCity) {
        favoriteCityDao.removeFavoriteCity(city)
        weatherCacheDao.clearCache(city.name)
    }

    suspend fun isFavoriteCity(cityName: String): Boolean {
        return favoriteCityDao.isCityFavorite(cityName)
    }

    suspend fun getAllFavoriteCities(): List<FavoriteCity> {
        return favoriteCityDao.getAllFavoriteCities()
    }

    suspend fun clearOldCache() {
        val expirationTimestamp = System.currentTimeMillis() - cacheExpirationTime
        weatherCacheDao.clearOldCache(expirationTimestamp)
    }
}