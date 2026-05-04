package com.example.frontgestor.Modelos

data class EmpresaDTO(
    val idEmpresa : Int ,
    val email : String ,
    val password : String ,
    val nombre: String,
    val apellidos : String ,
    val direccion : String ,
    val fechaCreacion : String
)
