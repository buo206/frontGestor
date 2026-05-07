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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMateriales(modifier: Modifier = Modifier ,
                      sesion : SessionManager ,
                      empresaViewModel : EmpresaViewModel ,
                      onBack : () -> Unit ,
                      onDetalle:() -> Unit ,
                      onCrearNuevo : () -> Unit
){
    //variables del navigationBar
    var selectedItem by remember { mutableStateOf(3) }
    val items = listOf("Info", "Trabajadores", "Tareas","Materiales")
    val icons = listOf(Icons.Filled.Info, Icons.Filled.AccountCircle, Icons.Filled.PlayArrow , Icons.Filled.Menu)

    //variable para el snackBar
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    //variable para el buscador
    var textoBusqueda by remember { mutableStateOf("") }
    val materialesFiltrados = empresaViewModel.materiales?.filter {
        it.titulo?.contains(textoBusqueda, ignoreCase = true) == true
    }

    LaunchedEffect(Unit){
        empresaViewModel.listarMateriales(sesion.getEmpresaId())
    }

    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent ,
        snackbarHost = {
            SnackbarHost(hostState = snackbarEstado) { data ->
                Snackbar(
                    containerColor = Color.Yellow,
                    contentColor = Color.Red,
                    snackbarData = data
                )
            }
        } ,
        topBar = {
            TopAppBar(
                //pegarlo al borde de arriba
                windowInsets = TopAppBarDefaults.windowInsets ,
                title = {
                    Text("Lista :" , color = colorResource(R.color.personalizadoVerdoso))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = {
                        empresaViewModel.listarMateriales(sesion.getEmpresaId())
                        lanzador.launch {
                            snackbarEstado.showSnackbar(
                                message = "Actualizando lista de Materiales...",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar",
                            tint = Color(0xFF2BB673)
                        )
                    }
                    IconButton(onClick = { onCrearNuevo()}) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir materiales",
                            tint = Color(0xFF2BB673)
                        )
                    }
                }
            )
        } ,
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
                            if(index ==1){

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddding)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar  Materiales...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.personalizadoVerdoso),
                    unfocusedBorderColor = Color.Gray
                )
            )


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
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(materialesFiltrados ?: emptyList()) { material ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.personalizadoVerdoso),
                                contentColor = Color.White
                            ) ,
                            onClick = {
                                /*
                                empresaViewModel.buscarTrabajador(material.idTrabajador)
                                onDetalle()*/
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Categoria",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Id : ${material.idMaterial ?: "No disponible"}",
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
                                        text = "Nombre : ${material.titulo ?: "No disponible"}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }

                                Row {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowUp,
                                        contentDescription = "Categoria",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Stock : ${material.stock ?: "No disponible"}",
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
}