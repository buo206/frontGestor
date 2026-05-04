package com.example.frontgestor.Api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontgestor.Modelos.EmpresaDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EmpresaViewModel : ViewModel() {
    var cargando by mutableStateOf(false)
    var mensageError by mutableStateOf<String?>(null)

    private val api = ApiService.getInstance()

    var empresa by mutableStateOf<EmpresaDTO?>(null)
        private set

    fun bucarEmpresa(id : Int ){
        viewModelScope.launch {
            try {
                cargando = true
                mensageError = null

                var result = api.buscarEmpresa(id)
                empresa = result
            }catch (e: HttpException) {
                if(e.code()== 400){
                    mensageError = "No existe una empresa con esta id"
                }
            } finally {
                cargando = false
            }
        }
    }
}