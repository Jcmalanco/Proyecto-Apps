package com.attor.app.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Maneja la sesión del usuario y su rol (INVITADO o USUARIO).
 *
 * Esta es la única fuente de verdad sobre el rol activo. Cualquier pantalla
 * que necesite saber si el usuario actual puede crear contenido, ver su
 * biblioteca personal o recibir notificaciones debe consultar aquí.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** true si no hay una cuenta iniciada (rol INVITADO). */
    fun isGuest(): Boolean = !prefs.getBoolean(KEY_LOGGED_IN, false)

    /** true si hay una sesión de USUARIO activa. */
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "Invitado") ?: "Invitado"

    /** Inicia sesión como USUARIO registrado. */
    fun login(userName: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_USER_NAME, userName)
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
    }
}
