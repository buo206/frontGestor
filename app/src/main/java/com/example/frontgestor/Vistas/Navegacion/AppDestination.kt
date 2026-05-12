package com.example.frontgestor.Vistas.Navegacion

sealed class AppDestination(val route: String) {
    data object Logueo : AppDestination("logueo")


    //vista empresa
    data object MenuMainE : AppDestination("menumaine")

    data object ListaTrabajadores : AppDestination("listatrabajadores")

    data object DetalleTrabajador : AppDestination("detalletrabajador")

    data object FormularioTrabajador : AppDestination("formulariotrabajador")

    data object ListaMateriales : AppDestination("listamateriales")

    data object FormularioMaterial : AppDestination("formulariomaterial")

    data object ListaRegistroMateriales : AppDestination("listaregistromateriales")

    data object ListaTrabajos : AppDestination("listatrabajos")

    data object FormularioTrabajo : AppDestination("formulariotrabajo")

    data object ListaRegistroTrabajador : AppDestination("listaRegistrotrabajador")


    data object FormularioRegistroTrabajo : AppDestination("formularioregistrotrabajo")


    data object FormularioRegistroMateriales : AppDestination("formularioregistromateriales")


    //vista trabajador
    data object MenuTrabajador : AppDestination("menutrabajador")

}