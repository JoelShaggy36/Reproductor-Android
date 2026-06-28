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
                // Imagen de portada almacenada en drawable
                R.drawable.portada1,
                // Archivo de audio almacenado en raw
                R.raw.afro
            ),

            Cancion(2, "Carnaval", "Cubanas",  R.drawable.portada2, R.raw.carnaval),
            Cancion(3, "Energy", "Electronica", R.drawable.portada3, R.raw.energy),
            Cancion(4,"Instrumental Abstract", "Instrumental", R.drawable.portada4, R.raw.instrumental_abstract_mountain),
            Cancion(5, "Instrumental Atlas", "Relax", R.drawable.portada5, R.raw.instrumental_atlas_audio),
            Cancion(6, "Instrumental Leberch", "Instrumental", R.drawable.portada6, R.raw.instrumental_leberch),
            Cancion(7, "Instrumental Piano Leberch", "Piano", R.drawable.portada7, R.raw.instrumental_piano_leberch),
        )
    }
}