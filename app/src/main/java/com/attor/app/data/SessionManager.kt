package com.attor.app.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Maneja la sesión del usuario y su rol (INVITADO o USUARIO).
 * Usa SharedPreferences, que persiste en disco automáticamente: por eso la
 * sesión se mantiene aunque cierres y vuelvas a abrir la app.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isGuest(): Boolean = !prefs.getBoolean(KEY_LOGGED_IN, false)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "Invitado") ?: "Invitado"

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "invitado@attor.app") ?: ""

    /** Inicia sesión como USUARIO registrado. */
    fun login(userName: String, email: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_USER_NAME, userName)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    /** Entra como INVITADO (sin cuenta, solo lectura). */
    fun loginAsGuest() {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, false)
            .putString(KEY_USER_NAME, "Invitado")
            .apply()
    }

    /** Cierra sesión y vuelve al estado de invitado. */
    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "attor_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
    }
}
