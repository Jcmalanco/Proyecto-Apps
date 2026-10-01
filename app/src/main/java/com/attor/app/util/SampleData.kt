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

    /** Contenido de texto de cada capítulo (prototipo). */
    fun contentForChapter(chapterId: String): String = when (chapterId) {
        "c1" -> """
            El horno llevaba tres días sin encenderse.

            Kaelen observó la fragua desde la puerta del taller, con las manos metidas en los bolsillos del delantal manchado de hollín. El frío de la mañana se colaba por las rendijas de las tablas, pero dentro del taller aún quedaba el calor residual de la última forja.

            —¿Piensas quedarte ahí parado mucho tiempo? —preguntó una voz desde el fondo.

            Era Maestro Orin, el viejo herrero que le había enseñado todo lo que sabía. Su barba blanca estaba recogida en una trenza desordenada, y sus ojos grises brillaban con esa mezcla de cansancio y curiosidad que Kaelen conocía tan bien.

            —No sé si tengo fuerzas para encenderlo —respondió el joven.

            Orin se acercó y colocó una mano callosa sobre el hombro de Kaelen.

            —El fuego no espera a que estés listo. Tú tienes que estar listo para el fuego.

            Kaelen asintió lentamente. Se acercó al horno, tomó el fuelle y comenzó a soplar. Las chispas saltaron primero, tímidas, como si dudaran. Pero poco a poco, el carbón se encendió y las llamas crecieron, devorando la madera seca con un crepitar reconfortante.

            —Eso es —dijo Orin con una sonrisa—. Ahora, trabaja.

            Kaelen tomó el martillo y golpeó el metal al rojo vivo. Cada impacto resonaba en el taller como un latido. El sudor le caía por la frente, pero no se detenía. No podía.

            Porque aquella no era una pieza cualquiera. Era el encargo que podía cambiar su vida.
        """.trimIndent()

        "c2" -> """
            El fuego no se apaga solo. Algo lo alimenta.

            Kaelen despertó sobresaltado. El taller estaba en silencio, pero el horno seguía encendido. No era el resplandor normal del carbón: era una luz azulada, antinatural, que palpitaba como un corazón.

            —¿Orin? —llamó.

            No hubo respuesta.

            Se levantó de la tarima donde dormió y se acercó al horno. Las llamas azules se retorcían formando figuras que casi parecían... rostros. Kaelen frotó los ojos, pero la visión persistió.

            —No estás loco —dijo una voz.

            Se dio la vuelta. Una mujer estaba apoyada en la puerta del taller. Llevaba una capa oscura y sus ojos brillaban con el mismo tono azulado que el fuego.

            —¿Quién eres? —preguntó Kaelen, retrocediendo un paso.

            —Me llaman Sombra. Y tú, Kaelen, has despertado algo que debería haber permanecido dormido.

            El martillo que Kaelen sostenía comenzó a vibrar. El metal se calentó en sus manos, pero no quemaba. Las llamas azules se intensificaron, y por un instante, Kaelen vio algo en ellas: una ciudad antigua, en ruinas, con un cielo de fuego.

            —¿Qué es esto? —susurró.

            —Es el principio —respondió Sombra—. O el final. Depende de ti.
        """.trimIndent()

        else -> "Contenido no disponible para este capítulo."
    }

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
