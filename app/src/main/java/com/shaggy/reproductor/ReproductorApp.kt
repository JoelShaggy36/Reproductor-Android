package com.shaggy.reproductor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReproductorApp(viewModel: ReproductorViewModel) {
    // Recolección de los flujos de forma reactiva en la vista raíz
    val musicaSonando by viewModel.musicaSonando.collectAsStateWithLifecycle()
    val progresoAudio by viewModel.progresoAudio.collectAsStateWithLifecycle()
    val indiceActual by viewModel.indiceCancionActual.collectAsStateWithLifecycle()
    val pantallaActual by viewModel.pantallaActual.collectAsStateWithLifecycle()
    val volumenAudio by viewModel.volumenAudio.collectAsStateWithLifecycle()
    val tiempoActual by viewModel.tiempoActualTexto.collectAsStateWithLifecycle()
    val tiempoTotal by viewModel.tiempoTotalTexto.collectAsStateWithLifecycle()

    var menuAbierto by remember { mutableStateOf(false) }
    val lista = DatosMusica().obtenerCanciones()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Reproductor de Música") },
                actions = {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                    // El menú desplegable que aparecerá al presionar el ícono
                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Lista de Canciones") },
                            onClick = {
                                menuAbierto = false
                                viewModel.cambiarPantalla(Destino.TODAS_LAS_CANCIONES)
                            }
                        )
                        Divider() // Una pequeña línea separadora
                        DropdownMenuItem(
                            text = { Text("Reproductor") },
                            onClick = {
                                menuAbierto = false
                                viewModel.cambiarPantalla(Destino.REPRODUCTOR)
                            }
                        )
                        Divider() // Una pequeña línea separadora
                        DropdownMenuItem(
                            text = { Text("Acerca de") },
                            onClick = {
                                menuAbierto = false
                                viewModel.cambiarPantalla(Destino.ACERCA_DE)
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        Image(
                            painter = painterResource(id = R.drawable.icon1),
                            contentDescription = "Todas las canciones",
                            modifier = Modifier
                                .padding(20.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.cambiarPantalla(Destino.TODAS_LAS_CANCIONES) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.icon2),
                            contentDescription = "Reproductor",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(80.dp)
                                .clickable { viewModel.cambiarPantalla(Destino.REPRODUCTOR) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                        Image(
                            painter = painterResource(id = R.drawable.icon3),
                            contentDescription = "Acerca de",
                            modifier = Modifier
                                .padding(20.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.cambiarPantalla(Destino.ACERCA_DE) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplicar el espacio seguro de las barras
        ) {
            when (pantallaActual) {
                Destino.TODAS_LAS_CANCIONES -> {
                    CancionesScreen(
                        listaCanciones = lista,
                        onCancionClick = { indiceSeleccionado ->
                            viewModel.seleccionarCancion(indiceSeleccionado)
                        }
                    )
                }

                Destino.ACERCA_DE -> {
                    AcercaDeScreen()
                }
                // Muestra el reproductor
                Destino.REPRODUCTOR -> {
                    //Creación de contenedor para animación al cerrar el reproductor
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(initialOffsetY = { it }), // Desplaza de abajo hacia arriba al entrar
                        exit = slideOutVertically(targetOffsetY = { it })   // Desplaza de arriba hacia abajo al salir
                    ) {
                        val lista = DatosMusica().obtenerCanciones()
                        val cancionActual = lista[indiceActual]
                        ReproductorScreen(
                                    // Obtenemos la canción
                            cancion = cancionActual,
                                    //verificar con verdadero o falso si se está reproduciendo
                            isPlaying = musicaSonando,
                                    //Obtenemos el progreso
                            progreso = progresoAudio,
                                    //Obtenemos el Tipo que lleva reproduciéndose
                            tiempoActual = tiempoActual,
                                    //Obtenemos el tiempo que dura la cancion
                            tiempoTotal = tiempoTotal,
                                    //Play-Pause para implementar el Hilo y el botón
                            onTogglePlay = {viewModel.togglePlay()},
                                    //Hilo de barra de reproducción (para progreso)
                            onProgresoChange = {nuevoProgreso -> viewModel.actualizarProgreso(nuevoProgreso)},
                            onAnteriorClick = {viewModel.cancionAnterior()},
                            onSiguienteClick = {viewModel.cancionSiguiente()},
                                    //Obtenemos el volumen del dispositivo
                            nuevoVolumen = volumenAudio,
                                    //Hilo para barra de volumen
                            onVolumenChange = {nuevoVolumen -> viewModel.cambiarVolumen(nuevoVolumen)},
                                    //Cambio de ui al cerrar el reproductor
                            onCerrarClick = {viewModel.cambiarPantalla(Destino.TODAS_LAS_CANCIONES)}
                        )
                    }
                }

            }
        }
    }}
