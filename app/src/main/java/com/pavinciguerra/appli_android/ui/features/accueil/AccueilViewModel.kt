package com.pavinciguerra.appli_android.ui.features.accueil

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pavinciguerra.appli_android.data.models.api.GeocodingResult
import com.pavinciguerra.appli_android.data.models.local.WeatherDatabase
import com.pavinciguerra.appli_android.data.models.local.entities.FavoriteCity
import com.pavinciguerra.appli_android.data.remote.client.RetrofitClient
import com.pavinciguerra.appli_android.data.repository.LocalisationRepository
import com.pavinciguerra.appli_android.data.repository.WeatherRepository
import com.pavinciguerra.appli_android.domain.model.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccueilViewModel(
    private val weatherRepository: WeatherRepository,
    private val localisationRepository: LocalisationRepository
) : ViewModel() {

    private var lastAction: (() -> Unit)? = null

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<GeocodingResult>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _favoriteWeather = MutableStateFlow<List<WeatherData>>(emptyList())
    val favoriteWeather = _favoriteWeather.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadFavoriteWeather()
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val results = localisationRepository.searchCities(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = "errerur lors de la recherceh : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadFavoriteWeather() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val favorites = weatherRepository.getFavoriteWeather()
                    _favoriteWeather.value = favorites
            } catch (e: Exception) {
                _error.value = "erruer lors du chagement des favoris: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retryLastAction() {
        lastAction?.invoke()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length >= 3) {
            searchCities(query)
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun addFavoriteCity(city: GeocodingResult) {
        viewModelScope.launch {
            try {
                val favoriteCity = FavoriteCity(
                    name = city.name,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    country = "",
                    region = "",
                )
                weatherRepository.addFavoriteCity(favoriteCity)
                loadFavoriteWeather() // Recharger la liste
                _searchQuery.value = "" // Réinitialiser la recherche
                _searchResults.value = emptyList()
            } catch (e: Exception) {
                _error.value = "Erreur lors de l'ajout aux favoris : ${e.message}"
            }
        }
    }

    fun removeFavoriteCity(cityName: String) {
        viewModelScope.launch {
            try {
                val city = weatherRepository.getAllFavoriteCities()
                    .find { it.name == cityName }
                city?.let {
                    weatherRepository.removeFavoriteCity(it)
                    loadFavoriteWeather() // Recharger la liste
                }
            } catch (e: Exception) {
                _error.value = "Erreur lors de la suppression des favoris : ${e.message}"
            }
        }
    }

    fun getLocationWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val cityInfo = localisationRepository.getCityFromCoordinates(latitude, longitude)
                cityInfo?.let { addFavoriteCity(it) }
            } catch (e: Exception) {
                _error.value = "Erreur lors de la géolocalisation : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}


// faudrait utiliser une librairie qui fait le taf pour nous
// au lieu de faire cette merde
class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccueilViewModel::class.java)) {
            // creation manuellzee des dépendances
            val database = WeatherDatabase.getInstance(context)
            val weatherApi = RetrofitClient.weatherApi
            val locationApi = RetrofitClient.locationApi

            val weatherRepository = WeatherRepository(
                database.weatherCacheDao(),
                database.favoriteCityDao(),
                weatherApi
            )

            val locationRepository = LocalisationRepository(locationApi)

            @Suppress("UNCHECKED_CAST")
            return AccueilViewModel(weatherRepository, locationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}