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
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

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

    var materialBuscado by mutableStateOf<MaterialDTO?>(null)
        private set

    var registrosMateriales by mutableStateOf<List<RegistroMaterialDTO>?>(null)
        private set

    var registroMaterialBuscado by mutableStateOf<RegistroMaterialDTO?>(null)

    var trabajadorBuscado by mutableStateOf<TrabajadorDTO?>(null)
        private set

    var trabajos by mutableStateOf<List<TrabajoListaDTO>?>(null)
        private set
    var trabajoBuscado by mutableStateOf<TrabajoDTO?>(null)
        private set

    //registros de un trabajador en tareas
    var registrosTrabajador by mutableStateOf<List<RegistroTrabajoDTO>?>(null)
        private set
    var registrosTrabajo by mutableStateOf<List<RegistroTrabajoDTO>?>(null)
        private set

    var registroTrabajobuscado by mutableStateOf<RegistroTrabajoDTO?>(null)
        private set

    //busquedas

    fun bucarEmpresa(id : Int ){
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarEmpresa(id)
                if(result.isSuccessful){
                    empresa = result.body()
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

    fun buscarRegistroTrabajador(id: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarRegistroTrabajador(id)
                if (result.isSuccessful) {
                    registrosTrabajador = result.body()
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

    fun buscarTrabajador(id: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarTrabajador(id)
                if (result.isSuccessful) {
                    trabajadorBuscado = result.body()
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

    fun buscarTrabajo(id: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarTrabajo(id)
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

    fun buscarRegistroTrabajo(idTrabajo: Int, idTrabajador: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarRegistroTrabajador(idTrabajo, idTrabajador)
                if (result.isSuccessful) {
                    registroTrabajobuscado = result.body()
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

    fun setRegistroTrabajo(registro : RegistroTrabajoDTO){
        registroTrabajobuscado = registro
    }
    fun setRegistroMaterial(registro : RegistroMaterialDTO){
        registroMaterialBuscado = registro
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

    fun buscarMaterial(material : MaterialDTO){
        materialBuscado = material
    }


    //limpiar variables
    fun limpiarTrabajador(){
        if(cargando == false ){
            trabajadorBuscado = null
        }
    }
    fun limpirarMaterial(){
        if(cargando == false){
            materialBuscado = null
        }
    }

    fun limpiarListaMateriales(){
        if(cargando == false){
            registrosMateriales = null
        }
    }

    fun limpiarRegistroTrabajoBuscado(){
        if(cargando == false){
            registroTrabajobuscado = null
        }
    }


    fun limpiarRegistroMaterialBuscado(){
        if(cargando == false){
            registroMaterialBuscado = null
        }
    }

    fun limpiarTrabajoBuscado(){
        if(cargando == false){
            trabajoBuscado = null
        }
    }

    fun limpiarErrorMensage(){
        if(cargando == false){
            mensageError = null
        }
    }

    //listar
    fun listarTrabajadores(empresaId: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.listarTrabajadores(empresaId)
                if (result.isSuccessful) {
                    trabajadores = result.body()
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

    fun listarTrabajos(empresaId: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.listarTrabajos(empresaId)
                if (result.isSuccessful) {
                    trabajos = result.body()
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

    fun listarRegistrosMateriales(empresaId: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.listarRegistrosMateriales(empresaId)
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

    fun listarRegistrosTrabajo(trabajoId: Int) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.buscarRegistrosPorTrabajo(trabajoId)
                if (result.isSuccessful) {
                    registrosTrabajo = result.body()
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


    //editar o altas
    fun editarTrabajador(trabajdor: TrabajadorDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.editarTrabajador(trabajdor)
                if (result.isSuccessful) {
                    trabajadorBuscado = result.body()
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

    fun editarMaterial(material: MaterialDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.editarMaterial(material)
                if (result.isSuccessful) {
                    materialBuscado = result.body()
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

    fun editarRegistroTrabajo(registro: RegistroTrabajoDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.editarRegistroTrabajo(registro)
                if (result.isSuccessful) {
                    registroTrabajobuscado = result.body()
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

    fun crearTrabajador(trabajdor: TrabajadorDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.crearTrabajador(trabajdor)
                if (result.isSuccessful) {
                    trabajadorBuscado = result.body()
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

    fun crearMaterial(material: MaterialDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.crearMaterial(material)
                if (result.isSuccessful) {
                    materialBuscado = result.body()
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

    fun crearRegistroTrabajo(registro: RegistroTrabajoDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.crearRegistroTrabajo(registro)
                if (result.isSuccessful) {
                    registroTrabajobuscado = result.body()
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

    fun crearTrabajo(trabajo: TrabajoDTO) {
        viewModelScope.launch {
            cargando = true
            mensageError = null
            try {
                var result = api.crearTrabajo(trabajo)
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