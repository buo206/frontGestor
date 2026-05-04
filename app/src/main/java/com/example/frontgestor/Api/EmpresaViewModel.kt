package com.example.frontgestor.Api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontgestor.Modelos.EmpresaDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EmpresaViewModel : ViewModel() {
    var cargando by mutableStateOf(false)
    var mensageError by mutableStateOf<String?>(null)

    private val api = ApiService.getInstance()

    var empresa by mutableStateOf<EmpresaDTO?>(null)
        private set

    var trabajadores by mutableStateOf<List<TrabajadorListaDTO>?>(null)
        private set

    fun bucarEmpresa(id : Int ){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.buscarEmpresa(id)
            if(result.isSuccessful){
                empresa = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una empresa con esta id"
                }
            }
            cargando = false
        }
    }

    fun listarTrabajadores(empresaId : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.listarTrabajadores(empresaId)
            if(result.isSuccessful){
                trabajadores = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una empresa con esta id"
                }
            }
            cargando = false
        }
    }
}