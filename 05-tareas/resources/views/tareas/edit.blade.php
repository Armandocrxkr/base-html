@extends('base')
@section('title', 'Editar - Control de Tareas')
@section('header', 'Control de Tareas')
@section('content')
<div class="container">
    <h2>Editar Registro #{{ $tarea->id }}</h2>
    <form method="POST" action="{{ route('tareas.update', $tarea) }}">
        @csrf
        @method('PUT')
        <div class="form-group"><label>TÃ­tulo</label><input type="text" name="titulo" value="{{ old('titulo', $tarea->titulo) }}">@error('titulo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>DescripciÃ³n</label><input type="text" name="descripcion" value="{{ old('descripcion', $tarea->descripcion) }}">@error('descripcion')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Responsable</label><input type="text" name="responsable" value="{{ old('responsable', $tarea->responsable) }}">@error('responsable')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Fecha lÃ­mite</label><input type="date" name="fecha_limite" value="{{ old('fecha_limite', $tarea->fecha_limite) }}">@error('fecha_limite')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Estado</label><select name="estado">@foreach (['pendiente', 'en proceso', 'completada'] as $est)<option value="{{ $est }}" @selected(old('estado', $tarea->estado) === $est)>{{ ucfirst($est) }}</option>@endforeach</select>@error('estado')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="{{ route('tareas.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection