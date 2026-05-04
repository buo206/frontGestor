package com.example.frontgestor.Api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontgestor.Modelos.EmpresaDTO
import com.example.frontgestor.Modelos.LoginDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    var cargando by mutableStateOf(false)
    var mensageError by mutableStateOf<String?>(null)

    private val api = ApiService.getInstance()

    var empresa by mutableStateOf<EmpresaDTO?>(null)
        private set

    var trabajador by mutableStateOf<TrabajadorDTO?>(null)
        private set


    fun login(email: String, password: String, isEmpresa: Boolean, onSuccess: (Any) -> Unit) {
        viewModelScope.launch {
            try {
                cargando = true
                mensageError = null

                val loginDTO = LoginDTO(email, password)

                val result = if (isEmpresa) {
                    val res = api.loginEmpresa(loginDTO)
                    empresa = res
                    res
                } else {
                    val res = api.loginTrabajador(loginDTO)
                    trabajador = res
                    res
                }

                onSuccess(result)

            } catch (e: HttpException) {
                if(e.code()== 400){
                    mensageError = "Contraseña o email incorrecto"
                }
            } finally {
                cargando = false
            }
        }
    }
}
