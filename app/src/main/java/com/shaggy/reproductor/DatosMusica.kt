package com.shaggy.reproductor

class DatosMusica {
    fun obtenerCanciones(): List<Cancion> {
        return listOf(
            Cancion(1, "Afro", "Electronica", "1:20", R.drawable.portada1, R.raw.afro),
            Cancion(2, "Carnaval", "Cubanas", "1:25", R.drawable.portada2, R.raw.carnaval),
            Cancion(3, "Energy", "Electronica", "2:00", R.drawable.portada3, R.raw.energy)
            )
    }
}