package com.example.frontgestor.Api

import com.example.frontgestor.Modelos.EmpresaDTO
import com.example.frontgestor.Modelos.LoginDTO
import com.example.frontgestor.Modelos.TrabajadorDTO
import com.example.frontgestor.Modelos.TrabajadorListaDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("empresa/login")
    suspend fun loginEmpresa(@Body loginDto: LoginDTO): Response<EmpresaDTO>

    @POST("trabajador/login")
    suspend fun loginTrabajador(@Body loginDto: LoginDTO): Response<TrabajadorDTO>

    @GET("trabajador/listar/{idEmpresa}")
    suspend fun listarTrabajadores(@Path("idEmpresa") id: Int): Response<List<TrabajadorListaDTO>>

    @GET("empresa/buscar/{id}")
    suspend fun buscarEmpresa(@Path("id") id: Int): Response<EmpresaDTO>


    companion object {
        private var apiService: ApiService? = null

        private const val BASE_URL = "http://192.168.0.62:8096/res/"

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