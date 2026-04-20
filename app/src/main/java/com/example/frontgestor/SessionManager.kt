package com.example.frontgestor

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(empresa : Int ,id: Int , tipo: String) {
        prefs.edit()
            .putInt("id", id)
            .putInt("idEmpresa" , empresa)
            .putString("tipo", tipo)
            .apply()
    }

    fun isLogged(): Boolean = prefs.contains("id")

    fun getUserId(): Int = prefs.getInt("id", -1)

    fun getEmpresaId(): Int = prefs.getInt("idEmpresa", -1)

    fun getTipo(): String? = prefs.getString("tipo", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
