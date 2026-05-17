package com.example.frontgestor.Api

import com.example.frontgestor.Modelos.EmpresaDTO
import com.example.frontgestor.Modelos.LoginDTO
import com.example.frontgestor.Modelos.MaterialDTO
import com.example.frontgestor.Modelos.RegistroMaterialDTO
import com.example.frontgestor.Modelos.RegistroTrabajoDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import com.example.frontgestor.Modelos.TrabajoDTO
import com.example.frontgestor.Modelos.TrabajoListaDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    //logins
    @POST("empresa/login")
    suspend fun loginEmpresa(@Body loginDto: LoginDTO): Response<EmpresaDTO>

    @POST("trabajador/login")
    suspend fun loginTrabajador(@Body loginDto: LoginDTO): Response<TrabajadorDTO>


    //listas
    @GET("trabajador/listar/{idEmpresa}")
    suspend fun listarTrabajadores(@Path("idEmpresa") id: Int): Response<List<TrabajadorListaDTO>>

    @GET("trabajo/listar/{idEmpresa}")
    suspend fun listarTrabajos(@Path("idEmpresa") id: Int): Response<List<TrabajoListaDTO>>

    @GET("trabajo/listarTrabajador/{idTrabjador}")
    suspend fun listarTrabajosPorTrabjador(@Path("idTrabjador") id: Int): Response<List<TrabajoListaDTO>>

    @GET("material/listar/{idEmpresa}")
    suspend fun listarMateriales(@Path("idEmpresa") id: Int): Response<List<MaterialDTO>>

    @GET("registroMaterial/listar/{idEmpresa}")
    suspend fun listarRegistrosMateriales(@Path("idEmpresa") id: Int): Response<List<RegistroMaterialDTO>>

    @GET("registroMaterial/buscarRegistroTrabajo/{idTrabajo}")
    suspend fun buscarRegistrosMaterialesPorTrabajo(@Path("idTrabajo") id: Int): Response<List<RegistroMaterialDTO>>

    @GET("registroTrabajo/buscarRegistroTrabajo/{idTrabajo}")
    suspend fun buscarRegistrosPorTrabajo(@Path("idTrabajo") id: Int): Response<List<RegistroTrabajoDTO>>


    //busquedas por id
    @GET("empresa/buscar/{id}")
    suspend fun buscarEmpresa(@Path("id") id: Int): Response<EmpresaDTO>

    @GET("trabajador/buscar/{id}")
    suspend fun buscarTrabajador(@Path("id") id: Int): Response<TrabajadorDTO>

    @GET("trabajo/buscar/{id}")
    suspend fun buscarTrabajo(@Path("id") id: Int): Response<TrabajoDTO>

    @GET("registroTrabajo/buscarRegistroTrabajador/{idTrabajador}")
    suspend fun buscarRegistroTrabajador(@Path("idTrabajador") id: Int): Response<List<RegistroTrabajoDTO>>

    @GET("registroTrabajo/buscar/{idTrabajo}/{idTrabajador}")
    suspend fun buscarRegistroTrabajador(@Path("idTrabajo"  ) idTrabajo: Int , @Path("idTrabajador"  ) idTrabajador: Int ): Response<RegistroTrabajoDTO>

    @GET("registroMaterial/buscarRegistroTodo/{idTrabajador}/{idTrabajo}/{idMaterial}")
    suspend fun buscarRegistroMaterialTodo(@Path("idTrabajador"  ) idTrabajador: Int ,@Path("idTrabajo"  ) idTrabajo: Int  ,@Path("idMaterial"  ) idMaterial: Int ): Response<RegistroMaterialDTO>


    //editar o altas
    @POST("trabajador/editar")
    suspend fun editarTrabajador(@Body trabajador: TrabajadorDTO): Response<TrabajadorDTO>

    @POST("trabajador/alta")
    suspend fun crearTrabajador(@Body trabajador: TrabajadorDTO): Response<TrabajadorDTO>

    @POST("material/editar")
    suspend fun editarMaterial(@Body material: MaterialDTO): Response<MaterialDTO>

    @POST("material/alta")
    suspend fun crearMaterial(@Body material: MaterialDTO): Response<MaterialDTO>


    @POST("registroTrabajo/editar")
    suspend fun editarRegistroTrabajo(@Body registoTrabajoDTO: RegistroTrabajoDTO): Response<RegistroTrabajoDTO>

    @POST("registroTrabajo/alta")
    suspend fun crearRegistroTrabajo(@Body registoTrabajoDTO: RegistroTrabajoDTO): Response<RegistroTrabajoDTO>

    @POST("registroMaterial/editar")
    suspend fun editarRegistroMaterial(@Body registoMaterialDTO: RegistroMaterialDTO): Response<RegistroMaterialDTO>

    @POST("registroMaterial/alta")
    suspend fun crearRegistroMaterial(@Body registoMaterialDTO: RegistroMaterialDTO): Response<RegistroMaterialDTO>


    @POST("trabajo/editar")
    suspend fun editarTrabajo(@Body trabajo: TrabajoDTO): Response<TrabajoDTO>

    @POST("empresa/editar")
    suspend fun editarEmpresa(@Body empresa: EmpresaDTO): Response<EmpresaDTO>

    @POST("trabajo/alta")
    suspend fun crearTrabajo(@Body trabajo: TrabajoDTO): Response<TrabajoDTO>

    //eliminar

    @POST("trabajo/eliminar/{idTrabajo}")
    suspend fun eliminarTrabajo(@Path("idTrabajo"  ) idTrabajo: Int): Response<Boolean>

    @POST("registroTrabajo/eliminar/{idTrabajo}/{idTrabajador}")
    suspend fun eliminarRegistroTrabajo(@Path("idTrabajo"  ) idTrabajo: Int , @Path("idTrabajador"  ) idTrabajador: Int): Response<Boolean>

    @POST("registroMaterial/eliminar/{idRegistro}")
    suspend fun eliminarRegistroMaterial(@Path("idRegistro"  ) idRegistro: Int  ): Response<Boolean>

    companion object {
        private var apiService: ApiService? = null

        private const val BASE_URL = "http://192.168.0.78:8096/res/"

        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}