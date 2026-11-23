<?php
namespace App\Http\Controllers;
use App\Models\Mascota;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class MascotaController extends Controller
{
    public function index()
    {
        $mascotas = Mascota::all();
        return view('mascotas.index', compact('mascotas'));
    }

    public function create()
    {
        return view('mascotas.create');
    }

    public function store(Request $request)
    {
        $datos = $request->validate([
            'codigo' => ['required', 'string', 'unique:mascotas,codigo'],
            'nombre' => ['required', 'string', 'max:80'],
            'especie' => ['required', 'in:perro,gato,otro'],
            'propietario' => ['required', 'string', 'max:100'],
            'fecha_nacimiento' => ['required', 'date', 'before_or_equal:today'],
        ]);
        Mascota::create($datos);
        return redirect()->route('mascotas.index')->with('success', 'Registro creado correctamente.');
    }
}