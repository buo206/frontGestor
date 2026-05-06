package com.example.frontgestor.Vistas.Empresa

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontgestor.Api.EmpresaViewModel
import com.example.frontgestor.R

@Composable
fun DetalleTrabajador(modifier: Modifier = Modifier ,
    empresaViewModel: EmpresaViewModel,
    onBack : () -> Unit ,
    onEditar: () ->Unit
){
    if(empresaViewModel.trabajadorBuscado == null){
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo empresa",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .border(2.dp, Color.Green, RoundedCornerShape(60.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nombre : ${empresaViewModel.trabajadorBuscado?.nombre ?: "No disponible"}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold ,
                color = colorResource(R.color.personalizadoVerdoso)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Información
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.personalizadoVerdoso),
                    contentColor = colorResource(R.color.white)
                )

            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Id : ${empresaViewModel.trabajadorBuscado?.idTrabajador ?: "No disponible"} ")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Apellidos : ${empresaViewModel.trabajadorBuscado?.apellidos ?: "No disponible"} ")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Correo : ${empresaViewModel.trabajadorBuscado?.email ?: "No disponible"} ")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Contraseña : ${empresaViewModel.trabajadorBuscado?.password ?: "No disponible"}")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ubicación: ${empresaViewModel.trabajadorBuscado?.dirreccion ?: "No disponible"}")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("DNI/NIE: ${empresaViewModel.trabajadorBuscado?.dni ?: "No disponible"}")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Num.Telefono: ${empresaViewModel.trabajadorBuscado?.numeroTelefono ?: "No disponible"}")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Fecha de alta: ${empresaViewModel.trabajadorBuscado?.fechaCreacion ?: "No disponible"}")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(22.dp))


            Button(
                onClick = { onEditar() },
                modifier = Modifier.fillMaxWidth() ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.personalizadoVerdoso),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text("Editar perfil")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    empresaViewModel.limpiarTrabajador()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth() ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.personalizadoVerdoso),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text("Volver atrás")
            }
        }
    }

}
