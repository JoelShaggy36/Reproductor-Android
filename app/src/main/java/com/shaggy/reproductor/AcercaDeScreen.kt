package com.shaggy.reproductor


import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AcercaDeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reproductor Offline", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Versión 1.0.0", fontSize = 16.sp, color = Modifier.padding(bottom = 24.dp).let { MaterialTheme.colorScheme.secondary })

        Text(
            "Objetivo del Proyecto",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Una aplicación diseñada especialmente para personas mayores. Ofrece un acceso offline, sencillo y directo a la música sin necesidad de configuraciones complejas ni conexiones de red inestables.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text("Sobre el Desarrollador", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
        Text(
            "Desarrollado por Joel Garcia.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        Text(
            "Music by AtlasAudio, Leberch, ",
            textAlign = TextAlign.Center, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
        )
        Text(
            "The_Mountain obtenida de: ",
            textAlign = TextAlign.Center, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Pixabay.com",
            textAlign = TextAlign.Center, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
        )

    }
}