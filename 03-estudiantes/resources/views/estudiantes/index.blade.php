@extends('base')
@section('title', 'Registro de Estudiantes')
@section('header', 'Registro de Estudiantes')
@section('content')
<div class="container">
    <p>Bienvenido, <strong>{{ auth()->user()->username }}</strong>. SesiÃ³n iniciada.</p>
</div>
@endsection