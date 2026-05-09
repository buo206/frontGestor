package com.example.frontgestor.Modelos

data class RegistroMaterialDTO(
    val idRegistro : Int ,
    val idTrabajo : Int ,
    val tituloTrabajo : String ,
    val idMaterial : Int ,
    val tituloMaterial : String ,
    val idTrabajador : Int ,
    val nombreTrabajador :String ,
    val fecha : String ,
    val cantidad : Int
)
