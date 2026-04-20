package com.example.frontgestor.Vistas.Navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontgestor.Api.LoginViewModel
import com.example.frontgestor.SessionManager
import com.example.frontgestor.Vistas.Empresa.MenuMainE
import com.example.frontgestor.Vistas.LoginScreen


@Composable
fun Navegation(modifier : Modifier = Modifier , sesion : SessionManager){
    val navController = rememberNavController()
    val viewModel: LoginViewModel = viewModel()
    val rutaPrimera = if (sesion.isLogged()) {
            AppDestination.MenuMainE.route
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
                viewModel ,
                {

                } ,
                onNavigateToMenu = {
                    if(sesion.getTipo().equals("empresa")){
                        navController.navigate(AppDestination.MenuMainE.route)
                    }
                }
            )
        }

        composable(route = AppDestination.MenuMainE.route) {
            MenuMainE(modifier ,
                {} ,
                sesion
            )
        }

        composable(route = AppDestination.ListaTrabajadores.route) {
            // Tu vista de ListaTrabajadores()
        }
    }
}