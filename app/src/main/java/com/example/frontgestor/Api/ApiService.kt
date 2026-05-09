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

    @GET("material/listar/{idEmpresa}")
    suspend fun listarMateriales(@Path("idEmpresa") id: Int): Response<List<MaterialDTO>>

    @GET("registroMaterial/listar/{idEmpresa}")
    suspend fun listarRegistrosMateriales(@Path("idEmpresa") id: Int): Response<List<RegistroMaterialDTO>>


    //busquedas por id
    @GET("empresa/buscar/{id}")
    suspend fun buscarEmpresa(@Path("id") id: Int): Response<EmpresaDTO>

    @GET("trabajador/buscar/{id}")
    suspend fun buscarTrabajador(@Path("id") id: Int): Response<TrabajadorDTO>

    @GET("trabajo/buscar/{id}")
    suspend fun buscarTrabajo(@Path("id") id: Int): Response<TrabajoDTO>

    @GET("registroTrabajo/buscarRegistroTrabajador/{idTrabajador}")
    suspend fun buscarRegistroTrabajador(@Path("idTrabajador") id: Int): Response<List<RegistroTrabajoDTO>>


    //editar o altas
    @POST("trabajador/editar")
    suspend fun editarTrabajador(@Body trabajador: TrabajadorDTO): Response<TrabajadorDTO>

    @POST("trabajador/alta")
    suspend fun crearTrabajador(@Body trabajador: TrabajadorDTO): Response<TrabajadorDTO>

    @POST("material/editar")
    suspend fun editarMaterial(@Body material: MaterialDTO): Response<MaterialDTO>

    @POST("material/alta")
    suspend fun crearMaterial(@Body material: MaterialDTO): Response<MaterialDTO>

    companion object {
        private var apiService: ApiService? = null

        private const val BASE_URL = "http://192.168.0.27:8096/res/"

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