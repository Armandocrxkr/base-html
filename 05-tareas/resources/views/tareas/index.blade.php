@extends('base')
@section('title', 'Control de Tareas')
@section('header', 'Control de Tareas')
@section('content')
<div class="container">
    @if (session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif
    <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">Listado</h2>
        <a href="{{ route('tareas.create') }}" class="btn btn-primary">+ Nuevo Registro</a>
    </div>
    <table>
        <thead><tr><th>ID</th><th>TÃ­tulo</th><th>DescripciÃ³n</th><th>Responsable</th><th>Fecha LÃ­mite</th><th>Estado</th><th>Acciones</th></tr></thead>
        <tbody>
            @forelse ($tareas as $tarea)
                <tr>
                    <td>{{ $tarea->id }}</td>
                    <td>{{ $tarea->titulo }}</td>
                    <td>{{ $tarea->descripcion }}</td>
                    <td>{{ $tarea->responsable }}</td>
                    <td>{{ \Carbon\Carbon::parse($tarea->fecha_limite)->format('d/m/Y') }}</td>
                    <td>{{ ucfirst($tarea->estado) }}</td>
                    <td class="actions">
                        <a href="{{ route('tareas.edit', $tarea) }}" class="btn btn-warning">Editar</a>
                        <form method="POST" action="{{ route('tareas.destroy', $tarea) }}" onsubmit="return confirm('Â¿Seguro que deseas eliminar este registro?')">
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