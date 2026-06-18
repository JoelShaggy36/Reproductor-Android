package com.shaggy.reproductor

data class Cancion(
    var id: Int,
    var titulo: String,
    var genero: String,
    var duracion: String,
    var portada: Int,
    var audio: Int
)
