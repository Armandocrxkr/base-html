# Registro de Estudiantes

AplicaciÃ³n web CRUD desarrollada en Laravel con autenticaciÃ³n de usuarios y persistencia en MySQL.

## InstalaciÃ³n y puesta en marcha

1. Instalar dependencias: `composer install`
2. Copiar archivo de entorno y generar clave: `cp .env.example .env` y `php artisan key:generate`
3. Configurar MySQL en `.env` (`DB_DATABASE=db_estudiantes`), crear la base de datos y migrar: `php artisan migrate --seed`
4. Ejecutar servidor local: `php artisan serve`

## Credenciales de acceso
* **Usuario:** `admin`
* **ContraseÃ±a:** `12345678`

## Estructura de datos
* **MigraciÃ³n:** Crea la tabla `users` (con campo Ãºnico `username`) y la tabla `estudiantes (id, numero_cuenta, nombre, correo, fecha_nacimiento, timestamps)`.
* **Factory:** Genera 10 estudiantes ficticios con nÃºmero de cuenta Ãºnico, nombre, correo Ãºnico y fecha de nacimiento.
* **Seeder:** Registra el usuario administrador (`admin` / `12345678`) y ejecuta la factory para poblar 10 registros iniciales.