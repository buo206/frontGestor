package com.example.frontgestor.Modelos

data class TrabajadorDTO(
    val idTrabajador: Int,
    val idEmpresa: Int ,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password : String ,
    val numeroTelefono: String?,
    val dni: String?,
    val dirreccion: String?,
    val fechaCreacion: String
)

