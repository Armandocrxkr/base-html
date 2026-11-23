@extends('base')
@section('title', 'Nuevo - ClÃ­nica Veterinaria')
@section('header', 'ClÃ­nica Veterinaria')
@section('content')
<div class="container">
    <h2>Registrar</h2>
    <form method="POST" action="{{ route('mascotas.store') }}">
        @csrf
        <div class="form-group"><label>CÃ³digo</label><input type="text" name="codigo" value="{{ old('codigo') }}">@error('codigo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Nombre</label><input type="text" name="nombre" value="{{ old('nombre') }}">@error('nombre')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Especie</label><select name="especie"><option value="">Seleccione...</option>@foreach (['perro', 'gato', 'otro'] as $esp)<option value="{{ $esp }}" @selected(old('especie') === $esp)>{{ ucfirst($esp) }}</option>@endforeach</select>@error('especie')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Propietario</label><input type="text" name="propietario" value="{{ old('propietario') }}">@error('propietario')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Fecha de nacimiento</label><input type="date" name="fecha_nacimiento" value="{{ old('fecha_nacimiento') }}">@error('fecha_nacimiento')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="{{ route('mascotas.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection