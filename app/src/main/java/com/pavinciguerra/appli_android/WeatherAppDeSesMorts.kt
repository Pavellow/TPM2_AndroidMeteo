package com.pavinciguerra.appli_android

import android.app.Application
import androidx.room.Room
import com.pavinciguerra.appli_android.data.models.local.WeatherDatabase

class WeatherAppDeSesMorts : Application() {
    private lateinit var database: WeatherDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }
}