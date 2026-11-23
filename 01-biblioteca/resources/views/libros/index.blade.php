@extends('base')

@section('title', 'Catálogo de Libros')
@section('header', 'Biblioteca - Catálogo de Libros')

@section('content')
<div class="container">
    @if (session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif

    <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">Listado de Libros</h2>
        <a href="{{ route('libros.create') }}" class="btn btn-primary">+ Nuevo Libro</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Código</th>
                <th>Título</th>
                <th>Autor</th>
                <th>Año</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            @forelse ($libros as $libro)
                <tr>
                    <td>{{ $libro->id }}</td>
                    <td>{{ $libro->codigo }}</td>
                    <td>{{ $libro->titulo }}</td>
                    <td>{{ $libro->autor }}</td>
                    <td>{{ $libro->anio_publicacion }}</td>
                    <td class="actions">
                        <a href="{{ route('libros.edit', $libro) }}" class="btn btn-warning">Editar</a>
                        <form method="POST" action="{{ route('libros.destroy', $libro) }}" onsubmit="return confirm('¿Seguro que deseas eliminar este libro?')">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @empty
                <tr>
                    <td colspan="6" style="text-align: center;">No hay libros registrados.</td>
                </tr>
            @endforelse
        </tbody>
    </table>
</div>
@endsection