package com.pavinciguerra.appli_android.data.remote.api

import com.pavinciguerra.appli_android.data.models.api.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocalisationApi {
    @GET("v1/search")
    suspend fun searchCity(
        @Query("name") cityName : String
    ): GeocodingResponse
}