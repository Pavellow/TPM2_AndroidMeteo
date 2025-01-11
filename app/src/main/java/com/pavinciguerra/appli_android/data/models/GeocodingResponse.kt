package com.pavinciguerra.appli_android.data.models

data class GeocodingResponse (
    val resuts: List<GeocodingResult>,
    val generationTime_ms: Double
    )

data class GeocodingResult(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Double, // je pense que dire altitude c'est trop demandé chez geocoding
    val feature_code: String,
    val country_code: String,
    val timezone: String,
    val population: Int,
    val country: String,
    val admin1: String?, // ????????? nomenclature de merde
    val admin2: String?, // là ausssi
    val admin3: String?,
    val admin4: String
)