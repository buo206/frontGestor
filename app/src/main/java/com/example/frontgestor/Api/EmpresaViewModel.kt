package com.example.frontgestor.Api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontgestor.Modelos.EmpresaDTO
import com.example.frontgestor.Modelos.MaterialDTO
import com.example.frontgestor.Modelos.RegistroMaterialDTO
import com.example.frontgestor.Modelos.RegistroTrabajoDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import com.example.frontgestor.Modelos.TrabajoDTO
import com.example.frontgestor.Modelos.TrabajoListaDTO
import kotlinx.coroutines.launch

class EmpresaViewModel : ViewModel() {
    var cargando by mutableStateOf(false)
    var mensageError by mutableStateOf<String?>(null)

    private val api = ApiService.getInstance()

    var empresa by mutableStateOf<EmpresaDTO?>(null)
        private set

    var trabajadores by mutableStateOf<List<TrabajadorListaDTO>?>(null)
        private set

    var materiales by mutableStateOf<List<MaterialDTO>?>(null)
        private set

    var registrosMateriales by mutableStateOf<List<RegistroMaterialDTO>?>(null)
        private set

    var materialBuscado by mutableStateOf<MaterialDTO?>(null)

    var trabajadorBuscado by mutableStateOf<TrabajadorDTO?>(null)
        private set

    var trabajos by mutableStateOf<List<TrabajoListaDTO>?>(null)
        private set
    var trabajoBuscado by mutableStateOf<TrabajoDTO?>(null)
        private set

    //registros de un trabajador en tareas
    var registrosTrabajador by mutableStateOf<List<RegistroTrabajoDTO>?>(null)
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

    fun buscarRegistroTrabajador(id : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.buscarRegistroTrabajador(id)
            if(result.isSuccessful){
                registrosTrabajador = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe un trabajador con un retgistro"
                }
            }
            cargando = false
        }
    }
    fun buscarTrabajador(id : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.buscarTrabajador(id)
            if(result.isSuccessful){
                trabajadorBuscado = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una un trabajador con esta id"
                }
            }
            cargando = false
        }
    }
    fun buscarTrabajo(id : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.buscarTrabajo(id)
            if(result.isSuccessful){
                trabajoBuscado = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una un trabajador con esta id"
                }
            }
            cargando = false
        }
    }

    fun limpiarTrabajador(){
        if(cargando == false ){
            trabajadorBuscado = null
        }
    }
    fun limpirarMaterial(){
        if(cargando == true){
            materialBuscado = null
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

    fun listarTrabajos(empresaId : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.listarTrabajos(empresaId)
            if(result.isSuccessful){
                trabajos = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una empresa con esta id"
                }
            }
            cargando = false
        }
    }

    fun listarMateriales(empresaId : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.listarMateriales(empresaId)
            if(result.isSuccessful){
                materiales = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una empresa con esta id"
                }
            }
            cargando = false
        }
    }

    fun listarRegistrosMateriales(empresaId : Int){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.listarRegistrosMateriales(empresaId)
            if(result.isSuccessful){
                registrosMateriales = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "No existe una empresa con esta id"
                }
            }
            cargando = false
        }
    }

    fun editarTrabajador(trabajdor : TrabajadorDTO){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.editarTrabajador(trabajdor)
            if(result.isSuccessful){
                trabajadorBuscado = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "Algun campo ha rellenado de forma incorrecta"
                }
            }
            cargando = false
        }
    }

    fun editarMaterial(material : MaterialDTO){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.editarMaterial(material)
            if(result.isSuccessful){
                materialBuscado = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "Algun campo ha rellenado de forma incorrecta"
                }
            }
            cargando = false
        }
    }

    fun buscarMaterial(material : MaterialDTO){
        materialBuscado = material
    }

    fun crearTrabajador(trabajdor : TrabajadorDTO){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.crearTrabajador(trabajdor)
            if(result.isSuccessful){
                trabajadorBuscado = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "Algun campo ha rellenado de forma incorrecta"
                }
            }
            cargando = false
        }
    }

    fun crearMaterial(material : MaterialDTO){
        viewModelScope.launch {
            cargando = true
            mensageError = null

            var result = api.crearMaterial(material)
            if(result.isSuccessful){
                materialBuscado = result.body()
            }else{
                if(result.code()== 400){
                    mensageError = "Algun campo ha rellenado de forma incorrecta o ya exite un material con ese titulo"
                }
            }
            cargando = false
        }
    }
}