package com.example.frontgestor.Modelos

data class EmpresaDTO(
    val id_Empresa : Int ,
    val email : String ,
    val password : String ,
    val nombre: String,
    val apellidos : String ,
    val dirreccion : String ,
    val fecha_Creacion : String
)
