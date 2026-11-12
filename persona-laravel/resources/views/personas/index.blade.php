
@extends('layouts.app')

@section('contenido')

    <h1>Personas registradas</h1>

    <table border="1" cellpadding="10">

        <thead>
            <tr>
                <th>ID</th>
                <th>DNI</th>
                <th>Nombre</th>
                <th>Nacimiento</th>
            </tr>
        </thead>

        <tbody>

            @forelse ($personas as $persona)

                <tr>
                    <td>{{ $persona->id }}</td>
                    <td>{{ $persona->dni }}</td>
                    <td>{{ $persona->nombre }}</td>
                    <td>{{ $persona->fecha_nacimiento->format('d/m/Y') }}</td>
                </tr>

            @empty

                <tr>
                    <td colspan="4">
                        No hay personas registradas.
                    </td>
                </tr>

            @endforelse

        </tbody>

    </table>

@endsection
