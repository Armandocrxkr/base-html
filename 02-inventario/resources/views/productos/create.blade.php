@extends('base')
@section('title', 'Nuevo - Inventario de Productos')
@section('header', 'Inventario de Productos')
@section('content')
<div class="container">
    <h2>Registrar</h2>
    <form method="POST" action="{{ route('productos.store') }}">
        @csrf
        <div class="form-group"><label>CÃ³digo</label><input type="text" name="codigo" value="{{ old('codigo') }}">@error('codigo')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Nombre</label><input type="text" name="nombre" value="{{ old('nombre') }}">@error('nombre')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Precio</label><input type="number" step="0.01" name="precio" value="{{ old('precio') }}">@error('precio')<div class="error-text">{{ $message }}</div>@enderror</div>
        <div class="form-group"><label>Existencias</label><input type="number" name="existencias" value="{{ old('existencias') }}">@error('existencias')<div class="error-text">{{ $message }}</div>@enderror</div>
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="{{ route('productos.index') }}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
@endsection