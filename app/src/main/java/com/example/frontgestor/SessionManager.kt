package com.example.frontgestor

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(empresaId: Int, userId: Int, tipo: String) {
        prefs.edit().apply {
            putInt("id", userId)
            putInt("idEmpresa", empresaId)
            putString("tipo", tipo)
            apply()
        }
    }

    fun isLogged(): Boolean = prefs.contains("id")

    fun getUserId(): Int = prefs.getInt("id", -1)

    fun getEmpresaId(): Int = prefs.getInt("idEmpresa", 0)

    fun getTipo(): String? = prefs.getString("tipo", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
