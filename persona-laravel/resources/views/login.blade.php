
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Iniciar sesión | Personas Laravel</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .contenedor {
            background: white;
            width: 100%;
            max-width: 400px;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 5px 25px rgba(0, 0, 0, 0.08);
        }

        h1 {
            text-align: center;
            color: #1e293b;
            margin-bottom: 10px;
        }

        .descripcion {
            text-align: center;
            color: #64748b;
            margin-bottom: 30px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #334155;
            font-weight: bold;
        }

        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 7px;
            margin-bottom: 20px;
            font-size: 15px;
        }

        input:focus {
            outline: 2px solid #0ea5e9;
            border-color: #0ea5e9;
        }

        .recordar {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 20px;
            color: #475569;
        }

        button {
            width: 100%;
            padding: 13px;
            background: #0ea5e9;
            color: white;
            border: none;
            border-radius: 7px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #0284c7;
        }

        .error {
            background: #fee2e2;
            color: #991b1b;
            padding: 12px;
            border-radius: 7px;
            margin-bottom: 20px;
            font-size: 14px;
        }
    </style>
</head>

<body>

    <div class="contenedor">

        <h1>Iniciar sesión</h1>

        <p class="descripcion">
            Sistema de registro de personas
        </p>

        @if ($errors->any())
            <div class="error">
                {{ $errors->first() }}
            </div>
        @endif

        <form action="{{ route('login.store') }}" method="POST">

            @csrf

            <label for="email">Correo electrónico</label>

            <input
                type="email"
                id="email"
                name="email"
                value="{{ old('email') }}"
                placeholder="admin@clase.test"
                required
                autofocus
            >

            <label for="password">Contraseña</label>

            <input
                type="password"
                id="password"
                name="password"
                placeholder="Ingresa tu contraseña"
                required
            >

            <div class="recordar">
                <input type="checkbox" name="remember" value="1" id="remember">
                <label for="remember">Recordarme</label>
            </div>

            <button type="submit">
                Iniciar sesión
            </button>

        </form>

    </div>

</body>
</html>
