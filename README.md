# Attor — Versión completa (Kotlin + Room)

App de lectura y creación de novelas, mangas, manhuas, cómics, libros,
canciones, podcast y arte, con 2 roles (Invitado / Usuario), persistencia
local con Room, perfil de usuario, configuración de tema/tamaño de letra,
carruseles por categoría y animaciones.

## Cómo abrirlo
1. Android Studio → **Open** → selecciona la carpeta `AttorApp`.
2. Espera a que Gradle sincronice (descarga Room, coroutines, Material, etc.).
3. Ejecuta el módulo `app` en un emulador (API 24+).

No hay backend: `SampleData.kt` siembra la base de datos Room la primera vez
que se abre la app; desde ahí todo (incluido lo que publiques) vive en el
dispositivo.

## Funcionalidades implementadas

### Autenticación
- **Registro de usuarios** con email y contraseña (hash SHA-256)
- **Inicio de sesión** con validación de credenciales
- **Modo invitado** (solo lectura, sin crear contenido)
- **Persistencia de sesión** con SharedPreferences

### Obras
- **Crear obras** con título, sinopsis, formato y géneros
- **Ver obras** en Home, Búsqueda, Biblioteca y Perfil
- **Agregar capítulos** a obras propias (título + contenido)
- **Eliminar obras** desde el perfil con diálogo de confirmación
- **Sistema de likes/estrellas** con persistencia local

### Lector
- **Lector de capítulos** con contenido completo
- **Navegación anterior/siguiente** entre capítulos
- **Carga desde Room** (contenido real guardado)

### Notificaciones
- **Notificaciones automáticas** al publicar una obra
- **Swipe to dismiss** (deslizar a la izquierda para eliminar)
- **Persistencia en Room** (sobreviven reinicios)

### Perfil
- **Información del usuario** (nombre, email)
- **Obras publicadas** con grid de 2 columnas
- **Contador de obras**
- **Eliminar obras** con confirmación

## Estructura del código
```
app/src/main/java/com/attor/app/
├── AttorApplication.kt        -> aplica el tema guardado al iniciar
├── data/
│   ├── Models.kt               -> Work (entidad Room), Chapter, NotificationItem
│   ├── User.kt                 -> User (entidad Room) - NUEVO
│   ├── ChapterEntity.kt        -> ChapterEntity (entidad Room) - NUEVO
│   ├── NotificationEntity.kt   -> NotificationEntity (entidad Room) - NUEVO
│   ├── SessionManager.kt       -> sesión (invitado/usuario), persiste sola
│   ├── SettingsManager.kt      -> tema + tamaño de letra
│   ├── WorkRepository.kt       -> capa de acceso a datos (CRUD)
│   ├── UserRepository.kt       -> registro y login de usuarios - NUEVO
│   ├── ChapterRepository.kt    -> capítulos de obras - NUEVO
│   ├── NotificationRepository.kt -> notificaciones - NUEVO
│   └── local/                  -> Room: AppDatabase, WorkDao, UserDao, ChapterDao, NotificationDao, Converters
├── util/                        -> adaptadores, SampleData, GridSpacingItemDecoration
└── ui/
    ├── common/BaseActivity.kt  -> aplica tamaño de letra a toda Activity
    ├── login/LoginActivity.kt -> registro e inicio de sesión
    ├── main/MainActivity.kt    -> bottom nav (colores invertidos al seleccionar)
    ├── home/HomeFragment.kt    -> carruseles por categoría + Ver todo
    ├── search/SearchFragment.kt
    ├── library/LibraryFragment.kt
    ├── notifications/NotificationsFragment.kt -> swipe to dismiss - NUEVO
    ├── create/CreateWorkFragment.kt  -> crea obra + notificación - NUEVO
    ├── workdetails/WorkDetailsActivity.kt -> agregar capítulos - NUEVO
    ├── reader/ReaderActivity.kt -> lector de capítulos - NUEVO
    ├── profile/ProfileActivity.kt    -> obras del usuario + eliminar - NUEVO
    ├── settings/SettingsActivity.kt  -> NUEVO
    └── seeall/SeeAllActivity.kt      -> NUEVO
```

## Base de datos (Room)

### Tablas
| Tabla | Descripción |
|-------|-------------|
| `works` | Obras (catálogo + creadas por usuarios) |
| `users` | Usuarios registrados (email, nombre, contraseña hash) |
| `chapters` | Capítulos de las obras (workId, título, contenido) |
| `notifications` | Notificaciones del usuario (mensaje, fecha) |

### Versiones
- **v1**: Work
- **v2**: + User
- **v3**: + NotificationEntity
- **v4**: + ChapterEntity

## Siguientes pasos sugeridos
- Conectar a un backend real (Retrofit/Firebase) en vez de solo Room local.
- Selector de imagen real para portadas (ahora mismo todo usa `placeholder_cover`).
- Editar capítulos existentes.
- Marcar notificaciones como leídas.
- Sincronizar progreso de lectura entre dispositivos.
