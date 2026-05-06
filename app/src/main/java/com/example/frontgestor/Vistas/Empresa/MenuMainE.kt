package com.example.frontgestor.Vistas.Empresa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager

@Composable
fun MenuMainE(modifier: Modifier = Modifier,
    onNavegationToLista: () -> Unit ,
    sesion : SessionManager ,
    empresaViewModel : EmpresaViewModel ,
    onBack : () -> Unit
){
    //variables del navigationBar
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Info", "Trabajadores", "Tareas")
    val icons = listOf(Icons.Filled.Info, Icons.Filled.AccountCircle, Icons.Filled.PlayArrow)

    //lamamos siempre que se llama a esta pantalla para referescar la infoprmacion
    empresaViewModel.bucarEmpresa(sesion.getEmpresaId())
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent ,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 1.dp ,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = colorResource(R.color.personalizadoVerdoso),
                            indicatorColor = colorResource(R.color.personalizadoVerdoso),
                            unselectedIconColor = colorResource(R.color.personalizadoVerdoso),
                            unselectedTextColor = colorResource(R.color.personalizadoVerdoso)
                        ),
                        selected = selectedItem == index,
                        onClick = {
                            if(index == 1){
                                onNavegationToLista()
                            }
                            if(index == 2){

                            }

                        }
                    )
                }
            }
        }
    ) { innerPaddding ->

        Box(modifier = modifier.background(Color.White).padding(innerPaddding)){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo empresa",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .border(2.dp, Color.Green, RoundedCornerShape(60.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre empresa
                Text(
                    text = "Nombre : ${empresaViewModel.empresa?.nombre ?: "No disponible"}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold ,
                    color = colorResource(R.color.personalizadoVerdoso)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Información
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.personalizadoVerdoso),
                        contentColor = colorResource(R.color.white)
                    )

                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text("Correo : ${empresaViewModel.empresa?.email ?: "No disponible"} ")
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Apellidos : ${empresaViewModel.empresa?.apellidos ?: "No disponible"} ")
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Ubicación: ${empresaViewModel.empresa?.direccion ?: "No disponible"}")
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))


                Button(
                    onClick = { /* editar perfil */ },
                    modifier = Modifier.fillMaxWidth() ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.personalizadoVerdoso),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Text("Editar perfil")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        sesion.logout()
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth() ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.personalizadoVerdoso),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }

}