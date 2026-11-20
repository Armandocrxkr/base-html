@extends('layouts.app')
@section('contenido')
<h1>Editar persona</h1>
<form method="POST"
    action="{{ route('personas.update', $persona->id) }}">
    @csrf
    @method('PUT')
    <label for="dni">DNI de 13 dígitos sin guiones</label>
    <input id="dni" name="dni" type="text" maxlength="13"
        pattern="[0-9]{13}"
        value="{{ old('dni', $persona->dni) }}" required>
    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" type="text" maxlength="100"
        value="{{ old('nombre', $persona->nombre) }}" required>
    <label for="fecha_nacimiento">Fecha de nacimiento</label>
    <input id="fecha_nacimiento" name="fecha_nacimiento" type="date"
        value="{{ old('fecha_nacimiento',
            $persona->fecha_nacimiento->format('Y-m-d')) }}"
        required>
    <button type="submit">Actualizar</button>
    <a href="{{ route('personas.index') }}">Cancelar</a>
</form>
@endsection