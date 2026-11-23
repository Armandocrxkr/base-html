<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>@yield('title', 'Sistema CRUD')</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f8; color: #222; margin: 0; padding: 0; }
        header { background: #1e293b; color: #fff; padding: 14px 24px; display: flex; justify-content: space-between; align-items: center; }
        header h1 { font-size: 18px; margin: 0; }
        .container { max-width: 960px; margin: 28px auto; background: #fff; padding: 24px; border-radius: 6px; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }
        .container-sm { max-width: 400px; margin: 60px auto; background: #fff; padding: 24px; border-radius: 6px; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 14px; }
        label { display: block; font-weight: bold; margin-bottom: 5px; font-size: 14px; }
        input, select, textarea { width: 100%; padding: 8px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; font-size: 14px; }
        .btn { display: inline-block; padding: 8px 14px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; font-size: 14px; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-secondary { background: #64748b; color: #fff; }
        .btn-warning { background: #d97706; color: #fff; }
        .btn-danger { background: #dc2626; color: #fff; }
        .alert-success { background: #dcfce7; color: #166534; padding: 10px 14px; border-radius: 4px; margin-bottom: 16px; border: 1px solid #86efac; }
        .error-text { color: #dc2626; font-size: 13px; margin-top: 4px; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; }
        th, td { border: 1px solid #e2e8f0; padding: 10px; text-align: left; font-size: 14px; }
        th { background: #f1f5f9; }
        .actions { display: flex; gap: 6px; }
    </style>
</head>
<body>
    @auth
    <header>
        <h1>@yield('header', 'Inventario de Productos')</h1>
        <form method="POST" action="{{ route('logout') }}">
            @csrf
            <button type="submit" class="btn btn-danger">Cerrar sesiÃ³n ({{ auth()->user()->username }})</button>
        </form>
    </header>
    @endauth

    @yield('content')
</body>
</html>