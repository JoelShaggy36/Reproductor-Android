package com.shaggy.reproductor

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration.Companion.milliseconds


// 1. Heredamos de AndroidViewModel pasándole la "application" para tener Contexto seguro
class ReproductorViewModel(application: Application) : AndroidViewModel(application) {

    // Obtener el contexto del sistema de manera segura para la memoria
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    // 2. Inicializamos el AudioManager y MediaPlayer de forma nativa (sin remember)
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    var mediaPlayer = MediaPlayer.create(context, R.raw.afro)

    // 3. Tus variables de estado mutables (Privadas para que nadie las modifique fuera del ViewModel)
    private val _musicaSonando = MutableStateFlow(false)
    val musicaSonando = _musicaSonando.asStateFlow() // Esta es la que lee tu MainActivity

    private val _progresoAudio = MutableStateFlow(0.0f)
    val progresoAudio = _progresoAudio.asStateFlow()

    val volumenMaximoInicial = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    val volumenActualInicial = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()

    //Creacion de variable reactiva para barra de volumen
    var volumenAudio by remember {
        mutableFloatStateOf(volumenActualInicial / volumenMaximoInicial)}

    //Variable reactiva para índice de canción seleccionada
    var indiceCancionActual by rememberSaveable {
        mutableIntStateOf(0) }

    // Variable que guarda la pantalla actual que se está mostrando.
    // rememberSavable conserva el valor aunque la pantalla se recree.
    // La aplicación inicia mostrando "TODAS_LAS_CANCIONES".
    var pantallaActual by rememberSaveable {
        mutableStateOf(Destino.REPRODUCTOR)
    }


    // Este bloque vigila el índice de la canción actual
    LaunchedEffect(indiceCancionActual) {
        // 1. Si ya estaba sonando algo, lo detenemos y liberamos para no saturar la memoria
        mediaPlayer.stop()
        mediaPlayer.release()

        // 2. Traemos la lista de canciones para saber cuál toca ahora
        val lista = DatosMusica().obtenerCanciones()
        val cancionNueva = lista[indiceCancionActual]

        // 3. Creamos un nuevo reproductor cargando el archivo de audio dinámico de la nueva canción
        mediaPlayer = MediaPlayer.create(context, cancionNueva.audio)

        //Autoplay al terminar de reproducir una cancion
        mediaPlayer.setOnCompletionListener {
            // Cuando la canción termina, ejecutamos exactamente la misma lógica del botón "Siguiente"
            if (indiceCancionActual < lista.size - 1) {
                indiceCancionActual++
            } else {
                indiceCancionActual = 0 // Si era la última, regresa a la primera
            }
        }

        // 4. Si la app estaba en modo "reproduciendo", hacemos que la nueva canción arranque de inmediato
        if (musicaSonando) {
            mediaPlayer.start()
        }
    }

    // Hilo de fondo que se ejecuta mientras el reproductor esté visible
    LaunchedEffect(pantallaActual) {
        // Si el usuario está viendo el reproductor, empezamos a monitorear el hardware
        while (pantallaActual == Destino.REPRODUCTOR) {
            val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            val actual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()

            // Si el usuario movió los botones físicos, actualizamos nuestra barra verde
            val porcentajeReal = actual / max
            if (volumenAudio != porcentajeReal) {
                volumenAudio = porcentajeReal
            }

            delay(100.milliseconds)
        }
    }

    //Creación de hilo para avisar a compose el estado de reproducción de audio para dibujarlo en pantalla
    LaunchedEffect(musicaSonando) {
        while (musicaSonando){
            if (mediaPlayer.duration > 0) { // Evitamos dividir entre cero si aún no carga el audio
                // Calculamos la fracción (Posición Actual / Duración Total)
                progresoAudio = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
            }
            delay(100.milliseconds)
        }
    }


}

@SuppressLint("DefaultLocale")
fun formatearTiempo(milisegundos: Int): String {
    val minutos = milisegundos / 60000
    val segundos = (milisegundos % 60000) / 1000
    // Esto asegura que si los segundos son menores a 10, pinte "1:05" y no "1:5"
    return String.format("%d:%02d", minutos, segundos)
}
