package com.shaggy.reproductor

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shaggy.reproductor.ui.theme.ReproductorTheme
import com.shaggy.reproductor.ReproductorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Compose creará y retendrá el ViewModel de forma automática aquí
            val reproductorViewModel: ReproductorViewModel = viewModel<ReproductorViewModel>()

            ReproductorApp(viewModel = reproductorViewModel)
        }
    }
}

// Permite utilizar componentes experimentales de Material Design 3
@OptIn(ExperimentalMaterial3Api::class)

// Indica que esta función dibuja una interfaz gráfica con Jetpack Compose
@Composable
fun ReproductorApp(viewModel: ReproductorViewModel) {
    // 1. Usamos "by" y "collectAsStateWithLifecycle()" para escuchar el flujo en vivo
    val musicaSonando by viewModel.musicaSonando.collectAsStateWithLifecycle()
    val progresoAudio by viewModel.progresoAudio.collectAsStateWithLifecycle()


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
                                        viewModel.pantallaActual.value = Destino.TODAS_LAS_CANCIONES
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
                                        viewModel.pantallaActual.value = Destino.REPRODUCTOR
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
                                        viewModel.pantallaActual.value = Destino.PLAYLIST
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
            val pantallaActual by viewModel.pantallaActual.collectAsStateWithLifecycle()

            // Dependiendo del valor de pantallaActual,
            // se muestra una pantalla diferente.
            when (pantallaActual){

                // Muestra la lista de canciones
                Destino.TODAS_LAS_CANCIONES -> {
                    CancionesScreen(
                        listaCanciones = DatosMusica().obtenerCanciones(),
                        onCancionClick = { indiceSeleccionado ->
                            // 1. Actualizamos el índice global (esto activará de golpe el LaunchedEffect que carga el audio)
                            viewModel.indiceCancionActual.value = indiceSeleccionado

                            // 2. Nos aseguramos de encender el interruptor para que empiece a sonar de inmediato
                            viewModel.musicaSonando.value = true

                            // 3. Viajamos al reproductor para que se muestre en pantalla
                            viewModel.pantallaActual.value = Destino.REPRODUCTOR
                        }
                    )
                }

                // Muestra texto de canciones favoritas
                Destino.CANCIONES_FAVORITAS -> {
                Text("PANTALLA DE CANCIONES FAVORITAS")
            }
                // Muestra el reproductor
                Destino.REPRODUCTOR ->{
                //Creacion de contenedor para animacion al cerrar el reproductor
                AnimatedVisibility(
                    visible = (pantallaActual == Destino.REPRODUCTOR),
                    enter = slideInVertically(initialOffsetY = { it }), // Desplaza de abajo hacia arriba al entrar
                    exit = slideOutVertically(targetOffsetY = { it })   // Desplaza de arriba hacia abajo al salir
                ) {
                    val lista = DatosMusica().obtenerCanciones()
                    val cancionActual = lista[indiceCancionActual]
                    ReproductorScreen(
                        // Obtenemos la canción
                        cancion = cancionActual,
                        //verificar con verdadero o falso si se está reproduciendo
                        musicaSonando,
                        //Obtenemos el progreso
                        progresoAudio,
                        formatearTiempo(mediaPlayer.currentPosition),
                        formatearTiempo(mediaPlayer.duration),
                        //Play-Pause para implementar el Hilo y el botón
                        {
                            if (mediaPlayer.isPlaying) { //Si la función media player está reproduciendo algo
                                mediaPlayer.pause()        //Pausa la reproducción
                                musicaSonando = false
                            }  // y cambia el verificador a falso para actualizar el Hilo y botón gráfico
                            else {
                                mediaPlayer.start()   //si no media player reproducirá el contenido
                                musicaSonando = true
                            }  // el verificador será true para actualizar compose
                        },
                        { nuevoProgreso ->
                            // 1. Actualizamos la barra visualmente en Compose
                            viewModel.progresoAudio.value = nuevoProgreso
                            // 2. Calculamos los milisegundos reales
                            val milisegundosDestino = (nuevoProgreso * viewModel.mediaPlayer.duration).toInt()
                            // 3. Le ordenamos al reproductor físico que viaje a ese milisegundo
                            viewModel.mediaPlayer.seekTo(milisegundosDestino)
                        },
                        onAnteriorClick = {
                            if (viewModel.indiceCancionActual.value > 0) {
                                viewModel.indiceCancionActual.value-- // Si no es la primera, retrocede 1
                            } else {
                                viewModel.indiceCancionActual.value =
                                    lista.size - 1 // Si es la primera, salta a la última
                            }
                        },
                        onSiguienteClick = {
                            if (viewModel.indiceCancionActual.value < lista.size - 1) {
                                viewModel.indiceCancionActual.value++ // Si no es la última, avanza 1
                            } else {
                                viewModel.indiceCancionActual.value = 0 // Si es la última, regresa a la primera
                            }
                        },
                        viewModel.volumenAudio.collectAsState().value,

                        onVolumenChange = { nuevoVolumen ->
                            viewModel.volumenAudio.value = nuevoVolumen // Actualiza tu barra verde en la UI

                            // 1. Obtención del tope máximo del volumen de música del dispositivo (ej: 10)
                            val volumenMaximo =
                                viewModel.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                            // 2. Multiplicar el porcentaje (0.0 a 1.0) por el tope máximo para sacar el entero destino
                            val volumenDestinoEnEntero = (nuevoVolumen * volumenMaximo).toInt()

                            // 3. Le ordenamos al sistema operativo cambiar el volumen general de la música
                            viewModel.audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                volumenDestinoEnEntero,
                                0 // 0 para no mostrar nada y AudioManager.FLAG_SHOW_UI para mostrar la barra nativa del sistema.
                            )
                        },
                        onCerrarClick = { viewModel.pantallaActual.value = Destino.TODAS_LAS_CANCIONES }
                    )
                }
            }
                // Muestra texto de playlists
                Destino.PLAYLIST -> {
                Text("PANTALLA DE PLAYLIST")
            }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ReproductorTheme {

    }
}