package com.example.frontgestor.Api

import com.example.frontgestor.Modelos.Empresa
import com.example.frontgestor.Modelos.Trabajador
import com.example.frontgestor.Modelos.LoginDTO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("trabajador/login")
    suspend fun loginTrabajador(@Body login: LoginDTO): Trabajador

    @GET("trabajador/listar/{idEmpresa}")
    suspend fun listarTrabajadores(@Path("idEmpresa") idEmpresa: Int): List<Trabajador>

    @POST("empresa/login")
    suspend fun loginEmpresa(@Body login: LoginDTO): Empresa

    companion object {
        private var apiService: ApiService? = null

        private const val BASE_URL = "http://192.168.102.20:8096/res/"

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