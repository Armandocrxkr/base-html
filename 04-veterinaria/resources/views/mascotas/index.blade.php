@extends('base')
@section('title', 'ClÃ­nica Veterinaria')
@section('header', 'ClÃ­nica Veterinaria')
@section('content')
<div class="container">
    <p>Bienvenido, <strong>{{ auth()->user()->username }}</strong>. SesiÃ³n iniciada.</p>
</div>
@endsection