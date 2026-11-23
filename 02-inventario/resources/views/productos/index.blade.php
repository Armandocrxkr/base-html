@extends('base')
@section('title', 'Inventario de Productos')
@section('header', 'Inventario de Productos')
@section('content')
<div class="container">
    @if (session('success'))
        <div class="alert-success">{{ session('success') }}</div>
    @endif
    <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">Listado</h2>
        <a href="{{ route('productos.create') }}" class="btn btn-primary">+ Nuevo Registro</a>
    </div>
    <table>
        <thead><tr><th>ID</th><th>CÃ³digo</th><th>Nombre</th><th>Precio</th><th>Existencias</th><th>Acciones</th></tr></thead>
        <tbody>
            @forelse ($productos as $producto)
                <tr>
                    <td>{{ $producto->id }}</td>
                    <td>{{ $producto->codigo }}</td>
                    <td>{{ $producto->nombre }}</td>
                    <td>L. {{ number_format($producto->precio, 2) }}</td>
                    <td>{{ $producto->existencias == 0 ? 'Sin existencias' : $producto->existencias }}</td>
                    <td class="actions">
                        <a href="{{ route('productos.edit', $producto) }}" class="btn btn-warning">Editar</a>
                        <form method="POST" action="{{ route('productos.destroy', $producto) }}" onsubmit="return confirm('Â¿Seguro que deseas eliminar este registro?')">
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