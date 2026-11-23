@extends('base')
@section('title', 'Catálogo de Libros')
@section('header', 'Biblioteca - Catálogo de Libros')
@section('content')
<div class="container">
    <p>Bienvenido, <strong>{{ auth()->user()->username }}</strong>. Sesión iniciada.</p>
</div>
@endsection