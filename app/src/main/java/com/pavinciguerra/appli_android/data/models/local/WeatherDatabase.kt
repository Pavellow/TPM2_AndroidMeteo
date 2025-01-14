package com.pavinciguerra.appli_android.data.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pavinciguerra.appli_android.data.models.local.dao.FavoriteCityDao
import com.pavinciguerra.appli_android.data.models.local.dao.WeatherCacheDao
import com.pavinciguerra.appli_android.data.models.local.entities.FavoriteCity
import com.pavinciguerra.appli_android.data.models.local.entities.WeatherCache

@Database(
    entities = [WeatherCache::class, FavoriteCity::class],
    version = 1,
    exportSchema = false
)

// franchement ça a l'air de marcher donc on laisse
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherCacheDao() : WeatherCacheDao
    abstract fun favoriteCityDao() : FavoriteCityDao

    companion object {
        @Volatile
        private var INSTANCE : WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}