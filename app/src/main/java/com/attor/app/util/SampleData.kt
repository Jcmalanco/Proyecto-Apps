package com.attor.app.util

import com.attor.app.data.Chapter
import com.attor.app.data.NotificationItem
import com.attor.app.data.Work
import com.attor.app.data.WorkFormat

/** Fuente de datos falsa (mock) para que el prototipo funcione sin backend. */
object SampleData {

    val continueReading = listOf(
        Work("w1", "Neo-Sapiens", "Val Vento", 4.7f, WorkFormat.COMIC),
        Work("w2", "El Herrero de Almas", "Kaelen Voss", 4.9f, WorkFormat.NOVEL),
        Work("w3", "Café & Neón", "Toa Cielo", 4.7f, WorkFormat.COMIC)
    )

    val trending = listOf(
        Work("w2", "El Herrero de Almas", "Kaelen Voss", 4.9f, WorkFormat.NOVEL),
        Work("w4", "Génesis Cero", "Aria Nova", 4.6f, WorkFormat.COMIC),
        Work("w5", "El Último Verso", "Mika Rún", 4.5f, WorkFormat.BOOK),
        Work("w6", "Gravewines", "Sombra de Ganza", 4.3f, WorkFormat.MANGA),
        Work("w7", "Cosmic", "El Curso del Éter", 4.4f, WorkFormat.MUSIC)
    )

    val library = listOf(
        Work("w1", "El Herrero de Almas", "Kaelen Voss", 4.9f, WorkFormat.NOVEL),
        Work("w2", "Neo-Sapiens", "Val Vento", 4.7f, WorkFormat.COMIC),
        Work("w3", "Café & Neón", "Toa Cielo", 4.7f, WorkFormat.COMIC),
        Work("w4", "Génesis Cero", "Aria Nova", 4.6f, WorkFormat.COMIC)
    )

    fun allWorks(): List<Work> = (trending + continueReading + library).distinctBy { it.id }

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

    fun synopsisFor(work: Work): String =
        "En un mundo donde las almas pueden forjarse como metal, un joven aprendiz " +
            "descubre un secreto que podría cambiar el destino de todos los reinos."
}
