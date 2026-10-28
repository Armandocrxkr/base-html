<!DOCTYPE html>
    <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Personas en Laravel</title>
            <style>
                body { font-family: Arial; margin: 24px; color: #172033; }
                main { max-width: 960px; margin: auto; }
                label { display: block; margin-top: 12px; }
                input, button { padding: 8px; margin: 4px 0; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ccc; padding: 8px; }
                .error { color: #a01818; }
                .exito { color: #14613e; }
            </style>
        </head>
        <body>
        <main>
            @auth
                <p>Sesión de {{ auth()->user()->name }}</p>
                <form method="POST" action="{{ route('logout') }}">
                    @csrf
                    <button type="submit">Cerrar sesión</button>
                </form>
            @endauth
            @if (session('exito'))
                <p class="exito">{{ session('exito') }}</p>
            @endif
            @if ($errors->any())
                <ul class="error">
                    @foreach ($errors->all() as $error)
                        <li>{{ $error }}</li>
                    @endforeach
                </ul>
            @endif
            @yield('contenido')
        </main>
    </body>
</html>