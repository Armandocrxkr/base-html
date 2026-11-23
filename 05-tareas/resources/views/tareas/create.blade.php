@extends('base')
@section('title', 'Nuevo - Control de Tareas')
@section('header', 'Control de Tareas')
@section('content')
<div class="container">
    <h2>Registrar</h2>
    <form method="POST" action="{{ route('tareas.store') }}">
        @csrf
        <div class="form-group"><label>TÃ­tulo</label><input type="text" name="titulo" value="{{ old('titulo') }}">@error('titulo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>DescripciÃ³n</label><input type="text" name="descripcion" value="{{ old('descripcion') }}">@error('descripcion')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Responsable</label><input type="text" name="responsable" value="{{ old('responsable') }}">@error('responsable')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Fecha lÃ­mite</label><input type="date" name="fecha_limite" value="{{ old('fecha_limite') }}">@error('fecha_limite')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Estado</label><select name="estado">@foreach (['pendiente', 'en proceso', 'completada'] as $est)<option value="{{ $est }}" @selected(old('estado', 'pendiente') === $est)>{{ ucfirst($est) }}</option>@endforeach</select>@error('estado')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="{{ route('tareas.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection