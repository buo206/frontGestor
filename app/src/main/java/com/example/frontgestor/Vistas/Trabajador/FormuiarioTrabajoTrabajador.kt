package com.example.frontgestor.Vistas.Trabajador

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import java.time.format.DateTimeFormatter
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Api.TrabajadorViewModel
import com.example.frontgestor.Modelos.TrabajoDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.ExperimentalTime
import java.time.Instant
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioTrabajoTrabajador(modifier: Modifier = Modifier ,
    trabajadorViewModel: TrabajadorViewModel ,
    onback: () -> Unit ,
    onEditarRegistroMateriales: () -> Unit,
    onCrearRegistroMateriales: () -> Unit
){
    //variables del scnackba
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    // Campos de TrabajadorDTO
    val idTrabajo = trabajadorViewModel.trabajoBuscado?.idTrabajo ?: 0
    val idEmpresa: Int = trabajadorViewModel.trabajoBuscado?.idEmpresa  ?: 0
    var titulo by remember { mutableStateOf(trabajadorViewModel.trabajoBuscado?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(trabajadorViewModel.trabajoBuscado?.descripcion ?: "") }
    var anotacion by remember { mutableStateOf(trabajadorViewModel.trabajoBuscado?.anotacion ?: "") }
    var estado by remember { mutableStateOf(trabajadorViewModel.trabajoBuscado?.estado ?: "Espera") }
    if(estado.uppercase().equals( "P")){
        estado = "Proceso"
    }else if(estado.uppercase().equals("E")){
        estado = "Espera"
    }else if(estado.uppercase().equals("R")){
        estado = "Revisión"
    }
    var fechaFinal by remember { mutableStateOf(trabajadorViewModel.trabajoBuscado?.fechaFinal ?: "") }

    var fechaInicial by remember { mutableStateOf(trabajadorViewModel.trabajoBuscado?.fechaInicio ?: "") }

    //borramos el mensage para que no haya problema al salir sin guardar
    trabajadorViewModel.limpiarErrorMensage()

    //variable para mostrar dialogo de alerta
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

    //variable para dropdowbn de estado
    var expandido by remember { mutableStateOf(false) }
    val estados = listOf("Espera", "Proceso", "Revisión")

    LaunchedEffect(Unit){
        trabajadorViewModel.buscarRegistroMaterialPorTrabajo(idTrabajo)
    }


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
                    label = { Text("Id Trabajo" , color = colorResource(id = R.color.personalizadoVerdoso)) },
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
                    value = titulo,
                    onValueChange = { titulo= it },
                    label = { Text("Titulo" , color = colorResource(id = R.color.personalizadoVerdoso) ) },
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
                    value = descripcion,
                    onValueChange = { descripcion= it },
                    label = { Text("Descripcion") },
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


                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estados") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = colorResource(id = R.color.personalizadoVerdoso),
                            cursorColor = colorResource(id = R.color.personalizadoVerdoso)
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
                        estados?.forEach { estadox ->
                            DropdownMenuItem(
                                text = { Text(estadox) },
                                onClick = {
                                    estado = estadox
                                    if(estadox.equals("Revisión")){
                                        fechaFinal = LocalDate.now().toString()
                                    }
                                    expandido = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = fechaInicial,
                    onValueChange = { },
                    label = { Text("Fecha inicial" ,color = colorResource(id = R.color.personalizadoVerdoso)) },
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
                    value = fechaFinal,
                    onValueChange = { },
                    label = { Text("Fecha final" , color = colorResource(id = R.color.personalizadoVerdoso) ) },
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
                    value = anotacion,
                    onValueChange = { anotacion = it },
                    label = { Text("Explicacion trabajador ") },
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


                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        trabajadorViewModel.limpiarRegistroMaterialBuscado()
                        onCrearRegistroMateriales()
                    }){
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(R.color.personalizadoVerdoso)
                        )
                    }
                    Text(
                        text = "Lista de registro de material",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(4.dp),
                        color = colorResource(R.color.personalizadoVerdoso)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                trabajadorViewModel.registrosMateriales?.forEach { registroMaterial ->
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
                            trabajadorViewModel.setRegistroMaterial(registroMaterial)
                            trabajadorViewModel.buscarRegistroMaterialPorTodo(registroMaterial.idTrabajador ?: 0 , registroMaterial.idTrabajo ?: 0 , registroMaterial.idMaterial ?: 0)
                            onEditarRegistroMateriales()
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
                                    text = "Id : ${registroMaterial.idRegistro ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Trabajador",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Trabajador : ${registroMaterial.nombreTrabajador ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }


                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Categoria",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Material : ${registroMaterial.tituloMaterial ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Categoria",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Cantidad : ${registroMaterial.cantidad ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Categoria",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Fecha y hora : ${registroMaterial.fecha ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                        }
                    }
                }


            }


            Spacer(modifier = Modifier.height(12.dp))



            Button(
                onClick = {
                    if(trabajadorViewModel.mensageError != null){
                        lanzador.launch {
                            snackbarEstado.showSnackbar(trabajadorViewModel.mensageError.toString())
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
                    trabajadorViewModel.limpiarErrorMensage();
                    var estadoFinal = "Proceso"
                    if(estado.equals( "Proceso")){
                        estadoFinal = "P"
                    }else if(estado.equals("Espera")){
                        estadoFinal = "E"
                    }else if(estado.equals("Revisión")){
                        estadoFinal = "R"
                    }
                    if(titulo.isNotEmpty() && descripcion.isNotEmpty()   && estado.isNotEmpty()){
                        val trabajo = TrabajoDTO(idTrabajo , idEmpresa , titulo , descripcion ,  fechaInicial , fechaFinal ,anotacion , estadoFinal )
                        trabajadorViewModel.editarTrabajo(trabajo)
                    }else{
                        lanzador.launch {
                            snackbarEstado.showSnackbar("Error al introducir los cambios reviselos , expecificamente el titulo , descripcion o fecha inicio ")
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


            trabajadorViewModel.mensageError?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }

        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
fun Long?.aFechaString(): String {
    if (this == null) return ""
    val fecha = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}