# Gestión de Usuarios App

## ¿Qué hace esta aplicación?
Es una aplicación móvil básica (CRUD) que permite administrar un registro de personas guardando la información localmente en el dispositivo. 

Sus funciones principales son:
* **Crear:** Agregar nuevos usuarios con sus datos personales (Nombre, Apellido, Edad, Género, Teléfono y Correo).
* **Leer:** Mostrar una lista de todos los usuarios registrados.
* **Actualizar:** Editar la información de cualquier usuario existente.
* **Eliminar:** Borrar usuarios de la base de datos.
* ## Splash Screen
La aplicación incluye una pantalla de carga inicial (Splash Screen) construida de forma nativa con Jetpack Compose. Muestra el ícono de la app centrado durante 3 segundos (`delay(3000)`) antes de redirigir automáticamente a la pantalla principal.

## Tecnologías utilizadas
* **Lenguaje:** Kotlin
* **Interfaz de Usuario:** Jetpack Compose (Material Design 3)
* **Base de Datos:** SQLite (`SQLiteOpenHelper`)
* **Entorno:** Android Studio
