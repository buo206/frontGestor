package com.example.frontgestor.Vistas.Navegacion

sealed class AppDestination(val route: String) {
    data object Logueo : AppDestination("logueo")


    //vista empresa
    data object MenuMainE : AppDestination("menumaine")

    data object ListaTrabajadores : AppDestination("listatrabajadores")

    data object DetalleTrabajador : AppDestination("detalletrabajador")

    data object FormularioTrabajador : AppDestination("formulariotrabajador")

    data object ListaMateriales : AppDestination("listamateriales")


    //vista trabajador
    data object MenuTrabajador : AppDestination("menutrabajador")

}