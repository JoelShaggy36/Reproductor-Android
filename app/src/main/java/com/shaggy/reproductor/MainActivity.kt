package com.shaggy.reproductor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.shaggy.reproductor.ui.theme.ReproductorTheme


class MainActivity : ComponentActivity() {
        // El ciclo de vida de la actividad crea y mantiene vivo al ViewModel de forma nativa
        private val viewModel: ReproductorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReproductorTheme {
                ReproductorApp(viewModel = viewModel)
            }
        }
    }
}


