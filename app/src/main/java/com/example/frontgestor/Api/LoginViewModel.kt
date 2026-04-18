package com.example.frontgestor.Api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontgestor.Modelos.LoginDTO
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var loading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val api = ApiService.getInstance()

    fun login(email: String, password: String, isEmpresa: Boolean, onSuccess: (Any) -> Unit) {
        viewModelScope.launch {
            try {
                loading = true
                errorMessage = null

                val loginDTO = LoginDTO(email, password)

                val result = if (isEmpresa) {
                    api.loginEmpresa(loginDTO)
                } else {
                    api.loginTrabajador(loginDTO)
                }

                onSuccess(result)

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                loading = false
            }
        }
    }
}
