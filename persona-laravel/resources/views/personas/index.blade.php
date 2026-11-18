@extends('layouts.app')

@section('contenido')

    <h1>Personas registradas</h1>

    <p><a href="{{ route('personas.create') }}">Crear persona</a></p>

    <table border="1" cellpadding="10">

        <thead>
            <tr>
                <th>ID</th>
                <th>DNI</th>
                <th>Nombre</th>
                <th>Nacimiento</th>
                <th>Acciones</th>
            </tr>
        </thead>

        <tbody>

            @forelse ($personas as $persona)

                <tr>
                    <td>{{ $persona->id }}</td>
                    <td>{{ $persona->dni }}</td>
                    <td>{{ $persona->nombre }}</td>
                    <td>{{ $persona->fecha_nacimiento->format('d/m/Y') }}</td>
                    <td>
                        <a href="{{ route('personas.edit', $persona->id) }}">Editar</a>
                    </td>
                </tr>

            @empty

                <tr>
                    <td colspan="5">
                        No hay personas registradas.
                    </td>
                </tr>

            @endforelse

        </tbody>

    </table>

@endsection