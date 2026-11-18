<?php

namespace App\Http\Controllers;

use App\Models\Persona;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class PersonaController extends Controller
{
    public function index()
    {
        $personas = Persona::orderBy('id')->get();
        return view('personas.index', compact('personas'));
    }

    public function create()
    {
        return view('personas.create');
    }

    public function edit(int $id)
    {
        $persona = Persona::findOrFail($id);
        return view('personas.edit', ['persona' => $persona]);
    }
    
    public function update(Request $request, int $id)
    {
        $persona = Persona::findOrFail($id);
        $datos = $request->validate([
            'dni' => ['required', 'regex:/^[0-9]{13}$/',
                Rule::unique('personas', 'dni')->ignore($persona)],
            'nombre' => ['required', 'string', 'max:100'],
            'fecha_nacimiento' => ['required', 'date',
                                    'before_or_equal:today'],
        ]);
        $persona->dni = $datos['dni'];
        $persona->nombre = $datos['nombre'];
        $persona->fecha_nacimiento = $datos['fecha_nacimiento'];
        $persona->save();
        return redirect()->route('personas.index')
            ->with('exito', 'Persona actualizada correctamente.');
    }

    public function store(Request $request)
    {
        $datos = $request->validate([
            'dni' => ['required', 'regex:/^[0-9]{13}$/', 'unique:personas,dni'],
            'nombre' => ['required', 'string', 'max:100'],
            'fecha_nacimiento' => ['required', 'date', 'before_or_equal:today'],
        ]);

        $persona = new Persona();
        $persona->dni = $datos['dni'];
        $persona->nombre = $datos['nombre'];
        $persona->fecha_nacimiento = $datos['fecha_nacimiento'];
        $persona->save();

        return redirect()->route('personas.index')
            ->with('exito', 'Persona creada correctamente.');
    }
}