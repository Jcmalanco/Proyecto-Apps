# ATTOR

Plataforma editorial que reúne distintos formatos de narrativa — novelas,
mangas, manhuas, cómics, libros, canciones, podcast y arte — en un solo
lugar, pensada tanto para lectores como para autores independientes.

## Descripción

Hoy en día, seguir una historia significa saltar entre varias apps distintas
según el formato: una para novelas, otra para cómics, otra para música o
podcast. Attor resuelve esto reuniendo los 8 formatos en una sola
aplicación, con un solo perfil y una sola biblioteca.

La app tiene dos roles: **invitado**, que puede explorar y leer todo el
catálogo sin necesidad de registrarse, y **usuario registrado**, que además
puede publicar sus propias obras, guardar su biblioteca y recibir
notificaciones de los autores que sigue.

## Características

- Modo invitado: leer sin necesidad de crear una cuenta.
- Publicación de obras propias en 8 formatos distintos, con portada, género e idioma.
- Descubrimiento de contenido por categorías, organizado en carruseles.
- Biblioteca personal y notificaciones (exclusivo de usuarios registrados).
- Perfil de usuario con configuración de tema claro/oscuro y tamaño de letra.
- Persistencia local de datos: lo que se publica o configura no se pierde al cerrar la app.

## Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **Entorno:** Android Studio (Gradle 8.5.0 + Android Gradle Plugin 8.5.0)
- **Base de datos local:** Room, con KSP para la generación de código
- **Interfaz:** Material Components, ConstraintLayout, RecyclerView, CardView
- **Asincronía:** Kotlin Coroutines
- **Persistencia de sesión y configuración:** SharedPreferences

## Requisitos

Antes de ejecutar el proyecto necesitas:

- Android Studio (versión reciente)
- JDK 17
- Un emulador o dispositivo Android con API 24 (Android 7.0) o superior
- Conexión a internet la primera vez, para que Gradle descargue las dependencias

## Instalación

1. Clonar el repositorio:

```bash
[git clone URL_DEL_REPOSITORIO](https://github.com/Jcmalanco/Proyecto-Apps.git)
```

2. Abrir el proyecto en Android Studio: `File → Open` y seleccionar la
   carpeta clonada.
3. Esperar a que Gradle sincronice automáticamente.
4. Ejecutar el módulo `app` en un emulador o dispositivo físico.
