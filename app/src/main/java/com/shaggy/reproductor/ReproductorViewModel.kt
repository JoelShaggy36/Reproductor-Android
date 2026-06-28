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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
    private var _musicaSonando = MutableStateFlow(false)
    var musicaSonando = _musicaSonando.asStateFlow() // Esta es la que lee tu MainActivity

   private val _progresoAudio = MutableStateFlow(0.0f)
    var progresoAudio = _progresoAudio.asStateFlow()

    val volumenMaximoInicial = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    val volumenActualInicial = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()

    //Creación de variable reactiva para barra de volumen
    private val _volumenAudio = MutableStateFlow(volumenActualInicial / volumenMaximoInicial)
    val volumenAudio = _volumenAudio.asStateFlow()

    //Variable reactiva para índice de canción seleccionada
    private val _indiceCancionActual = MutableStateFlow(0)
    val indiceCancionActual = _indiceCancionActual.asStateFlow()

    // Variable que guarda la pantalla actual que se está mostrando.
    // rememberSavable conserva el valor aunque la pantalla se recree.
    // La aplicación inicia mostrando "TODAS_LAS_CANCIONES".
    private var _pantallaActual = MutableStateFlow(Destino.TODAS_LAS_CANCIONES)
    var pantallaActual = _pantallaActual.asStateFlow()

    // Estados para los textos formateados de tiempo
    private val _tiempoActualTexto = MutableStateFlow("00:00")
    val tiempoActualTexto = _tiempoActualTexto.asStateFlow()

    private val _tiempoTotalTexto = MutableStateFlow("00:00")
    val tiempoTotalTexto = _tiempoTotalTexto.asStateFlow()


    init {
        // HILO 1: Cambiar de canción cuando cambie el índice
        viewModelScope.launch {
            _indiceCancionActual.collect { nuevoIndice ->
                mediaPlayer.stop()
                mediaPlayer.release()

                val lista = DatosMusica().obtenerCanciones()
                val cancionNueva = lista[nuevoIndice]
                mediaPlayer = MediaPlayer.create(context, cancionNueva.audio)

                //En cuanto cargue la canción, calculamos su duración total fija en texto
                _tiempoTotalTexto.value = formatearTiempo(mediaPlayer.duration)

                // Le avisamos al MediaPlayer que, en cuanto termine la pista actual, ejecute la siguiente
                mediaPlayer.setOnCompletionListener {
                    cancionSiguiente() // Llama a la función que creamos para avanzar el índice
                }

                if (_musicaSonando.value) {
                    mediaPlayer.start()
                }
            }
        }

        // HILO 2: Monitorear el volumen físico del teléfono
        viewModelScope.launch {
            while (true) {
                // Solo medimos el hardware si el usuario está metido en el Reproductor
                if (_pantallaActual.value == Destino.REPRODUCTOR) {
                    val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
                    val actual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
                    val porcentajeReal = actual / max

                    if (_volumenAudio.value != porcentajeReal) {
                        _volumenAudio.value = porcentajeReal
                    }
                }
                delay(100) // Revisa cada 100 milisegundos
            }
        }

        // HILO 3: Monitorear el progreso (barra verde) de la canción
        viewModelScope.launch {
            while (true) {
                if (_musicaSonando.value && mediaPlayer.duration > 0) {
                    val posicionActual = mediaPlayer.currentPosition

                    // 1. Seguimos actualizando el porcentaje del Slider (0.0f a 1.0f)
                    _progresoAudio.value = posicionActual.toFloat() / mediaPlayer.duration.toFloat()

                    // 2. Formateamos la posición actual a texto en tiempo real
                    _tiempoActualTexto.value = formatearTiempo(posicionActual)
                }
                delay(100)
            }
        }
    }

    fun togglePlay() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _musicaSonando.value = false // Aquí sí te dejará cambiarlo porque es el MutableStateFlow interno
        } else {
            mediaPlayer.start()
            _musicaSonando.value = true
        }
    }
    fun seleccionarCancion(indice: Int) {
        _indiceCancionActual.value = indice
        _musicaSonando.value = true
        _pantallaActual.value = Destino.REPRODUCTOR
    }
    fun actualizarProgreso(nuevoProgreso: Float) {
        _progresoAudio.value = nuevoProgreso

        // El ViewModel calcula los milisegundos de manera segura y le ordena al hardware viajar
        val milisegundosDestino = (nuevoProgreso * mediaPlayer.duration).toInt()
        mediaPlayer.seekTo(milisegundosDestino)
    }
    // Una sola función para controlar toda la navegación del reproductor
    fun cambiarPantalla(destino: Destino) {
        _pantallaActual.value = destino
    }
    fun cancionAnterior() {
        val lista = DatosMusica().obtenerCanciones()
        if (_indiceCancionActual.value > 0) {
            _indiceCancionActual.value--
        } else {
            _indiceCancionActual.value = lista.size - 1 // Salta a la última
        }
    }

    fun cancionSiguiente() {
        val lista = DatosMusica().obtenerCanciones()
        if (_indiceCancionActual.value < lista.size - 1) {
            _indiceCancionActual.value++
        } else {
            _indiceCancionActual.value = 0 // Regresa a la primera
        }
    }
    fun cambiarVolumen(nuevoPorcentaje: Float) {
        _volumenAudio.value = nuevoPorcentaje
        // Ajustamos el volumen real en el hardware del teléfono
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val destino = (nuevoPorcentaje * max).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, destino, 0)
    }



}


@SuppressLint("DefaultLocale")
fun formatearTiempo(milisegundos: Int): String {
    val minutos = milisegundos / 60000
    val segundos = (milisegundos % 60000) / 1000
    // Esto asegura que si los segundos son menores a 10, pinte "1:05" y no "1:5"
    return String.format("%d:%02d", minutos, segundos)
}

