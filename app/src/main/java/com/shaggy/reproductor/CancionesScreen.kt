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

class CancionesScreen {
    @Composable
    fun Screen(listaCanciones: List<Cancion>) {
        LazyColumn {
            items(listaCanciones) {cancion ->
                CancionItem(cancion)
            }
        }
    }
}



@Composable
fun CancionItem(cancion: Cancion){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = cancion.portada),
            contentDescription = "Imagen",
            modifier = Modifier
                .padding(10.dp)
                .size(70.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.weight(1f),
        ){
            Text(
                fontWeight = FontWeight.Bold,
                text = cancion.titulo
            )
            Text(
                fontWeight = FontWeight.Light,
                text = cancion.genero
            )
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.MoreVert, contentDescription = "Más")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CancionesScreenPreview() {
    val listaCancionespantalla = CancionesScreen()
    listaCancionespantalla.Screen(listaCanciones = DatosMusica().obtenerCanciones())
}