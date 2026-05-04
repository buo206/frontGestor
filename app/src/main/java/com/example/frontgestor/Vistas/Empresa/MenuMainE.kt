package com.example.frontgestor.Vistas.Empresa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
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
import com.example.frontgestor.SessionManager

@Composable
fun MenuMainE(modifier: Modifier = Modifier,
    onNavegationToLista: () -> Unit ,
    sesion : SessionManager ,
    empresaViewModel : EmpresaViewModel ,
    onBack : () -> Unit
){
    empresaViewModel.bucarEmpresa(sesion.getEmpresaId())
    Box(modifier = modifier.background(Color.White)){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo empresa",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .border(2.dp, Color.Green, RoundedCornerShape(60.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre empresa
            Text(
                text = "Nombre : ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
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

                    Text("Correo : ${empresaViewModel.empresa?.email ?: "No disponible"} ")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Apellidos : ${empresaViewModel.empresa?.apellidos ?: "No disponible"} ")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ubicación: ")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = { /* editar perfil */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar perfil")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    sesion.logout()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}