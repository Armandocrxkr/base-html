# Proyecto `personas-laravel` — CRUD con Autenticación en Laravel

Aplicación web desarrollada en **Laravel** con persistencia en **MySQL** para la carrera de Ingeniería en Sistemas Computacionales (UNAH). Implementa autenticación de usuarios y el ciclo completo **CRUD** (Crear, Leer, Actualizar y Eliminar) sobre la entidad `Persona`.

---

## 1. Conceptos Fundamentales de Laravel

### Migración
Archivo de control de versiones para el esquema de la base de datos que permite crear o modificar tablas desde código PHP sin escribir SQL manual.
* **Ejemplo en el proyecto:** Las migraciones en `database/migrations/` definen las tablas `users` (credenciales de acceso) y `personas` (con los campos `id`, `dni`, `nombre`, `fecha_nacimiento`, `created_at` y `updated_at`), ejecutadas mediante `php artisan migrate`.

### Seeder
Clase encargada de poblar la base de datos con registros iniciales o de prueba de forma automatizada.
* **Ejemplo en el proyecto:** `DatabaseSeeder.php` (y `PersonaSeeder.php`) inserta el usuario inicial en `users` y registros ficticios iniciales en `personas` verificando primero que la tabla esté vacía (`Persona::count() === 0`), evitando duplicados si se vuelve a ejecutar `php artisan db:seed`.

### Factory
Plantilla que define reglas con la librería **Faker** para generar instancias y datos ficticios de prueba sin comprometer información real.
* **Ejemplo en el proyecto:** `UserFactory.php` y `PersonaFactory.php` en `database/factories/` generan datos ficticios (`fake()->name()`, fechas e identificadores) para pruebas de desarrollo.

### Modelo
Clase de **Eloquent ORM** que representa una tabla de MySQL y permite consultar, insertar, actualizar y eliminar registros usando métodos orientados a objetos.
* **Ejemplo en el proyecto:** `app/Models/Persona.php` y `app/Models/User.php`. En `PersonaController.php` se utiliza `Persona::all()` para listar, `Persona::create(...)` para guardar, `Persona::findOrFail($id)` para localizar un registro (o devolver `404` si no existe) y `$persona->delete()` para eliminarlo definitivamente de MySQL.  ### Blade Motor de plantillas nativo de Laravel que combina HTML con directivas (`@extends`, `@section`, `@forelse`, `@if`, `{{ }}`) y escapa automáticamente los datos impresos para evitar ataques XSS. * **Ejemplo en el proyecto:** `resources/views/personas/index.blade.php` recorre la colección `$personas` con `@forelse ($personas as$persona)` para renderizar las filas de la tabla e imprime los valores de forma segura con `{{ $persona->dni }}` y `{{ $persona->nombre }}`.

### Sesión
Mecanismo que conserva estado entre peticiones HTTP sin estado, utilizado tanto para mantener autenticado al usuario como para mostrar mensajes temporales (*flash data*).
* **Ejemplo en el proyecto:** Al iniciar sesión en `LoginController`, el middleware `auth` en `routes/web.php` protege todas las rutas `/personas`. Asimismo, en `PersonaController::destroy()`, `->with('exito', 'Persona eliminada correctamente.')` guarda un mensaje temporal en sesión que se muestra una sola vez en `index.blade.php` mediante `session('exito')`.

### CSRF (Cross-Site Request Forgery)
Mecanismo de seguridad que protege los formularios contra peticiones maliciosas enviadas desde sitios externos aprovechando una sesión activa, mediante un token único validado por el servidor.
* **Ejemplo en el proyecto:** En `resources/views/personas/index.blade.php`, cada formulario de eliminación incluye la directiva `@csrf`, que genera un `<input type="hidden" name="_token">` verificado por Laravel antes de ejecutar `destroy()`.

### Métodos HTTP (`GET`, `POST`, `PUT`, `DELETE`)

| Método | Función | Ejemplo en el proyecto |
| :--- | :--- | :--- |
| **`GET`** | Consultar datos y renderizar vistas sin modificar registros. | `Route::get('/personas', [PersonaController::class, 'index'])` muestra el listado; `Route::get('/personas/{id}/editar', ...)` carga el formulario de edición. |
| **`POST`** | Enviar datos nuevos al servidor o gestionar la sesión. | `Route::post('/personas', [PersonaController::class, 'store'])` guarda una nueva persona; `Route::post('/logout', ...)` cierra la sesión. |
| **`PUT`** | Actualizar un registro existente en el servidor. | `Route::put('/personas/{id}', [PersonaController::class, 'update'])`, enviado desde un formulario HTML con `method="POST"` y `@method('PUT')`. |
| **`DELETE`** | Eliminar un registro específico identificado por su ID. | `Route::delete('/personas/{id}', [PersonaController::class, 'destroy'])`, enviado desde `index.blade.php` con `method="POST"`, `@method('DELETE')` y confirmación previa en JavaScript. |

---

## 2. Flujo de una Petición en el Proyecto (Práctica en casa)

1. **Archivo que recibe la petición:**
   * Todas las solicitudes HTTP entran por `public/index.php` y son dirigidas por **`routes/web.php`**, pasando primero por los middlewares `auth` (sesión activa) y verificación CSRF, hasta llegar al método correspondiente en **`app/Http/Controllers/PersonaController.php`** (o `LoginController.php`).
2. **Archivo que consulta o modifica los datos:**
   * Los modelos Eloquent **`app/Models/Persona.php`** y **`app/Models/User.php`**, invocados desde el controlador, ejecutan las operaciones `SELECT`, `INSERT`, `UPDATE` y `DELETE` sobre la base de datos **MySQL**.
3. **Archivo que genera el HTML:**
   * Las plantillas Blade ubicadas en **`resources/views/personas/`** (`index.blade.php`, `create.blade.php`, `edit.blade.php`) y **`resources/views/auth/login.blade.php`** procesan los datos enviados por el controlador y devuelven el documento HTML final al navegador.