package com.pavinciguerra.appli_android.data.models.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavinciguerra.appli_android.data.models.local.entities.FavoriteCity

@Dao
interface FavoriteCityDao {
    @Query("SELECT * FROM favorite_cities")
    suspend fun getAllFavoriteCities(): List<FavoriteCity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteCity(city: FavoriteCity)

    @Delete
    suspend fun removeFavoriteCity(city: FavoriteCity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE name = :cityName LIMIT 1)")
    // pour vériifer que la ville exitse vraiment parce que
    // grosse grossee flemme de faire de la logique alors que la requête sql fonctionne
    suspend fun isCityFavorite(cityName: String): Boolean
}