package com.example.frontgestor.Modelos

data class TrabajoDTO(
    val idTrabajo : Int ,
    val idEmpresa: Int ,
    val titulo : String ,
    val descripcion : String ,
    val fechaInicio : String ,
    val fechaFinal : String ,
    val anotacion : String ,
    val estado : String ,
)
