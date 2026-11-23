<?php
namespace App\Http\Controllers;
use App\Models\Estudiante;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class EstudianteController extends Controller
{
    public function index()
    {
        $estudiantes = Estudiante::all();
        return view('estudiantes.index', compact('estudiantes'));
    }

    public function create()
    {
        return view('estudiantes.create');
    }

    public function store(Request $request)
    {
        $datos = $request->validate([
            'numero_cuenta' => ['required', 'string', 'unique:estudiantes,numero_cuenta'],
            'nombre' => ['required', 'string', 'max:100'],
            'correo' => ['required', 'email', 'unique:estudiantes,correo'],
            'fecha_nacimiento' => ['required', 'date', 'before_or_equal:today'],
        ]);
        Estudiante::create($datos);
        return redirect()->route('estudiantes.index')->with('success', 'Registro creado correctamente.');
    }

    public function edit(Estudiante $estudiante)
    {
        return view('estudiantes.edit', compact('estudiante'));
    }

    public function update(Request $request, Estudiante $estudiante)
    {
        $datos = $request->validate([
            'numero_cuenta' => ['required', 'string', Rule::unique('estudiantes', 'numero_cuenta')->ignore($estudiante->id)],
            'nombre' => ['required', 'string', 'max:100'],
            'correo' => ['required', 'email', Rule::unique('estudiantes', 'correo')->ignore($estudiante->id)],
            'fecha_nacimiento' => ['required', 'date', 'before_or_equal:today'],
        ]);
        $estudiante->update($datos);
        return redirect()->route('estudiantes.index')->with('success', 'Registro actualizado correctamente.');
    }

    public function destroy(Estudiante $estudiante)
    {
        $estudiante->delete();
        return redirect()->route('estudiantes.index')->with('success', 'Registro eliminado correctamente.');
    }
}