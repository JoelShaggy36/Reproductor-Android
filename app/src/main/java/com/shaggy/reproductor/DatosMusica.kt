package com.shaggy.reproductor

// Clase encargada de almacenar y proporcionar los datos de música
class DatosMusica {

    // Función que devuelve una lista de objetos Cancion
    fun obtenerCanciones(): List<Cancion> {

        // Retorna una lista con varias canciones
        return listOf(

            // Primera canción
            Cancion(
                // Identificador único de la canción
                1,
                // Título de la canción
                "Afro",
                // Género musical
                "Electronica",
                // Duración de la canción
                "1:20",
                // Imagen de portada almacenada en drawable
                R.drawable.portada1,
                // Archivo de audio almacenado en raw
                R.raw.afro
            ),

            // Segunda canción
            Cancion(2, "Carnaval", "Cubanas", "1:25", R.drawable.portada2, R.raw.carnaval),

            // Tercera canción
            Cancion(3, "Energy", "Electronica", "2:00", R.drawable.portada3, R.raw.energy)
        )
    }
}