@extends('base')
@section('title', 'Inventario de Productos')
@section('header', 'Inventario de Productos')
@section('content')
<div class="container">
    <p>Bienvenido, <strong>{{ auth()->user()->username }}</strong>. SesiÃ³n iniciada.</p>
</div>
@endsection