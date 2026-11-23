@extends('base')
@section('title', 'Registro de Estudiantes')
@section('header', 'Registro de Estudiantes')
@section('content')
<div class="container">
    @if (session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif
    <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">Listado</h2>
        <a href="{{ route('estudiantes.create') }}" class="btn btn-primary">+ Nuevo Registro</a>
    </div>
    <table>
        <thead><tr><th>ID</th><th>NÃºmero de Cuenta</th><th>Nombre</th><th>Correo</th><th>Fecha de Nacimiento</th><th>Acciones</th></tr></thead>
        <tbody>
            @forelse ($estudiantes as $estudiante)
                <tr>
                    <td>{{ $estudiante->id }}</td>
                    <td>{{ $estudiante->numero_cuenta }}</td>
                    <td>{{ $estudiante->nombre }}</td>
                    <td>{{ $estudiante->correo }}</td>
                    <td>{{ \Carbon\Carbon::parse($estudiante->fecha_nacimiento)->format('d/m/Y') }}</td>
                    <td class="actions">
                        <a href="{{ route('estudiantes.edit', $estudiante) }}" class="btn btn-warning">Editar</a>
                        <form method="POST" action="{{ route('estudiantes.destroy', $estudiante) }}" onsubmit="return confirm('Â¿Seguro que deseas eliminar este registro?')">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @empty
                <tr><td colspan="6" style="text-align: center;">No hay registros disponibles.</td></tr>
            @endforelse
        </tbody>
    </table>
</div>
@endsection