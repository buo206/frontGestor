package com.example.frontgestor.Vistas.Empresa

import android.os.Build
import android.se.omapi.Session
import androidx.activity.result.launch
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
    esEdicion: Boolean
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

            }


            Spacer(modifier = Modifier.height(12.dp))



            Button(
                onClick = {
                    onback()
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
