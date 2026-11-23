@extends('base')

@section('title', 'Editar Libro')
@section('header', 'Biblioteca - Editar Libro')

@section('content')
<div class="container">
    <h2>Editar Libro #{{ $libro->id }}</h2>
    <form method="POST" action="{{ route('libros.update', $libro) }}">
        @csrf
        @method('PUT')
        <div class="form-group">
            <label>Código</label>
            <input type="text" name="codigo" value="{{ old('codigo', $libro->codigo) }}">
            @error('codigo') <div class="error-text">{{ $message }}</div> @enderror
        </div>

        <div class="form-group">
            <label>Título</label>
            <input type="text" name="titulo" value="{{ old('titulo', $libro->titulo) }}">
            @error('titulo') <div class="error-text">{{ $message }}</div> @enderror
        </div>

        <div class="form-group">
            <label>Autor</label>
            <input type="text" name="autor" value="{{ old('autor', $libro->autor) }}">
            @error('autor') <div class="error-text">{{ $message }}</div> @enderror
        </div>

        <div class="form-group">
            <label>Año de publicación</label>
            <input type="number" name="anio_publicacion" value="{{ old('anio_publicacion', $libro->anio_publicacion) }}">
            @error('anio_publicacion') <div class="error-text">{{ $message }}</div> @enderror
        </div>

        <button type="submit" class="btn btn-primary">Actualizar</button>
        <a href="{{ route('libros.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection