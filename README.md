# Attor — Prototipo Android (Kotlin + XML)

Prototipo funcional de la app "Attor" para leer y crear novelas, mangas, manhuas,
libros, cómics, música y podcast, con **2 roles**: Invitado y Usuario.

Este ZIP es un **proyecto Gradle completo** (no solo recursos sueltos): tiene
`settings.gradle`, `build.gradle` raíz y de módulo, `AndroidManifest.xml`,
todos los layouts XML y todo el código Kotlin necesario para correr en un
emulador/dispositivo directamente desde Android Studio.

## Cómo abrirlo

1. Abre Android Studio → **Open** → selecciona la carpeta `AttorApp`.
2. Espera a que Gradle sincronice (necesita conexión a internet la primera
   vez, para descargar AGP 8.5.0, Kotlin 1.9.24 y las librerías de AndroidX/Material).
3. Ejecuta el módulo `app` en un emulador (API 24+).

No hay backend: todos los datos (`SampleData.kt`) son de ejemplo, pensados
para que el prototipo se vea y se sienta completo sin necesidad de un servidor.

## Estructura del código

```
app/src/main/java/com/attor/app/
├── data/
│   ├── Models.kt          -> Work, Chapter, NotificationItem, WorkFormat (7 formatos)
│   └── SessionManager.kt  -> guarda el rol activo (INVITADO / USUARIO) en SharedPreferences
├── util/
│   ├── SampleData.kt      -> datos de ejemplo (obras, capítulos, notificaciones)
│   ├── WorkAdapter.kt     -> RecyclerView.Adapter reutilizable (home, search, library)
│   ├── ChapterAdapter.kt  -> índice de capítulos en work-details
│   └── NotificationAdapter.kt
└── ui/
    ├── login/LoginActivity.kt
    ├── main/MainActivity.kt          -> contenedor + bottom nav
    ├── home/HomeFragment.kt
    ├── search/SearchFragment.kt
    ├── library/LibraryFragment.kt
    ├── notifications/NotificationsFragment.kt
    ├── create/CreateWorkFragment.kt
    └── workdetails/WorkDetailsActivity.kt
```

## Cómo funcionan los 2 roles

Toda la lógica de roles vive en **`SessionManager`** (`data/SessionManager.kt`),
que guarda un flag `logged_in` en `SharedPreferences`:

- `session.isGuest()` → `true` si nadie inició sesión.
- `session.isLoggedIn()` → `true` si hay una cuenta activa.
- `session.login(nombre)` → guarda sesión de usuario.
- `session.loginAsGuest()` → entra sin cuenta.
- `session.logout()` → borra la sesión.

### Puntos donde se aplica el rol

| Archivo | Qué hace con el rol |
|---|---|
| `LoginActivity` | El botón **"Continuar como invitado"** llama a `loginAsGuest()` y entra directo a `MainActivity`, sin pedir credenciales. |
| `MainActivity` | Oculta `nav_create` y `nav_notifications` del bottom nav si `session.isGuest()`. |
| `HomeFragment` | Oculta la sección "Sigue leyendo" para invitados (no tienen historial). |
| `LibraryFragment` | Si es invitado, muestra `layoutGuestLock` (mensaje + botón "Iniciar Sesión") en vez del contenido. |
| `NotificationsFragment` | Guarda extra: si un invitado llega aquí por cualquier vía, se le redirige a `LoginActivity`. |
| `CreateWorkFragment` | Guarda extra: si un invitado llega aquí, se muestra un `AlertDialog` obligatorio pidiendo iniciar sesión antes de continuar. |

Esto significa que aunque el ítem del menú esté oculto, cada pantalla sensible
también se protege a sí misma (defensa en profundidad), útil si más adelante
agregas navegación por deep links o notificaciones push.

## Crear obra: un formulario para 7 formatos

`CreateWorkFragment` usa los chips de `fragment_create_work.xml`
(`formatNovel`, `formatManga`, `formatManhua`, `formatComic`, `formatBook`,
`formatMusic`, `formatPodcast`) para seleccionar el `WorkFormat` antes de
publicar. Al pulsar "Publicar Obra" valida que haya título y sinopsis, y
simula la publicación con un `Toast` (reemplázalo por tu llamada real al
backend cuando lo tengas).

Los géneros (`ChipGroup`) están limitados a 3 selecciones máximo; si el
usuario intenta marcar un cuarto, se desmarca automáticamente y se le avisa.

## Siguientes pasos sugeridos

- Conectar `LoginActivity` y `CreateWorkFragment` a un backend real (Retrofit/Firebase).
- Reemplazar `SampleData` por llamadas a API, y las portadas (`ImageView`) por
  carga real de imágenes (Glide/Coil).
- Añadir una pantalla de "Lector" real al pulsar "Empezar a Leer" o un capítulo.
- Agregar persistencia de biblioteca/favoritos por usuario (Room o backend).
- Reemplazar los íconos placeholder (`@android:drawable/...`) por Material Icons.
