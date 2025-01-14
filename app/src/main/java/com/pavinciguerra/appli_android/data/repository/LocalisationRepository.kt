package com.pavinciguerra.appli_android.data.repository

import com.pavinciguerra.appli_android.data.models.api.GeocodingResult
import com.pavinciguerra.appli_android.data.remote.api.LocalisationApi

class LocalisationRepository(
    private val locationApi: LocalisationApi
) {
    suspend fun searchCities(query: String): List<GeocodingResult> {
        return try {
            val response = locationApi.searchCity(query)
            response.results
        } catch (e: Exception) {
            // TODO : faire une logique pour gérer l'exception parce que jsp quoi fair elà
            emptyList()
        }
    }

    suspend fun getCityFromCoordinates(latitude: Double, longitude: Double): GeocodingResult? {
        return try {
            val response = locationApi.searchCity("$latitude,$longitude")
            // On prend la ville la plus proche des coordonnées données
            response.results.minByOrNull { result ->
                val latDiff = Math.abs(result.latitude - latitude)
                val lonDiff = Math.abs(result.longitude - longitude)
                latDiff + lonDiff // Simple distance calculation
            }
        } catch (e: Exception) {
            // En cas d'erreur, on retourne null
            null
        }
    }
}