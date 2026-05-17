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
import com.example.frontgestor.Api.TrabajadorViewModel
import com.example.frontgestor.SessionManager
import com.example.frontgestor.Vistas.Empresa.DetalleTrabajador
import com.example.frontgestor.Vistas.Empresa.FormularioEmpresa
import com.example.frontgestor.Vistas.Empresa.FormularioMaterial
import com.example.frontgestor.Vistas.Empresa.FormularioRegistroMateriales
import com.example.frontgestor.Vistas.Empresa.FormularioRegistroTrabajo
import com.example.frontgestor.Vistas.Empresa.FormularioTrabajador
import com.example.frontgestor.Vistas.Empresa.FormularioTrabajo
import com.example.frontgestor.Vistas.Empresa.ListaMateriales
import com.example.frontgestor.Vistas.Empresa.ListaRegistroMateriales
import com.example.frontgestor.Vistas.Empresa.ListaRegistroTrabajador
import com.example.frontgestor.Vistas.Empresa.ListaTrabajadores
import com.example.frontgestor.Vistas.Empresa.ListaTrabajos
import com.example.frontgestor.Vistas.Empresa.MenuMainE
import com.example.frontgestor.Vistas.LoginScreen
import com.example.frontgestor.Vistas.Trabajador.FormularioEditarTrabajador
import com.example.frontgestor.Vistas.Trabajador.FormularioRegistroMaterialesTrabajo
import com.example.frontgestor.Vistas.Trabajador.FormularioTrabajoTrabajador
import com.example.frontgestor.Vistas.Trabajador.ListaTrabajosTrabajador
import com.example.frontgestor.Vistas.Trabajador.MenuTrabajador


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navegation(modifier : Modifier = Modifier , sesion : SessionManager){
    val navController = rememberNavController()
    val loginviewModel: LoginViewModel = viewModel()
    val empresaViewModel: EmpresaViewModel = viewModel()
    val trabajadorViewModel: TrabajadorViewModel = viewModel()
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


        //vistaempresa

        composable(route = AppDestination.MenuMainE.route) {
            MenuMainE(modifier ,
                {
                    navController.navigate(AppDestination.ListaTrabajadores.route)
                } ,{
                    navController.navigate(AppDestination.FormularioEmpresa.route)
                },
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

        composable(
            route = AppDestination.FormularioEmpresa.route,
        ){ backStackEntry ->
            FormularioEmpresa(modifier, empresaViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion ,
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
                },{
                    navController.navigate(AppDestination.ListaRegistroTrabajador.route)
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
                {
                    navController.navigate(AppDestination.FormularioTrabajo.route + "/false")
                },
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

        composable(route = AppDestination.ListaRegistroTrabajador.route){
            ListaRegistroTrabajador(modifier ,
                empresaViewModel ,
                {
                    navController.popBackStack()
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
                esEdicion ,
                {
                    navController.navigate(AppDestination.FormularioRegistroTrabajo.route + "/true")
                },
                {
                    navController.navigate(AppDestination.FormularioRegistroTrabajo.route + "/false")
                },{
                    navController.navigate(AppDestination.FormularioRegistroMateriales.route + "/true")
                },{
                    navController.navigate(AppDestination.FormularioRegistroMateriales.route + "/false")
                }
            )
        }


        composable(
            route = AppDestination.FormularioRegistroTrabajo.route + "/{esEdicion}",
            arguments = listOf(
                navArgument("esEdicion") { type = NavType.BoolType }
            )
        ){ backStackEntry ->
            val esEdicion = backStackEntry.arguments?.getBoolean("esEdicion") ?: true
            FormularioRegistroTrabajo(modifier, empresaViewModel ,
                {3
                    navController.popBackStack()
                } ,
                sesion ,
                esEdicion
            )
        }


        composable(
            route = AppDestination.FormularioRegistroMateriales.route + "/{esEdicion}",
            arguments = listOf(
                navArgument("esEdicion") { type = NavType.BoolType }
            )
        ){ backStackEntry ->
            val esEdicion = backStackEntry.arguments?.getBoolean("esEdicion") ?: true
            FormularioRegistroMateriales(modifier, empresaViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion ,
                esEdicion
            )
        }

        //vista trabajador

        composable(route = AppDestination.MenuTrabajador.route){
            MenuTrabajador(modifier ,
                sesion ,
                trabajadorViewModel ,
                {
                    navController.popBackStack()
                    navController.navigate(AppDestination.Logueo.route)
                } ,
                {
                    navController.navigate(AppDestination.ListaTrabajosTrabajador.route)
                } ,
                {
                    navController.navigate(AppDestination.FormularioEditarTrabajador.route)
                }
            )
        }

        composable(route = AppDestination.ListaTrabajosTrabajador.route){
            ListaTrabajosTrabajador(modifier ,
                sesion ,
                trabajadorViewModel ,
                {
                    navController.popBackStack()
                } ,
                {
                    navController.navigate(AppDestination.FormularioTrabajoTrabajador.route)
                }
            )
        }


        composable(route = AppDestination.FormularioTrabajoTrabajador.route){
            FormularioTrabajoTrabajador(modifier ,
                trabajadorViewModel ,
                {
                    navController.popBackStack()
                } ,
                {
                    navController.navigate(AppDestination.FormularioRegistroMaterialesTrabajo.route + "/true")
                },{
                    navController.navigate(AppDestination.FormularioRegistroMaterialesTrabajo.route + "/false")
                }
            )
        }


        composable(
            route = AppDestination.FormularioRegistroMaterialesTrabajo.route + "/{esEdicion}",
            arguments = listOf(
                navArgument("esEdicion") { type = NavType.BoolType }
            )
        ){ backStackEntry ->
            val esEdicion = backStackEntry.arguments?.getBoolean("esEdicion") ?: true
            FormularioRegistroMaterialesTrabajo(modifier, trabajadorViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion ,
                esEdicion
            )
        }


        composable(route = AppDestination.FormularioEditarTrabajador.route){
            FormularioEditarTrabajador(modifier ,
                trabajadorViewModel ,
                {
                    navController.popBackStack()
                } ,
                sesion
            )
        }
    }
}