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

## Qué leer primero
- **`CHANGELOG.md`** en esta misma carpeta: documenta cada cambio agregado en
  esta versión, archivo por archivo, útil para la documentación del proyecto.

## Estructura del código
```
app/src/main/java/com/attor/app/
├── AttorApplication.kt        -> aplica el tema guardado al iniciar
├── data/
│   ├── Models.kt               -> Work (entidad Room), Chapter, NotificationItem
│   ├── SessionManager.kt       -> sesión (invitado/usuario), persiste sola
│   ├── SettingsManager.kt      -> tema + tamaño de letra
│   ├── WorkRepository.kt       -> capa de acceso a datos (CRUD)
│   └── local/                  -> Room: AppDatabase, WorkDao, Converters
├── util/                        -> adaptadores, SampleData, GridSpacingItemDecoration
└── ui/
    ├── common/BaseActivity.kt  -> aplica tamaño de letra a toda Activity
    ├── login/LoginActivity.kt
    ├── main/MainActivity.kt    -> bottom nav (colores invertidos al seleccionar)
    ├── home/HomeFragment.kt    -> carruseles por categoría + Ver todo
    ├── search/SearchFragment.kt
    ├── library/LibraryFragment.kt
    ├── notifications/NotificationsFragment.kt
    ├── create/CreateWorkFragment.kt  -> ahora guarda en Room de verdad
    ├── workdetails/WorkDetailsActivity.kt
    ├── profile/ProfileActivity.kt    -> NUEVO
    ├── settings/SettingsActivity.kt  -> NUEVO
    └── seeall/SeeAllActivity.kt      -> NUEVO
```

## Siguientes pasos sugeridos
- Conectar a un backend real (Retrofit/Firebase) en vez de solo Room local.
- Selector de imagen real para portadas (ahora mismo todo usa `placeholder_cover`).
- Pantalla de lector real al tocar "Empezar a Leer" o un capítulo.
