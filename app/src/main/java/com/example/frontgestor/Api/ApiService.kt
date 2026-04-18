package com.example.frontgestor.Api

import com.example.frontgestor.Modelos.Trabajador
import com.example.frontgestor.modelosDTO.LoginDTO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // LOGIN TRABAJADOR
    @POST("trabajador/login")
    suspend fun loginTrabajador(@Body login: LoginDTO): Trabajador

    // LISTAR TRABAJADORES POR EMPRESA
    @GET("trabajador/listar/{idEmpresa}")
    suspend fun listarTrabajadores(@Path("idEmpresa") idEmpresa: Int): List<Trabajador>

    companion object {
        private var apiService: ApiService? = null

        // IMPORTANTE: Android NO usa localhost → usa 10.0.2.2
        private const val BASE_URL = "http://10.0.2.2:8096/res/"

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