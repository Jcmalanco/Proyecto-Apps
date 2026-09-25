package com.attor.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Las 8 categorías de contenido que se pueden crear y leer en Attor. */
enum class WorkFormat(val displayNameRes: String) {
    NOVEL("Novelas"),
    MANGA("Mangas"),
    MANHUA("Manhuas"),
    COMIC("Cómics"),
    BOOK("Libros"),
    MUSIC("Canciones"),
    PODCAST("Podcast"),
    ART("Arte");

    val displayName: String get() = displayNameRes
}

/**
 * Representa una obra (novela, manga, cómic, canción, podcast, etc.).
 * Es tanto el modelo de dominio como la entidad de Room: se guarda tal cual
 * en la tabla "works" de la base de datos local.
 */
@Entity(tableName = "works")
data class Work(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val rating: Float,
    val format: WorkFormat,
    val synopsis: String = "",
    val readsLabel: String = "",
    val statusLabel: String = "En emisión",
    val coverUrl: String? = null,
    /** true si la publicó un usuario desde "Crear Obra" (no es del catálogo de ejemplo). */
    val isUserCreated: Boolean = false,
    /** Nombre del usuario dueño de la obra (para "Mis obras" en el Perfil). */
    val ownerName: String? = null
)

/** Un capítulo/episodio dentro del índice de una obra. */
data class Chapter(
    val id: String,
    val title: String,
    val dateLabel: String
)

/** Una notificación dentro del feed de actividad del usuario. */
data class NotificationItem(
    val id: String,
    val message: String
)
