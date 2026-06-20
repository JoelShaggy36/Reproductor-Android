package com.shaggy.reproductor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Función que muestra una lista de canciones en pantalla
@Composable
fun CancionesScreen(listaCanciones: List<Cancion>) {

    // LazyColumn crea una lista vertical desplazable.
    // Solo carga los elementos visibles para mejorar el rendimiento.
    LazyColumn {

        // Recorre cada canción de la lista recibida
        items(listaCanciones) { cancion ->

            // Por cada canción llama a CancionItem
            // para dibujar su información en pantalla
            CancionItem(cancion)
        }
    }
}


// Función que dibuja una canción individual
@Composable
fun CancionItem(cancion: Cancion){

    // Row organiza los elementos horizontalmente
    Row(

        // Configuración visual de la fila
        modifier = Modifier

            // Ocupa todo el ancho disponible
            .fillMaxWidth()

            // Agrega espacio alrededor de la fila
            .padding(4.dp),

        // Centra verticalmente los elementos dentro de la fila
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Muestra la imagen de portada de la canción
        Image(

            // Obtiene la imagen almacenada en el recurso indicado
            // por cancion.portada
            painter = painterResource(id = cancion.portada),

            // Descripción para accesibilidad
            contentDescription = "Imagen",

            // Modificadores visuales
            modifier = Modifier

                // Espacio alrededor de la imagen
                .padding(10.dp)

                // Tamaño de la imagen
                .size(70.dp)

                // Convierte la imagen en forma circular
                .clip(CircleShape)
        )

        // Columna que contendrá el título y el género
        Column(

            // Ocupa el espacio sobrante de la fila
            modifier = Modifier.weight(1f),

            ){

            // Muestra el título de la canción
            Text(

                // Texto en negrita
                fontWeight = FontWeight.Bold,

                // Título obtenido del objeto canción
                text = cancion.titulo
            )

            // Muestra el género musical
            Text(

                // Texto con grosor más ligero
                fontWeight = FontWeight.Light,

                // Género obtenido del objeto canción
                text = cancion.genero
            )
        }

        // Botón con icono de opciones
        IconButton(

            // Acción que se ejecutará al presionarlo
            // Actualmente está vacío
            onClick = {}

        ) {

            // Icono de tres puntos verticales
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Más"
            )
        }
    }
}


// Vista previa para Android Studio
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CancionesScreenPreview() {

    // Muestra una vista previa usando datos de ejemplo
    CancionesScreen(
        listaCanciones = DatosMusica().obtenerCanciones()
    )
}