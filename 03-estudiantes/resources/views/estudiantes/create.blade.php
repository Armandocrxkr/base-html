@extends('base')
@section('title', 'Nuevo - Registro de Estudiantes')
@section('header', 'Registro de Estudiantes')
@section('content')
<div class="container">
    <h2>Registrar</h2>
    <form method="POST" action="{{ route('estudiantes.store') }}">
        @csrf
        <div class="form-group"><label>NÃºmero de cuenta</label><input type="text" name="numero_cuenta" value="{{ old('numero_cuenta') }}">@error('numero_cuenta')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Nombre completo</label><input type="text" name="nombre" value="{{ old('nombre') }}">@error('nombre')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Correo electrÃ³nico</label><input type="email" name="correo" value="{{ old('correo') }}">@error('correo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Fecha de nacimiento</label><input type="date" name="fecha_nacimiento" value="{{ old('fecha_nacimiento') }}">@error('fecha_nacimiento')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="{{ route('estudiantes.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection