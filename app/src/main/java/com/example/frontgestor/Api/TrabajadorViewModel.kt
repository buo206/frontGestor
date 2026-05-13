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
import com.example.frontgestor.Modelos.TrabajoDTO
import com.example.frontgestor.Modelos.TrabajoListaDTO
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class TrabajadorViewModel : ViewModel() {

    var cargando by mutableStateOf(false)
    var mensageError by mutableStateOf<String?>(null)

    private val api = ApiService.getInstance()

    var trabajador by mutableStateOf<TrabajadorDTO?>(null)
        private set

    var trabajos by mutableStateOf<List<TrabajoListaDTO>?>(null)
        private set

    var trabajoBuscado by mutableStateOf<TrabajoDTO?>(null)
        private set

    var registrosMateriales by mutableStateOf<List<RegistroMaterialDTO>?>(null)
        private set

    var registroMaterialBuscado by mutableStateOf<RegistroMaterialDTO?>(null)
        private set

    var materiales by mutableStateOf<List<MaterialDTO>?>(null)
        private set

    //buscar
    fun buscarTrabajador(id : Int ){
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarTrabajador(id)
                if(result.isSuccessful){
                    trabajador = result.body()
                }else{
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."

            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }

            cargando = false
        }
    }

    fun listarTrabajos(id : Int ){
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.listarTrabajosPorTrabjador(id)
                if(result.isSuccessful){
                    trabajos = result.body()
                }else{
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."

            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }

            cargando = false
        }
    }


    fun listarMateriales(empresaId: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.listarMateriales(empresaId)
                if (result.isSuccessful) {
                    materiales = result.body()
                } else {
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."
            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }
            cargando = false
        }
    }

    fun buscarTrabajo(id : Int ){
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarTrabajo(id)
                if(result.isSuccessful){
                    trabajoBuscado = result.body()
                }else{
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."

            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }

            cargando = false
        }
    }

    //registro de materiales por trabajo
    fun buscarRegistroMaterialPorTrabajo(id: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarRegistrosMaterialesPorTrabajo(id)
                if (result.isSuccessful) {
                    registrosMateriales = result.body()
                } else {
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."
            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }
            cargando = false
        }
    }


    fun buscarRegistroMaterialPorTodo(idTrabajador : Int , idTrabajo : Int , idMaterial : Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarRegistroMaterialTodo(idTrabajador, idTrabajo, idMaterial)
                if (result.isSuccessful) {
                    registroMaterialBuscado = result.body()
                } else {
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError =
                    "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."
            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }

        }
    }


    //metodos de ayuda
    fun limpiarErrorMensage(){
        if(cargando == false){
            mensageError = null ;
        }
    }

    fun limpiarRegistroMaterialBuscado(){
        if(cargando == false){
            registroMaterialBuscado = null ;
        }
    }

    fun limpiarListaMateriales(){
        if(cargando == false){
            registrosMateriales = null ;
        }
    }

    fun setRegistroMaterial(registro : RegistroMaterialDTO){
        registroMaterialBuscado = registro
    }


    //editar y crear

    fun editarTrabajo(trabajo: TrabajoDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.editarTrabajo(trabajo)
                if (result.isSuccessful) {
                    trabajoBuscado = result.body()
                } else {
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."
            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }
            cargando = false
        }
    }

    fun editarRegistroMaterial(registro: RegistroMaterialDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.editarRegistroMaterial(registro)
                if (result.isSuccessful) {
                    registroMaterialBuscado = result.body()
                } else {
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."
            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }
            cargando = false
        }
    }

    fun crearRegistroMaterial(registro: RegistroMaterialDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.crearRegistroMaterial(registro)
                if (result.isSuccessful) {
                    registroMaterialBuscado = result.body()
                } else {
                    mensageError = obtenerMensajeError(result)
                }
            } catch (e: IOException) {
                mensageError = "No hay conexión con el servidor. Revisa tu internet o inténtalo más tarde."
            } catch (e: HttpException) {
                mensageError = "Error de conexión con el servidor."
            } catch (e: Exception) {
                mensageError = "Ha ocurrido un error inesperado."
            }
            cargando = false
        }
    }
}





private fun obtenerMensajeError(response: Response<*>): String {
    val errorBody = response.errorBody()?.string()
        ?: return "Error desconocido"

    return try {
        val json = JSONObject(errorBody)

        json.optString("message")
            .ifBlank { json.optString("detail") }
            .ifBlank { json.optString("error") }
            .ifBlank { errorBody }
    } catch (e: Exception) {
        errorBody
    }
}