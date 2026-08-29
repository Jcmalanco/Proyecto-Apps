package com.attor.app.data

/** Los 7 tipos de contenido que se pueden crear y leer en Attor. */
enum class WorkFormat(val displayName: String) {
    NOVEL("Novela"),
    MANGA("Manga"),
    MANHUA("Manhua"),
    BOOK("Libro"),
    COMIC("Cómic"),
    MUSIC("Música"),
    PODCAST("Podcast")
}

/** Representa una obra (novela, manga, cómic, álbum musical, podcast, etc.). */
data class Work(
    val id: String,
    val title: String,
    val author: String,
    val rating: Float,
    val format: WorkFormat,
    val synopsis: String = "",
    val readsLabel: String = "",
    val statusLabel: String = "En emisión",
    val coverUrl: String? = null
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
