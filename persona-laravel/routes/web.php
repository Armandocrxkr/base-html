
<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\LoginController;
use App\Http\Controllers\PersonaController;

// Página principal
Route::get('/', function () {
    return redirect()->route('login');
});

// Mostrar formulario de login
Route::get('/login', [LoginController::class, 'create'])
    ->name('login');

// Procesar login
Route::post('/login', [LoginController::class, 'store'])
    ->middleware('throttle:6,1')
    ->name('login.store');

// Rutas protegidas
Route::middleware('auth')->group(function () {

    Route::get('/inicio', function () {
        return redirect()->route('personas.index');
    })->name('inicio');

    // Listado de personas
    Route::get('/personas', [PersonaController::class, 'index'])
        ->name('personas.index');

    // Cerrar sesión
    Route::post('/logout', [LoginController::class, 'destroy'])
        ->name('logout');

});
