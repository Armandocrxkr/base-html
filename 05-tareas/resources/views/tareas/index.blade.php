@extends('base')
@section('title', 'Control de Tareas')
@section('header', 'Control de Tareas')
@section('content')
<div class="container">
    <p>Bienvenido, <strong>{{ auth()->user()->username }}</strong>. SesiÃ³n iniciada.</p>
</div>
@endsection