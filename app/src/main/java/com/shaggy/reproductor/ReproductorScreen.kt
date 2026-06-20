package com.shaggy.reproductor

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReproductorScreen(cancion: Cancion, isPlaying: Boolean, onTogglePlay: () -> Unit, progreso: Float, onProgresoChange: (Float) -> Unit){
    var volume by remember { mutableStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6200EE), Color(0xFF121212))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { Destino.TODAS_LAS_CANCIONES  }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Cerrar", tint = Color.White)
            }
            Text("REPRODUCIENDO AHORA", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            IconButton(onClick = { /* Abrir menú */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.White)
            }
        }

        // Carátula del álbum
        Card(
            modifier = Modifier
                .size(290.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Image(
                painter = painterResource(id = cancion.portada),
                contentDescription = "Carátula",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Título y Autor
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(cancion.titulo, color = Color.White, fontSize = 24.sp, style = MaterialTheme.typography.titleLarge)
            Text(cancion.genero, color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
        }

        // Barra de progreso
        Column {
            Slider(
                value = progreso,
                onValueChange = { onProgresoChange(it) },
                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.Green)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("0:00", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Text(cancion.duracion, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }

        // Controles de Reproducción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Anterior */ }) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            FloatingActionButton(
                onClick = { onTogglePlay() },
                containerColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Reproducir",
                    tint = Color.Black
                )
            }
            IconButton(onClick = { /* Siguiente */ }) {
                Icon(Icons.Default.SkipNext, contentDescription = "Siguiente", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }

        // Control de Volumen
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Icon(Icons.Default.VolumeDown, contentDescription = "Bajar volumen", tint = Color.White)
            Slider(
                value = volume,
                onValueChange = { volume = it },
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White.copy(alpha = 0.5f))
            )
            Icon(Icons.Default.VolumeUp, contentDescription = "Subir volumen", tint = Color.White)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReproductorPreview() {
    ReproductorScreen(cancion = DatosMusica().obtenerCanciones()[1],false, {}, 0.0f, {})
}