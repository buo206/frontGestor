package com.example.frontgestor.Modelos

data class RegistroTrabajoDTO(
    val idTrabajo : Int? ,
    val tituloTrabajo : String ,
    val idTrabajador : Int ,
    val nombreTrabajador : String ,
    val apellidosTrabajador : String ,
    val rol : String ,
)
