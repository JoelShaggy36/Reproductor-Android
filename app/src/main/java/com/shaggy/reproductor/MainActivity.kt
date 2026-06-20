package com.shaggy.reproductor

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaggy.reproductor.ui.theme.ReproductorTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReproductorTheme {
                ReproductorApp()
            }
        }
    }
}
// Permite utilizar componentes experimentales de Material Design 3
@OptIn(ExperimentalMaterial3Api::class)

// Indica que esta función dibuja una interfaz gráfica con Jetpack Compose
@Composable
fun ReproductorApp(){
    //Inicializacion de Objeto mediaPlayer
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.afro) }
    //creacion de estado para verificar si la musica se esta reproduciendo
    var musicaSonando by remember { mutableStateOf(false) }
    //Creacion de estado para barra de progreso reactiva
    var progresoAudio by remember { mutableFloatStateOf(0.0f) }
    //Creacion de hilo para avisar a compose el estado de reproduccion de audio para dibujarlo en pantalla
    LaunchedEffect(musicaSonando) {
        while (musicaSonando == true){
            progresoAudio = mediaPlayer.currentPosition.toFloat()/mediaPlayer.duration.toFloat()
            delay(500)
        }
    }



    // Variable que guarda la pantalla actual que se está mostrando.
    // rememberSaveable conserva el valor aunque la pantalla se recree.
    // La aplicación inicia mostrando "TODAS_LAS_CANCIONES".
    var pantallaActual by rememberSaveable {
        mutableStateOf(Destino.REPRODUCTOR)
    }

    // Scaffold es una estructura base que organiza la pantalla
    // permitiendo usar barra superior, barra inferior y botón flotante.
    Scaffold(

        // Barra superior de la aplicación
        topBar = {
            //Barra superior centrada
            CenterAlignedTopAppBar(

                // Configuración de colores de la barra superior
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                // Título mostrado en la barra superior
                title = {
                    Text("Reproductor de Musica")
                },

                // Botones que aparecen del lado derecho de la barra
                actions = {
                    IconButton(onClick = {
                        // Aquí se puede agregar alguna acción para el menú
                    }) {

                        // Icono de menú
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },

        // Barra inferior de navegación
        bottomBar = {
            BottomAppBar(

                // Colores de la barra inferior
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {

                // Fila que contiene los botones de navegación
                Row(

                    // Ocupa todo el ancho disponible
                    modifier = Modifier
                        .fillMaxWidth(),

                    // Centra los elementos verticalmente
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ){

                        // Primer botón de navegación
                        Image(

                            // Carga la imagen desde los recursos del proyecto
                            painter = painterResource(id = R.drawable.icon1),

                            // Descripción para accesibilidad
                            contentDescription = "Imagen",

                            // Modificadores visuales y de comportamiento
                            modifier = Modifier

                                // Espacio alrededor de la imagen
                                .padding(20.dp)

                                // Tamaño de la imagen
                                .size(80.dp)

                                // Convierte la imagen en un círculo
                                .clip(CircleShape)


                                // Acción al hacer clic
                                .clickable(
                                    onClick = {
                                        pantallaActual =
                                            Destino.TODAS_LAS_CANCIONES
                                    }
                                )
                        )

                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ){
                        // Segundo botón de navegación
                        Image(
                            painter = painterResource(id = R.drawable.icon2),
                            contentDescription = "Imagen",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(80.dp)

                                // Cambia a la pantalla del reproductor
                                .clickable(
                                    onClick = {
                                        pantallaActual =
                                            Destino.REPRODUCTOR
                                    }
                                )
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ){

                        // Tercer botón de navegación
                        Image(
                            painter = painterResource(id = R.drawable.icon3),
                            contentDescription = "Imagen",
                            modifier = Modifier
                                .padding(20.dp)
                                .size(80.dp)
                                .clip(CircleShape)

                                // Cambia a la pantalla de playlists
                                .clickable(
                                    onClick = {
                                        pantallaActual =
                                            Destino.PLAYLIST
                                    }
                                )
                        )
                    }

                }
            }
        },
    ) { innerPadding ->

        // Contenedor principal del contenido de la pantalla
        Column(

            // Aplica el espacio necesario para no quedar debajo
            // de la barra superior o inferior
            modifier = Modifier.padding(innerPadding)

        ){

            // Dependiendo del valor de pantallaActual,
            // se muestra una pantalla diferente.
            when (pantallaActual){

                // Muestra la lista de canciones
                Destino.TODAS_LAS_CANCIONES ->
                    CancionesScreen(
                        listaCanciones =
                            DatosMusica().obtenerCanciones()
                    )

                // Muestra texto de canciones favoritas
                Destino.CANCIONES_FAVORITAS ->
                    Text("PANTALLA DE CANCIONES FAVORITAS")

                // Muestra texto del reproductor
                Destino.REPRODUCTOR ->
                    ReproductorScreen(cancion = DatosMusica().obtenerCanciones()[1], musicaSonando, {if (mediaPlayer.isPlaying == true) mediaPlayer.pause() else mediaPlayer.start()},progresoAudio, {})

                // Muestra texto de playlists
                Destino.PLAYLIST ->
                    Text("PANTALLA DE PLAYLIST")
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ReproductorTheme {
        ReproductorApp()
    }
}