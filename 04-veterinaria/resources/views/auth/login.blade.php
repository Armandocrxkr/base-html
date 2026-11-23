@extends('base')

@section('title', 'Iniciar SesiÃ³n - Biblioteca')

@section('content')
<div class="container-sm">
    <h2 style="margin-top: 0;">ClÃ­nica Veterinaria</h2>
    <form method="POST" action="{{ route('login.post') }}">
        @csrf
        <div class="form-group">
            <label for="username">Usuario</label>
            <input type="text" id="username" name="username" value="{{ old('username') }}" required autofocus>
            @error('username')
                <div class="error-text">{{ $message }}</div>
            @enderror
        </div>

        <div class="form-group">
            <label for="password">ContraseÃ±a</label>
            <input type="password" id="password" name="password" required>
            @error('password')
                <div class="error-text">{{ $message }}</div>
            @enderror
        </div>

        <button type="submit" class="btn btn-primary" style="width: 100%;">Iniciar sesiÃ³n</button>
    </form>
</div>
@endsection