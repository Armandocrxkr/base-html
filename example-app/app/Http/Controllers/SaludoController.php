<?php
namespace App\Http\Controllers;
class SaludoController extends Controller
{
public function index()
{
$mensaje = 'Hola mundo desde Laravel';
$nombre = 'Ana';
$asignatura = 'ISC-305 Programación Web';
return view('hola', [
'mensaje' => $mensaje,
'nombre' => $nombre,
'asignatura' => $asignatura
]);
}
}