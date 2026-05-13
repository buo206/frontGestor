package com.example.frontgestor.Vistas.Empresa

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Modelos.RegistroTrabajoDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioRegistroTrabajo(modifier: Modifier = Modifier ,
    empresaViewModel: EmpresaViewModel ,
    onback: () -> Unit ,
    session: SessionManager ,
    esEdicion: Boolean
){
    //variables del scnackba
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    // Campos de TrabajadorDTO
    val idTrabajo = empresaViewModel.registroTrabajobuscado?.idTrabajo ?: empresaViewModel.trabajoBuscado?.idTrabajo
    val idTrabajador = empresaViewModel.registroTrabajobuscado?.idTrabajador  ?: 0
    val nombreTrabajador = empresaViewModel.registroTrabajobuscado?.nombreTrabajador ?:  ""
    val apellidosTrabajador = empresaViewModel.registroTrabajobuscado?.apellidosTrabajador ?: ""
    var rol by remember { mutableStateOf(empresaViewModel.registroTrabajobuscado?.rol ?: "") }



    LaunchedEffect(Unit) {
        empresaViewModel.limpiarErrorMensage()
        empresaViewModel.listarTrabajadores(session.getEmpresaId())
    }

    //variable para mostrar dialogo de alerta
    var mostrarDialogoSalida by remember { mutableStateOf(false) }


    var expandido by remember { mutableStateOf(false) }
    var trabajadorSeleccionado by remember { mutableStateOf< TrabajadorListaDTO?>(null) }

    val todosLosTrabajadores = empresaViewModel.trabajadores ?: emptyList()
    val idsAsignados = empresaViewModel.registrosTrabajo?.map { it.idTrabajador } ?: emptyList()

    // Filtramos
    val trabajadoresDisponibles = todosLosTrabajadores.filter { it.idTrabajador !in idsAsignados }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (mostrarDialogoSalida) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoSalida = false },
                    title = { Text(text = "Cambios sin guardar") },
                    text = { Text(text = "¿Estás seguro de que quieres salir? Se perderán los cambios que no hayas guardado.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                mostrarDialogoSalida = false
                                onback()
                            }
                        ) {
                            Text("Salir sin guardar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { mostrarDialogoSalida = false }
                        ) {
                            Text(
                                "Permanecer y arreglar",
                                color = colorResource(id = R.color.personalizadoVerdoso)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    value = idTrabajo.toString(),
                    onValueChange = {  },
                    label = { Text("Id Trabajo") },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)  ,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = colorResource(id = R.color.personalizadoVerdoso) ,
                        disabledBorderColor = colorResource(id = R.color.personalizadoVerdoso)
                    )
                )



                if(idTrabajador != 0){
                    OutlinedTextField(
                        value = idTrabajador.toString(),
                        onValueChange = {  },
                        label = { Text("ID Trabajador") },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)  ,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = colorResource(id = R.color.personalizadoVerdoso) ,
                            disabledBorderColor = colorResource(id = R.color.personalizadoVerdoso)
                        )
                    )

                    OutlinedTextField(
                        value = nombreTrabajador,
                        onValueChange = { },
                        label = { Text("Nombre Trabajador") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)  ,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = colorResource(id = R.color.personalizadoVerdoso) ,
                            disabledBorderColor = colorResource(id = R.color.personalizadoVerdoso)
                        )
                    )
                    OutlinedTextField(
                        value = apellidosTrabajador,
                        onValueChange = { },
                        label = { Text("Apellidos Trabajador") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)  ,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = colorResource(id = R.color.personalizadoVerdoso) ,
                            disabledBorderColor = colorResource(id = R.color.personalizadoVerdoso)
                        )
                    )
                }else{
                    Text(
                        "Asignar nuevo trabajador",
                        modifier = Modifier.padding(8.dp),
                        color = Color.Green
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandido,
                        onExpandedChange = { expandido = !expandido },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = trabajadorSeleccionado?.let { "${it.nombre}" } ?: "Selecciona un trabajador",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Trabajador disponible") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                                unfocusedBorderColor = Color.Gray,
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandido,
                            onDismissRequest = { expandido = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            if (trabajadoresDisponibles.isNullOrEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay trabajadores disponibles") },
                                    onClick = { expandido = false }
                                )
                            } else {
                                trabajadoresDisponibles?.forEach { trabajador ->
                                    DropdownMenuItem(
                                        text = { Text("${trabajador.idTrabajador}.${trabajador.nombre} con email : ${trabajador.email} ") },
                                        onClick = {
                                            trabajadorSeleccionado = trabajador
                                            expandido = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                }


                OutlinedTextField(
                    value = rol,
                    onValueChange = { rol = it },
                    label = { Text("Rol o cargo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)  ,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = colorResource(id = R.color.personalizadoVerdoso),
                        cursorColor = colorResource(id = R.color.personalizadoVerdoso)
                    )
                )





            }


            Spacer(modifier = Modifier.height(12.dp))



            Button(
                onClick = {
                    if(empresaViewModel.mensageError != null){
                        lanzador.launch {
                            snackbarEstado.showSnackbar(empresaViewModel.mensageError.toString())
                        }
                        mostrarDialogoSalida = true
                    }else{
                        onback()
                    }

                },
                modifier = Modifier.fillMaxWidth() ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.personalizadoVerdoso),
                    contentColor = Color.White
                )
            ) {
                Text("Salir")
            }

            SnackbarHost(
                hostState = snackbarEstado,
                modifier = Modifier.padding(2.dp)
            ){ data ->
                Snackbar(
                    containerColor = Color.Yellow,
                    contentColor = Color.Red,
                    snackbarData = data
                )
            }

            Button(
                onClick = {
                    empresaViewModel.limpiarErrorMensage()

                    if(esEdicion &&  rol.isNotEmpty()){
                        val registroTrabajo = RegistroTrabajoDTO(idTrabajo , "" ,idTrabajador ,nombreTrabajador , apellidosTrabajador ,rol)
                        empresaViewModel.editarRegistroTrabajo(registroTrabajo)
                    }else if(!esEdicion && trabajadorSeleccionado != null  &&  rol.isNotEmpty()){
                        val registroTrabajo = RegistroTrabajoDTO(idTrabajo , "" ,trabajadorSeleccionado!!.idTrabajador ,"" , "" ,rol)
                        empresaViewModel.crearRegistroTrabajo(registroTrabajo)
                    }else{
                        lanzador.launch {
                            snackbarEstado.showSnackbar("Error al introducir los cambios reviselos , expecificamente el nombre , email o contraseña ")
                        }
                    }

                },
                modifier = Modifier.fillMaxWidth() ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.personalizadoVerdoso),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar")
            }


            empresaViewModel.mensageError?.let {
                lanzador.launch {
                    snackbarEstado.showSnackbar(it)
                }
            }

        }
    }
}