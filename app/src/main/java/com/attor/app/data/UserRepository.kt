package com.attor.app.data

import android.content.Context
import com.attor.app.data.local.AppDatabase
import java.security.MessageDigest

/**
 * Repositorio de usuarios — maneja registro y autenticación local.
 * Las contraseñas se guardan hasheadas (SHA-256) para no almacenarlas en texto plano.
 */
class UserRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context).userDao()

    /** Registra un nuevo usuario. Retorna true si se creó, false si el email ya existe. */
    suspend fun register(name: String, email: String, password: String): Boolean {
        if (dao.countByEmail(email) > 0) return false
        val user = User(
            email = email,
            name = name,
            passwordHash = hashPassword(password)
        )
        dao.insertUser(user)
        return true
    }

    /** Autentica un usuario. Retorna el User si las credenciales son correctas, null si no. */
    suspend fun login(email: String, password: String): User? {
        val user = dao.getUserByEmail(email) ?: return null
        return if (user.passwordHash == hashPassword(password)) user else null
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
