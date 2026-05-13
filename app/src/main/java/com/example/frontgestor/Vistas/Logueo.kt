package com.example.frontgestor.Vistas

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.frontgestor.Api.LoginViewModel
import com.example.frontgestor.Modelos.EmpresaDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.R
import com.example.frontgestor.SessionManager


@Composable
fun LoginScreen( modifier: Modifier = Modifier ,
    viewModel : LoginViewModel,
    onLoginSuccess: () -> Unit ,
    onNavigateToMenu: () -> Unit
) {
    var isEmpresa by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val session = remember { SessionManager(context) }


    Box(
        modifier = modifier
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

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(colorResource(id = R.color.personalizadoVerdoso))
                    .clickable { isEmpresa = !isEmpresa }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isEmpresa) Color.Green else Color.Transparent,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Empresa",
                        color = if (isEmpresa) Color.White else Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (!isEmpresa) Color.Green else Color.Transparent,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Trabajador",
                        color = if (!isEmpresa) Color.White else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Campos de login
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth() ,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = colorResource(id = R.color.personalizadoVerdoso),
                    cursorColor = colorResource(id = R.color.personalizadoVerdoso)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth() ,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.personalizadoVerdoso),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = colorResource(id = R.color.personalizadoVerdoso),
                    cursorColor = colorResource(id = R.color.personalizadoVerdoso)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.login(email, password, isEmpresa) {

                        if (isEmpresa && viewModel.empresa != null) {
                            val idEmpresa = viewModel.empresa?.idEmpresa ?: 0
                            session.saveUser(idEmpresa , 0, "empresa")
                        } else if(viewModel.trabajador != null){
                            val idTrabajador = viewModel.trabajador?.idTrabajador ?: 0
                            val idEmpresa = viewModel.trabajador?.idEmpresa ?: 0
                            session.saveUser(idEmpresa , idTrabajador, "trabajador")
                        }

                        if(viewModel.empresa != null || viewModel.trabajador != null ){
                            onLoginSuccess()
                            onNavigateToMenu()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth() ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.personalizadoVerdoso),
                    contentColor = Color.White
                )
            ) {
                Text("Iniciar sesión")
            }

            viewModel.mensageError?.let {
                Text(
                    text = it, color = Color.Red,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}