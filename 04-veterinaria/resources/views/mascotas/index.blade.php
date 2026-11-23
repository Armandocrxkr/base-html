@extends('base')
@section('title', 'ClÃ­nica Veterinaria')
@section('header', 'ClÃ­nica Veterinaria')
@section('content')
<div class="container">
    @if (session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif
    <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">Listado</h2>
        <a href="{{ route('mascotas.create') }}" class="btn btn-primary">+ Nuevo Registro</a>
    </div>
    <table>
        <thead><tr><th>ID</th><th>CÃ³digo</th><th>Nombre</th><th>Especie</th><th>Propietario</th><th>Fecha de Nacimiento</th><th>Acciones</th></tr></thead>
        <tbody>
            @forelse ($mascotas as $mascota)
                <tr>
                    <td>{{ $mascota->id }}</td>
                    <td>{{ $mascota->codigo }}</td>
                    <td>{{ $mascota->nombre }}</td>
                    <td>{{ ucfirst($mascota->especie) }}</td>
                    <td>{{ $mascota->propietario }}</td>
                    <td>{{ \Carbon\Carbon::parse($mascota->fecha_nacimiento)->format('d/m/Y') }}</td>
                    <td class="actions">
                        <a href="{{ route('mascotas.edit', $mascota) }}" class="btn btn-warning">Editar</a>
                        <form method="POST" action="{{ route('mascotas.destroy', $mascota) }}" onsubmit="return confirm('Â¿Seguro que deseas eliminar este registro?')">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @empty
                <tr><td colspan="7" style="text-align: center;">No hay registros disponibles.</td></tr>
            @endforelse
        </tbody>
    </table>
</div>
@endsection