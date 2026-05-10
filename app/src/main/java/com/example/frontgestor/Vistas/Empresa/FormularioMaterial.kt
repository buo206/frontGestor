package com.example.frontgestor.Vistas.Empresa

import android.os.Build
import android.se.omapi.Session
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.copy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.Modelos.MaterialDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioMaterial(modifier: Modifier = Modifier ,
    empresaViewModel: EmpresaViewModel ,
    onback: () -> Unit ,
    session: SessionManager ,
    esEdicion: Boolean
){
    //variables del scnackba
    val snackbarEstado = remember { SnackbarHostState() }
    val lanzador = rememberCoroutineScope()

    // Campos de TrabajadorDTO
    val idMaterial = empresaViewModel.materialBuscado?.idMaterial ?: 0
    val idEmpresa: Int = empresaViewModel.trabajadorBuscado?.idEmpresa  ?: session.getEmpresaId()
    var titulo by remember { mutableStateOf(empresaViewModel.materialBuscado?.titulo ?: "") }
    var stock by remember { mutableStateOf(empresaViewModel.materialBuscado?.stock ?: 1) }


    //borramos el mensage para que no haya problema al salir sin guardar
    empresaViewModel.limpiarErrorMensage()

    //variable para mostrar dialogo de alerta
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

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
                    value = idMaterial.toString(),
                    onValueChange = {  },
                    label = { Text("Id Material") },
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
                    onValueChange = { titulo = it },
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



                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // Botón Menos
                    IconButton(
                        onClick = { if (stock > 0) stock-- },
                        modifier = Modifier.background(colorResource(id = R.color.personalizadoVerdoso), shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Restar", tint = Color.White)
                    }

                    // Cuadro de texto para ver/editar número
                    OutlinedTextField(
                        value = stock.toString(),
                        onValueChange = {
                            // Solo permite números y actualiza el estado
                            val newValue = it.filter { char -> char.isDigit() }
                            stock = newValue.toIntOrNull() ?: 0
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        label = { Text("Stock", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                    )

                    // Botón Más
                    IconButton(
                        onClick = { stock++ },
                        modifier = Modifier.background(colorResource(id = R.color.personalizadoVerdoso), shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Sumar", tint = Color.White)
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
                    empresaViewModel.limpiarErrorMensage()
                    if(titulo != null && titulo != "" && stock != null && stock >= 0){
                        val material = MaterialDTO(idMaterial, idEmpresa , titulo , stock)
                        if(esEdicion){
                            empresaViewModel.editarMaterial(material)
                        }else{
                            empresaViewModel.crearMaterial(material)
                        }
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