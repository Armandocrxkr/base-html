@extends('base')
@section('title', 'Editar - Registro de Estudiantes')
@section('header', 'Registro de Estudiantes')
@section('content')
<div class="container">
    <h2>Editar Registro #{{ $estudiante->id }}</h2>
    <form method="POST" action="{{ route('estudiantes.update', $estudiante) }}">
        @csrf
        @method('PUT')
        <div class="form-group"><label>NÃºmero de cuenta</label><input type="text" name="numero_cuenta" value="{{ old('numero_cuenta', $estudiante->numero_cuenta) }}">@error('numero_cuenta')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Nombre completo</label><input type="text" name="nombre" value="{{ old('nombre', $estudiante->nombre) }}">@error('nombre')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Correo electrÃ³nico</label><input type="email" name="correo" value="{{ old('correo', $estudiante->correo) }}">@error('correo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Fecha de nacimiento</label><input type="date" name="fecha_nacimiento" value="{{ old('fecha_nacimiento', $estudiante->fecha_nacimiento) }}">@error('fecha_nacimiento')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="{{ route('estudiantes.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection