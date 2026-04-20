package com.example.frontgestor.Vistas.Navegacion

sealed class AppDestination(val route: String) {
    data object Logueo : AppDestination("logueo")

    data object MenuMainE : AppDestination("menumaine")

    data object ListaTrabajadores : AppDestination("listatrabajadores")
}