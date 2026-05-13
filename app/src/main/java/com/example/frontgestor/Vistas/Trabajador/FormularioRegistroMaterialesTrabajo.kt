package com.example.frontgestor.Vistas.Trabajador

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
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Api.TrabajadorViewModel
import com.example.frontgestor.Modelos.MaterialDTO
import com.example.frontgestor.Modelos.RegistroMaterialDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import com.example.frontgestor.Vistas.Empresa.aFechaString
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioRegistroMaterialesTrabajo(modifier: Modifier = Modifier ,
    trabajadorViewModel: TrabajadorViewModel ,
    onback: () -> Unit ,
    session: SessionManager ,
    esEdicion: Boolean
){
    //variables del scnackba
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    // Campos de TrabajadorDTO
    val idRegistro = trabajadorViewModel.registroMaterialBuscado?.idRegistro ?: 0
    val idMaterial = trabajadorViewModel.registroMaterialBuscado?.idMaterial ?: 0
    val nombrematerial = trabajadorViewModel.registroMaterialBuscado?.tituloMaterial ?: "";
    val idTrabajo = trabajadorViewModel.registroMaterialBuscado?.idTrabajo ?: trabajadorViewModel.trabajoBuscado?.idTrabajo
    var idTrabajador by remember { mutableStateOf(trabajadorViewModel.registroMaterialBuscado?.idTrabajador  ?: session.getUserId()) }
    val nombreTrabajador = trabajadorViewModel.registroMaterialBuscado?.nombreTrabajador ?:  ""

    //para luego cambiar la fecha y el trabajador de registro si se ha modificado la cantidad
    val cantidadInicial = trabajadorViewModel.registroMaterialBuscado?.cantidad ?: 1
    var cantidad by remember { mutableStateOf(cantidadInicial) }

    var fechaHora = trabajadorViewModel.registroMaterialBuscado?.fecha ?: LocalDateTime.now().toString()
    var fecha  by remember { mutableStateOf(fechaHora.substring(0,10))}
    var hora by remember { mutableStateOf (fechaHora.substring(11,13).toInt() )}
    var minuto by remember { mutableStateOf(fechaHora.substring(14,16).toInt())}




    LaunchedEffect(Unit) {
        trabajadorViewModel.listarMateriales(session.getEmpresaId())
    }

    //variable para mostrar dialogo de alerta
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

    //para material
    var expandido by remember { mutableStateOf(false) }
    var materialSeleccionado by remember { mutableStateOf< MaterialDTO?>(null) }

    val todosLosMateriales = trabajadorViewModel.materiales ?: emptyList()
    val idsAsignados = trabajadorViewModel.registrosMateriales?.map { it.idMaterial } ?: emptyList()

    // Filtramos
    val materialesDisponibles = todosLosMateriales.filter { it.idMaterial !in idsAsignados }


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



                if( idMaterial != 0){
                    OutlinedTextField(
                        value = idMaterial.toString(),
                        onValueChange = {  },
                        label = { Text("ID Material" , color = colorResource(id = R.color.personalizadoVerdoso)) },
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
                        label = { Text("Nombre material" , color = colorResource(id = R.color.personalizadoVerdoso)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)  ,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = colorResource(id = R.color.personalizadoVerdoso) ,
                            disabledBorderColor = colorResource(id = R.color.personalizadoVerdoso)
                        )
                    )
                }else {
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
                            value = materialSeleccionado?.let { "${it.titulo}" }
                                ?: "Selecciona un material",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Materiales disponible" , color = colorResource(id = R.color.personalizadoVerdoso)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = colorResource(id = R.color.personalizadoVerdoso),
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

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { },
                    label = { Text("Fecha" , color = colorResource(id = R.color.personalizadoVerdoso)) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                        cursorColor = Color.Transparent
                    )
                )



                Row{
                    OutlinedTextField(
                        value = hora.toString(),
                        onValueChange = {
                        },
                        readOnly = true ,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        label = { Text("Hora", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center , color = colorResource(id = R.color.personalizadoVerdoso)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                            unfocusedBorderColor = Color.Gray,
                        )
                    )


                    OutlinedTextField(
                        value = minuto.toString(),
                        onValueChange = {
                        },
                        readOnly = true ,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        label = { Text("Minutos", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center , color = colorResource(id = R.color.personalizadoVerdoso)) },
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
                    trabajadorViewModel.limpiarErrorMensage()

                    if(cantidad != cantidadInicial){
                        fechaHora = LocalDateTime.now().toString().substring(0,19)
                        idTrabajador = session.getUserId()
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
                        trabajadorViewModel.editarRegistroMaterial(registroMaterial)
                    }else if(!esEdicion && cantidad > 0   && fechaHora.isNotEmpty()  && materialSeleccionado != null){
                        val registroMaterial = RegistroMaterialDTO(
                            idRegistro,
                            idTrabajo,
                            "",
                            materialSeleccionado?.idMaterial,
                            "",
                            idTrabajador,
                            "",
                            fechaHora,
                            cantidad
                        )
                        trabajadorViewModel.crearRegistroMaterial(registroMaterial)
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


            trabajadorViewModel.mensageError?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }

        }
    }
}