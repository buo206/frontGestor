package com.example.frontgestor.Vistas.Empresa

import android.os.Build
import android.se.omapi.Session
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.darkColorScheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.ExperimentalTime
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioTrabajo(modifier: Modifier = Modifier ,
    empresaViewModel: EmpresaViewModel ,
    onback: () -> Unit ,
    session: SessionManager ,
    esEdicion: Boolean ,
    onEditarRegistroTrabajador : () -> Unit,
    onCrearRegistroTrabajador: () -> Unit ,
    onEditarRegistroMateriales: () -> Unit,
    onCrearRegistroMateriales: () -> Unit
){
    //variables del scnackba
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    // Campos de TrabajadorDTO
    val idTrabajo = empresaViewModel.trabajoBuscado?.idTrabajo ?: 0
    val idEmpresa: Int = empresaViewModel.trabajoBuscado?.idEmpresa  ?: session.getEmpresaId()
    var titulo by remember { mutableStateOf(empresaViewModel.trabajoBuscado?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(empresaViewModel.trabajoBuscado?.descripcion ?: "") }
    var anotacion by remember { mutableStateOf(empresaViewModel.trabajoBuscado?.anotacion ?: "") }
    var estado by remember { mutableStateOf(empresaViewModel.trabajoBuscado?.estado ?: "Espera") }
    if(estado.uppercase().equals( "P")){
        estado = "Proceso"
    }else if(estado.uppercase().equals("F")){
        estado = "Finalizado"
    }else if(estado.uppercase().equals("E")){
        estado = "Espera"
    }else if(estado.uppercase().equals("R")){
        estado = "Revisión"
    }
    var fechaFinal by remember { mutableStateOf(empresaViewModel.trabajoBuscado?.fechaFinal ?: "") }

    val fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    var fechaInicial by remember { mutableStateOf(empresaViewModel.trabajoBuscado?.fechaInicio ?: fechaActual) }


    //variables para el selector de fechas
    var mostrarPickerInicial by remember { mutableStateOf(false) }
    var mostrarPickerFinal by remember { mutableStateOf(false) }

    val datePickerStateInicial = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val datePickerStateFinal = rememberDatePickerState()


    //variable para saber si mosrtrar las opciones/datos a avanzadas
    var mostrarOpcionesAvanzadas by remember { mutableStateOf(false) }


    //borramos el mensage para que no haya problema al salir sin guardar
    empresaViewModel.limpiarErrorMensage()
    //y con los materiales para no ver otros registro de materiales que no sean los de este trabajo
    empresaViewModel.limpiarListaMateriales()

    //variable para mostrar dialogo de alerta
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        empresaViewModel.buscarRegistroMaterialPorTrabajo(idTrabajo)
        empresaViewModel.listarRegistrosTrabajo(idTrabajo)
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



                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo= it },
                    label = { Text("Titulo") },
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



                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado ") },
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



                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    OutlinedTextField(
                        value = fechaInicial,
                        onValueChange = { },
                        label = { Text("Fecha inicial") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true, // No permite escribir a mano
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            cursorColor = Color.Transparent
                        )
                    )
                    Box(modifier = Modifier
                        .matchParentSize()
                        .clickable { mostrarPickerInicial = true }
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    OutlinedTextField(
                        value = fechaFinal,
                        onValueChange = { },
                        label = { Text("Fecha final") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            cursorColor = Color.Transparent
                        )
                    )
                    Box(modifier = Modifier
                        .matchParentSize()
                        .clickable { mostrarPickerFinal = true }
                    )
                }

                if (mostrarPickerInicial) {
                    DatePickerDialog(
                        onDismissRequest = { mostrarPickerInicial = false },
                        confirmButton = {
                            Button(onClick = {
                                val inicioMs = datePickerStateInicial.selectedDateMillis ?: 0L
                                val finMs = datePickerStateFinal.selectedDateMillis


                                if (finMs != null && inicioMs > finMs) {
                                    lanzador.launch {
                                        snackbarEstado.showSnackbar("La fecha inicial no puede ser posterior a la final")
                                    }
                                } else {
                                    fechaInicial = inicioMs.aFechaString()
                                    mostrarPickerInicial = false
                                }
                            },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.White
                                )

                            ) { Text("Aceptar") }
                        },
                        dismissButton = {
                            Button(
                                onClick = { mostrarPickerInicial = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.White
                                )
                            ) { Text("Cancelar") }
                        },
                        colors = DatePickerDefaults.colors(
                            containerColor = colorResource(id = R.color.personalizadoVerdoso),
                            titleContentColor = colorResource(id = R.color.personalizadoVerdoso),
                            headlineContentColor = colorResource(id = R.color.personalizadoVerdoso)
                        )
                    ) {
                        DatePicker(
                            state = datePickerStateInicial,
                            colors = DatePickerDefaults.colors(
                                containerColor = colorResource(id = R.color.personalizadoVerdoso),
                                titleContentColor = colorResource(id = R.color.personalizadoVerdoso),
                                headlineContentColor = Color.White ,
                                selectedDayContainerColor = Color.Green,
                                selectedDayContentColor = Color.White,
                                todayContentColor = Color.White,
                                todayDateBorderColor = colorResource(id = R.color.personalizadoVerdoso),

                            )
                        )
                    }
                }

                if (mostrarPickerFinal) {
                    DatePickerDialog(
                        onDismissRequest = { mostrarPickerFinal = false },
                        confirmButton = {
                            Button(onClick = {
                                val inicioMs = datePickerStateInicial.selectedDateMillis ?: 0L
                                val finMs = datePickerStateFinal.selectedDateMillis ?: 0L

                                if (finMs >= inicioMs) {
                                    fechaFinal = finMs.aFechaString()
                                    mostrarPickerFinal = false
                                } else {
                                    lanzador.launch {
                                        snackbarEstado.showSnackbar("La fecha final debe ser posterior a la inicial")
                                    }
                                }
                            },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.White
                                )
                            ) { Text("Aceptar") }
                        },
                        dismissButton = {
                            Button(
                                onClick = {mostrarPickerFinal = false} ,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.White
                                )
                            ){
                                Text("Cerrar")
                            }
                        },
                        colors = DatePickerDefaults.colors(
                            containerColor = colorResource(id = R.color.personalizadoVerdoso),
                            titleContentColor = colorResource(id = R.color.personalizadoVerdoso),
                            headlineContentColor = colorResource(id = R.color.personalizadoVerdoso)
                        )
                    ) {
                        DatePicker(
                            state = datePickerStateFinal,
                            colors = DatePickerDefaults.colors(
                                containerColor = colorResource(id = R.color.personalizadoVerdoso),
                                titleContentColor = colorResource(id = R.color.personalizadoVerdoso),
                                headlineContentColor = Color.White ,
                                selectedDayContainerColor = Color.Green,
                                selectedDayContentColor = Color.White,
                                todayContentColor = Color.White,
                                todayDateBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            )
                        )
                    }
                }


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

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        empresaViewModel.limpiarRegistroTrabajoBuscado()
                        onCrearRegistroTrabajador()
                    }){
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(R.color.personalizadoVerdoso)
                        )
                    }
                    Text(
                        text = "Lista de Trabajadores",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(4.dp),
                        color = colorResource(R.color.personalizadoVerdoso)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                empresaViewModel.registrosTrabajo?.forEach{ registroTrabajo ->
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
                            empresaViewModel.setRegistroTrabajo(registroTrabajo)
                            empresaViewModel.buscarRegistroTrabajo(idTrabajo , registroTrabajo.idTrabajador)
                            onEditarRegistroTrabajador()
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row {

                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Nombre",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Nombre : ${registroTrabajo.nombreTrabajador  ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Face,
                                    contentDescription = "Apellidos",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Apellidos : ${registroTrabajo.apellidosTrabajador  ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Rol",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Rol : ${registroTrabajo.rol ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                        }
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        empresaViewModel.limpiarRegistroMaterialBuscado()
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
                empresaViewModel.registrosMateriales?.forEach { registroMaterial ->
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
                            empresaViewModel.setRegistroMaterial(registroMaterial)
                            empresaViewModel.buscarRegistroMaterialPorTodo(registroMaterial.idTrabajador , registroMaterial.idTrabajo , registroMaterial.idMaterial)
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
                    empresaViewModel.limpiarErrorMensage();
                    if(titulo.isNotEmpty() && descripcion.isNotEmpty() && fechaInicial.isNotEmpty() && fechaFinal.isNotEmpty() && estado.isNotEmpty()){
                        /*val trabajador
                        if(esEdicion){
                            empresaViewModel.editarTrabajador(trabajador)
                        }else{
                            empresaViewModel.crearTrabajador(trabajador)
                        }*/
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
