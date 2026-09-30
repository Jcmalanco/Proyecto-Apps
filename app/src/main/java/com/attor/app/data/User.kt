package com.attor.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Usuario registrado en la app.
 * Se guarda localmente en Room (prototipo — en producción usaría Firebase Auth o backend).
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)
