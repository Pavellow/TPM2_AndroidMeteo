package com.pavinciguerra.appli_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pavinciguerra.appli_android.ui.features.accueil.AccueilView
import com.pavinciguerra.appli_android.ui.features.accueil.AccueilViewModel
import com.pavinciguerra.appli_android.ui.features.accueil.HomeViewModelFactory
import com.pavinciguerra.appli_android.ui.theme.Appli_androidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // bah beaucoup de courage et de volonté pour
        // coder en natif, c'est ce quie je retiens
        setContent {
            Appli_androidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    val viewModel: AccueilViewModel = viewModel(
                        factory = HomeViewModelFactory(application as WeatherAppDeSesMorts)
                    )
                    AccueilView(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}