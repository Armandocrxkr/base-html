@extends('base')
@section('title', 'Editar - ClÃ­nica Veterinaria')
@section('header', 'ClÃ­nica Veterinaria')
@section('content')
<div class="container">
    <h2>Editar Registro #{{ $mascota->id }}</h2>
    <form method="POST" action="{{ route('mascotas.update', $mascota) }}">
        @csrf
        @method('PUT')
        <div class="form-group"><label>CÃ³digo</label><input type="text" name="codigo" value="{{ old('codigo', $mascota->codigo) }}">@error('codigo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Nombre</label><input type="text" name="nombre" value="{{ old('nombre', $mascota->nombre) }}">@error('nombre')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Especie</label><select name="especie">@foreach (['perro', 'gato', 'otro'] as $esp)<option value="{{ $esp }}" @selected(old('especie', $mascota->especie) === $esp)>{{ ucfirst($esp) }}</option>@endforeach</select>@error('especie')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Propietario</label><input type="text" name="propietario" value="{{ old('propietario', $mascota->propietario) }}">@error('propietario')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Fecha de nacimiento</label><input type="date" name="fecha_nacimiento" value="{{ old('fecha_nacimiento', $mascota->fecha_nacimiento) }}">@error('fecha_nacimiento')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="{{ route('mascotas.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection