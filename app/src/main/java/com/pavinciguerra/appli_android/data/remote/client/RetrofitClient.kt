package com.pavinciguerra.appli_android.data.remote.client

import com.pavinciguerra.appli_android.data.remote.api.LocalisationApi
import com.pavinciguerra.appli_android.data.remote.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitClient {
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"
    private const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/"

    //  config client
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // creation instance retrofit
    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // creation instance geocoding
    private val geocodingRetrofit = Retrofit.Builder()
        .baseUrl(GEOCODING_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // creation interfaces api
    // mais je crois que là j'ai fait de la merde aussi
    val weatherApi: WeatherApi = weatherRetrofit.create(WeatherApi::class.java)
    val locationApi: LocalisationApi = geocodingRetrofit.create(LocalisationApi::class.java)
}