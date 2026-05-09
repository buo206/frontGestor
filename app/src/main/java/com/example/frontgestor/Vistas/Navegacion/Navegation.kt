package com.example.frontgestor.Vistas.Navegacion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Api.LoginViewModel
import com.example.frontgestor.SessionManager
import com.example.frontgestor.Vistas.Empresa.DetalleTrabajador
import com.example.frontgestor.Vistas.Empresa.FormularioMaterial
import com.example.frontgestor.Vistas.Empresa.FormularioTrabajador
import com.example.frontgestor.Vistas.Empresa.FormularioTrabajo
import com.example.frontgestor.Vistas.Empresa.ListaMateriales
import com.example.frontgestor.Vistas.Empresa.ListaRegistroMateriales
import com.example.frontgestor.Vistas.Empresa.ListaTrabajadores
import com.example.frontgestor.Vistas.Empresa.ListaTrabajos
import com.example.frontgestor.Vistas.Empresa.MenuMainE
import com.example.frontgestor.Vistas.LoginScreen
import com.example.frontgestor.Vistas.Trabajador.MenuTrabajador


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navegation(modifier : Modifier = Modifier , sesion : SessionManager){
    val navController = rememberNavController()
    val loginviewModel: LoginViewModel = viewModel()
    val empresaViewModel: EmpresaViewModel = viewModel()
    val rutaPrimera = if (sesion.isLogged()) {
            if(sesion.getTipo().equals("empresa")){
               AppDestination.MenuMainE.route
            }else{
                AppDestination.MenuTrabajador.route
            }
        } else {
            AppDestination.Logueo.route
        }


    NavHost(
        navController = navController,
        startDestination = rutaPrimera,
        modifier = modifier
    ){
        composable(route = AppDestination.Logueo.route){
            LoginScreen(modifier = modifier,
                loginviewModel ,
                {

                } ,
                onNavigateToMenu = {
                    if(sesion.getTipo().equals("empresa")){
                        navController.navigate(AppDestination.MenuMainE.route)
                    }else{
                        navController.navigate(AppDestination.MenuTrabajador.route)
                    }
                }
            )
        }


        //empresa

        composable(route = AppDestination.MenuMainE.route) {
            MenuMainE(modifier ,
                {
                    navController.navigate(AppDestination.ListaTrabajadores.route)
                } ,
                sesion ,
                empresaViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.Logueo.route)
                } ,
                {
                    navController.navigate(AppDestination.ListaMateriales.route)
                } ,
                {
                    navController.navigate(AppDestination.ListaTrabajos.route)
                }
            )
        }

        composable(route = AppDestination.ListaTrabajadores.route) {
            ListaTrabajadores(modifier ,
                sesion ,
                empresaViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.MenuMainE.route)
                },
                {
                    navController.navigate(AppDestination.DetalleTrabajador.route)
                } ,
                {
                    navController.navigate(AppDestination.FormularioTrabajador.route + "/false")
                } ,
                {
                    navController.navigate(AppDestination.ListaMateriales.route)
                } ,
                {
                    navController.navigate(AppDestination.ListaTrabajos.route)
                }
            )
        }

        composable(route = AppDestination.DetalleTrabajador.route){
            DetalleTrabajador(modifier ,
                empresaViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.ListaTrabajadores.route)
                } ,
                {
                    navController.navigate(AppDestination.FormularioTrabajador.route + "/true")
                }
            )
        }

        composable(
            route = AppDestination.FormularioTrabajador.route + "/{esEdicion}",
            arguments = listOf(
                navArgument("esEdicion") { type = NavType.BoolType }
            )
        ){ backStackEntry ->
            val esEdicion = backStackEntry.arguments?.getBoolean("esEdicion") ?: true
            FormularioTrabajador(modifier, empresaViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion ,
                esEdicion
            )
        }


        composable(route = AppDestination.ListaMateriales.route){
            ListaMateriales(modifier ,
                sesion ,
                empresaViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.MenuMainE.route)
                } ,
                {
                    navController.navigate(AppDestination.FormularioMaterial.route + "/true")
                } ,
                {
                    navController.navigate(AppDestination.FormularioMaterial.route + "/false")
                } ,
                {
                    navController.navigate(AppDestination.ListaRegistroMateriales.route)
                }
            )
        }

        composable(
            route = AppDestination.FormularioMaterial.route + "/{esEdicion}",
            arguments = listOf(
                navArgument("esEdicion") { type = NavType.BoolType }
            )
        ){ backStackEntry ->
            val esEdicion = backStackEntry.arguments?.getBoolean("esEdicion") ?: true
            FormularioMaterial(modifier, empresaViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion ,
                esEdicion
            )
        }

        composable(route = AppDestination.ListaRegistroMateriales.route){
            ListaRegistroMateriales(modifier ,
                sesion ,
                empresaViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.MenuMainE.route)
                },
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.ListaMateriales.route)
                }
            )
        }


        composable(route = AppDestination.ListaTrabajos.route){
            ListaTrabajos(modifier ,
                sesion ,
                empresaViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.MenuMainE.route)
                },
                {
                    navController.navigate(AppDestination.FormularioTrabajo.route + "/true")
                },
                {},
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.ListaMateriales.route)
                },
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.ListaTrabajadores.route)
                }
            )
        }

        composable(
            route = AppDestination.FormularioTrabajo.route + "/{esEdicion}",
            arguments = listOf(
                navArgument("esEdicion") { type = NavType.BoolType }
            )
        ){ backStackEntry ->
            val esEdicion = backStackEntry.arguments?.getBoolean("esEdicion") ?: true
            FormularioTrabajo(modifier, empresaViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion ,
                esEdicion
            )
        }

        //trabajador

        composable(route = AppDestination.MenuTrabajador.route){
            MenuTrabajador(modifier)
        }
    }
}