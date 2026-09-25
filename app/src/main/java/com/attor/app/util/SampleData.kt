package com.attor.app.util

import com.attor.app.data.Chapter
import com.attor.app.data.NotificationItem
import com.attor.app.data.Work
import com.attor.app.data.WorkFormat

/** Catálogo de ejemplo: se usa una sola vez para sembrar la base de datos Room. */
object SampleData {

    fun allWorks(): List<Work> = listOf(
        Work("w1", "El Herrero de Almas", "Kaelen Voss", 4.9f, WorkFormat.NOVEL),
        Work("w2", "El Último Verso", "Mika Rún", 4.5f, WorkFormat.NOVEL),
        Work("w3", "Neo-Sapiens", "Val Vento", 4.7f, WorkFormat.MANGA),
        Work("w4", "Gravewines", "Sombra de Ganza", 4.3f, WorkFormat.MANGA),
        Work("w5", "Despertar del Fuego", "Attaca Boto", 4.4f, WorkFormat.MANHUA),
        Work("w6", "Génesis Cero", "Aria Nova", 4.6f, WorkFormat.COMIC),
        Work("w7", "Café & Neón", "Toa Cielo", 4.7f, WorkFormat.COMIC),
        Work("w8", "Trayectos", "Elin Marsh", 4.2f, WorkFormat.BOOK),
        Work("w9", "Cosmic", "El Curso del Éter", 4.4f, WorkFormat.MUSIC),
        Work("w10", "Ecos Nocturnos", "Ren Alva", 4.1f, WorkFormat.MUSIC),
        Work("w11", "Voces del Barrio", "Studio Attor", 4.0f, WorkFormat.PODCAST),
        Work("w12", "Retratos Digitales", "Nia Cruz", 4.8f, WorkFormat.ART)
    )

    fun chaptersFor(workId: String): List<Chapter> = listOf(
        Chapter("c1", "Capítulo 1: El Herrero Gris", "Publicado hace 3 días"),
        Chapter("c2", "Capítulo 2: Despertar del Fuego", "Publicado hace 1 día")
    )

    val notifications = listOf(
        NotificationItem("n1", "Yeli Soto subió un nuevo capítulo de El Herrero de Almas"),
        NotificationItem("n2", "Liam Carter comenzó a seguir tu perfil"),
        NotificationItem("n3", "Attaca Boto te encontró la trama de Génesis Cero"),
        NotificationItem("n4", "Alena Ross comentó en tu obra Despertar del Fuego")
    )

    fun synopsisFor(work: Work): String = work.synopsis.ifBlank {
        "En un mundo donde las almas pueden forjarse como metal, un joven aprendiz " +
            "descubre un secreto que podría cambiar el destino de todos los reinos."
    }
}
