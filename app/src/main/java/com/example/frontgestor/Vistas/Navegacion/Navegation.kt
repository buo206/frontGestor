package com.example.frontgestor.Vistas.Navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Api.LoginViewModel
import com.example.frontgestor.SessionManager
import com.example.frontgestor.Vistas.Empresa.MenuMainE
import com.example.frontgestor.Vistas.LoginScreen
import com.example.frontgestor.Vistas.Trabajador.MenuTrabajador


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
                {} ,
                sesion ,
                empresaViewModel ,
                {
                    navController.popBackStack()

                }
            )
        }

        composable(route = AppDestination.ListaTrabajadores.route) {
            // vista de ListaTrabajadores()
        }

        //trabajador

        composable(route = AppDestination.MenuTrabajador.route){
            MenuTrabajador(modifier)
        }
    }
}