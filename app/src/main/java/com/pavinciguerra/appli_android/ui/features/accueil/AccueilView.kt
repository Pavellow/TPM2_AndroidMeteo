package com.pavinciguerra.appli_android.ui.features.accueil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pavinciguerra.appli_android.data.models.api.GeocodingResult
import com.pavinciguerra.appli_android.domain.model.WeatherData

@Composable
// la vue m'a été donnée par dieu
// sinon je ne sais même pas à quoi elle
// ressemble
fun AccueilView(
    viewModel: AccueilViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val favoriteWeather by viewModel.favoriteWeather.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        SearchBarWithLocation(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            onLocationClick = { /* TODO: faire la géoloc mais giga flemme */ },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // là aussi jsp à quoi ça ressemble mais inchallah c'est stylé
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chargement")
                }
            }
            error != null -> {
                ErrorMessage(
                    message = error ?: "",
                    onRetryClick = viewModel::retryLastAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            searchQuery.isNotEmpty() -> {
                SearchResultsList(
                    results = searchResults,
                    onResultClick = viewModel::addFavoriteCity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            else -> {
                FavoritesList(
                    favorites = favoriteWeather,
                    onRemoveClick = viewModel::removeFavoriteCity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    results: List<GeocodingResult>,
    onResultClick: (GeocodingResult) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results) { result ->
            SearchResultItem(
                result = result,
                onResultClick = { onResultClick(result) }
            )
        }
    }
}

@Composable
private fun FavoritesList(
    favorites: List<WeatherData>,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (favorites.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ajoutez des villes en facoris pour voir la météo",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favorites) { weather ->
                WeatherCard(
                    weatherData = weather,
                    onRemoveClick = { onRemoveClick(weather.cityName) }
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryClick) {
            Text("Réessayer")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarWithLocation(
    value: String,
    onValueChange: (String) -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Rechercher une ville...") },
            leadingIcon = { Icon(Icons.Default.Search, "Rechercher") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        IconButton(onClick = onLocationClick) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Utiliser ma position"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherCard(
    weatherData: WeatherData,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { /* TODO: faire le nav vers le détail
        mais déjà si on affche la page d'accueil c'est bien
        */ }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = weatherData.cityName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${weatherData.currentWeather.temperature}°C",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(onClick = onRemoveClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Retirer des favoris"
                        )
                    }
                    Text(
                        text = "Vent: ${weatherData.currentWeather.windSpeed} km/h",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    result: GeocodingResult,
    onResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onResultClick),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = result.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = buildString {
                    append(result.country)
                    result.admin1?.let { append(", $it") }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}