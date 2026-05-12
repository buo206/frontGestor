package com.example.frontgestor.Vistas.Empresa

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Modelos.MaterialDTO
import com.example.frontgestor.Modelos.RegistroMaterialDTO
import com.example.frontgestor.Modelos.RegistroTrabajoDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioRegistroMateriales(modifier: Modifier = Modifier ,
    empresaViewModel: EmpresaViewModel ,
    onback: () -> Unit ,
    session: SessionManager ,
    esEdicion: Boolean
){
    //variables del scnackba
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    // Campos de TrabajadorDTO
    val idRegistro = empresaViewModel.registroMaterialBuscado?.idRegistro ?: 0
    val idMaterial = empresaViewModel.registroMaterialBuscado?.idMaterial ?: 0
    val nombrematerial = empresaViewModel.registroMaterialBuscado?.tituloMaterial ?: "";
    val idTrabajo = empresaViewModel.registroMaterialBuscado?.idTrabajo ?: empresaViewModel.trabajoBuscado?.idTrabajo
    val idTrabajador = empresaViewModel.registroMaterialBuscado?.idTrabajador  ?: 0
    val nombreTrabajador = empresaViewModel.registroMaterialBuscado?.nombreTrabajador ?:  ""
    var cantidad by remember { mutableStateOf(empresaViewModel.registroMaterialBuscado?.cantidad ?: 1) }
    var fechaHora = empresaViewModel.registroMaterialBuscado?.fecha ?: LocalDateTime.now().toString()
    var fecha  by remember { mutableStateOf(fechaHora.substring(0,10))}
    var hora by remember { mutableStateOf (fechaHora.substring(11,13).toInt() )}
    var minuto by remember { mutableStateOf(fechaHora.substring(14,16).toInt())}




    LaunchedEffect(Unit) {
        empresaViewModel.listarMateriales(session.getEmpresaId())
        empresaViewModel.listarTrabajadores(session.getEmpresaId())
    }

    //variable para mostrar dialogo de alerta
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

    //para material
    var expandido by remember { mutableStateOf(false) }
    var materialSeleccionado by remember { mutableStateOf< MaterialDTO?>(null) }

    val todosLosMateriales = empresaViewModel.materiales ?: emptyList()
    val idsAsignados = empresaViewModel.registrosMateriales?.map { it.idMaterial } ?: emptyList()

    // Filtramos
    val materialesDisponibles = todosLosMateriales.filter { it.idMaterial !in idsAsignados }


    //para trabajador
    var expandidoTrabajadores by remember { mutableStateOf(false) }
    var trabajadorSeleccionado by remember { mutableStateOf< TrabajadorListaDTO?>(null) }

    val todosLosTrabajadores = empresaViewModel.trabajadores ?: emptyList()
    val idsAsignadosT = empresaViewModel.registrosTrabajo?.map { it.idTrabajador } ?: emptyList()

    // Filtramos
    val trabajadoresDisponibles = todosLosTrabajadores.filter { it.idTrabajador in idsAsignadosT }


    //variables para el selector de fechas
    var mostrarPickerInicial by remember { mutableStateOf(false) }

    val datePickerStateInicial = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

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
                    containerColor = colorResource(R.color.personalizadoVerdoso),
                    title = { Text(text = "Cambios sin guardar" , color = Color.White) },
                    text = { Text(text = "¿Estás seguro de que quieres salir? Se perderán los cambios que no hayas guardado."  , color = Color.White) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                mostrarDialogoSalida = false
                                onback()
                            }
                        ) {
                            Text("Salir sin guardar", color = Color.Green)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { mostrarDialogoSalida = false }
                        ) {
                            Text(
                                "Permanecer y arreglar",
                                color = Color.Green
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
                    value = idRegistro.toString(),
                    onValueChange = {  },
                    label = { Text("Id Registro") },
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



                if(idTrabajador != 0 && idMaterial != 0){
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
                        value = idMaterial.toString(),
                        onValueChange = {  },
                        label = { Text("ID Material") },
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
                        value = nombrematerial,
                        onValueChange = { },
                        label = { Text("Nombre material") },
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
                        "Asignar nuevo Material",
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
                            value = materialSeleccionado?.let { "${it.titulo}" } ?: "Selecciona un material",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Materiales disponible") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = colorResource(id = R.color.personalizadoVerdoso) ,
                                disabledBorderColor = colorResource(id = R.color.personalizadoVerdoso)
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
                            if (materialesDisponibles.isNullOrEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay materiales disponibles") },
                                    onClick = { expandido = false }
                                )
                            } else {
                                materialesDisponibles?.forEach { material ->
                                    DropdownMenuItem(
                                        text = { Text("${material.idMaterial}.${material.titulo} con stock : ${material.stock} ") },
                                        onClick = {
                                            materialSeleccionado = material
                                            expandido = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }


                    Text(
                        "Asignar trabajador",
                        modifier = Modifier.padding(8.dp),
                        color = Color.Green
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandidoTrabajadores,
                        onExpandedChange = { expandidoTrabajadores = !expandidoTrabajadores },
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
                            expanded = expandidoTrabajadores,
                            onDismissRequest = { expandidoTrabajadores = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            if (trabajadoresDisponibles.isNullOrEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay trabajadores disponibles") },
                                    onClick = { expandidoTrabajadores = false }
                                )
                            } else {
                                trabajadoresDisponibles?.forEach { trabajador ->
                                    DropdownMenuItem(
                                        text = { Text("${trabajador.idTrabajador}.${trabajador.nombre} con email : ${trabajador.email} ") },
                                        onClick = {
                                            trabajadorSeleccionado = trabajador
                                            expandidoTrabajadores = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // Botón Menos
                    IconButton(
                        onClick = { if (cantidad > 0) cantidad-- },
                        modifier = Modifier.background(colorResource(id = R.color.personalizadoVerdoso), shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Restar", tint = Color.White)
                    }

                    // Cuadro de texto para ver/editar número
                    OutlinedTextField(
                        value = cantidad.toString(),
                        onValueChange = {
                            // Solo permite números y actualiza el estado
                            val newValue = it.filter { char -> char.isDigit() }
                            cantidad = newValue.toIntOrNull() ?: 0
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        label = { Text("Stock", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            unfocusedBorderColor = Color.Gray,
                        )

                    )

                    // Botón Más
                    IconButton(
                        onClick = { cantidad++ },
                        modifier = Modifier.background(colorResource(id = R.color.personalizadoVerdoso), shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Sumar", tint = Color.White)
                    }
                }


                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { },
                        label = { Text("Fecha") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
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

                if (mostrarPickerInicial) {
                    DatePickerDialog(
                        onDismissRequest = { mostrarPickerInicial = false },
                        confirmButton = {
                            Button(onClick = {
                                val inicioMs = datePickerStateInicial.selectedDateMillis ?: 0L
                                fecha = inicioMs.aFechaString()
                                mostrarPickerInicial = false
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

                Row{
                    OutlinedTextField(
                        value = hora.toString(),
                        onValueChange = {
                            val newValue = it.filter { char -> char.isDigit() }
                            hora = newValue.toIntOrNull() ?: 0
                            if(hora > 24){
                                hora = 24
                            }else if(hora < 0){
                                hora = 0
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        label = { Text("Hora", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            unfocusedBorderColor = Color.Gray,
                        )
                    )


                    OutlinedTextField(
                        value = minuto.toString(),
                        onValueChange = {
                            val newValue = it.filter { char -> char.isDigit() }
                            minuto = newValue.toIntOrNull() ?: 0
                            if(minuto > 59){
                                minuto = 59
                            }else if(minuto < 0){
                                minuto = 0
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        label = { Text("Minutos", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            unfocusedBorderColor = Color.Gray,
                        )
                    )
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
                    empresaViewModel.limpiarErrorMensage()

                    var fechaHora = fecha +"T"
                    if(hora <10){
                        fechaHora += "0"+hora+":"
                    }else{
                        fechaHora += hora.toString() + ":"
                    }

                    if(minuto < 10 ){
                        fechaHora += "0"+minuto+":00"
                    }else{
                        fechaHora+= minuto.toString()+":00"
                    }

                    if(esEdicion &&  cantidad > 0   && fechaHora.isNotEmpty() ){
                        val registroMaterial = RegistroMaterialDTO(
                            idRegistro,
                            idTrabajo,
                            "",
                            idMaterial,
                            "",
                            idTrabajador,
                            "",
                            fechaHora,
                            cantidad
                        )
                        empresaViewModel.editarRegistroMaterial(registroMaterial)
                    }else if(!esEdicion && cantidad > 0   && fechaHora.isNotEmpty() && trabajadorSeleccionado != null && materialSeleccionado != null){
                        val registroMaterial = RegistroMaterialDTO(
                            idRegistro,
                            idTrabajo,
                            "",
                            materialSeleccionado?.idMaterial,
                            "",
                            trabajadorSeleccionado?.idTrabajador,
                            "",
                            fechaHora,
                            cantidad
                        )
                        empresaViewModel.crearRegistroMaterial(registroMaterial)
                    }else{
                        lanzador.launch {
                            snackbarEstado.showSnackbar("Error al introducir los cambios reviselos , expecificamente la cantidad (minimo 1) y la fecha(YYY-MM-DD)")
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
