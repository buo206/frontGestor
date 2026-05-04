package com.example.frontgestor.Vistas.Empresa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager


@Composable
fun ListaTrabajadores(modifier: Modifier = Modifier ,
    sesion : SessionManager ,
    empresaViewModel : EmpresaViewModel ,
    onBack : () -> Unit
){
    //variables del navigationBar
    var selectedItem by remember { mutableStateOf(1) }
    val items = listOf("Info", "Trabajadores", "Tareas")
    val icons = listOf(Icons.Filled.Info, Icons.Filled.AccountCircle, Icons.Filled.PlayArrow)


    LaunchedEffect(Unit){
        empresaViewModel.listarTrabajadores(sesion.getEmpresaId())
    }

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
                            if(index == 0){
                                onBack()
                            }
                            if(index == 2){

                            }

                        }
                    )
                }
            }
        }
    ) { innerPaddding ->
        Spacer(Modifier.height(16.dp))
        if(empresaViewModel.trabajadores == null){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF2BB673), // tu verde
                    strokeWidth = 4.dp
                )
            }
        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddding) ,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(empresaViewModel.trabajadores !!) { trabajador ->

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.personalizadoVerdoso),
                            contentColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Categoria",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Id : ${trabajador.idTrabajador ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Categoria",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Nombre : ${trabajador.nombre ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "Categoria",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Email : ${trabajador.email ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                        }
                    }
                }
            }

        }
    }

}