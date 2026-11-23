<?php
namespace App\Http\Controllers;
use App\Models\Producto;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class ProductoController extends Controller
{
    public function index()
    {
        $productos = Producto::all();
        return view('productos.index', compact('productos'));
    }

    public function create()
    {
        return view('productos.create');
    }

    public function store(Request $request)
    {
        $datos = $request->validate([
            'codigo' => ['required', 'string', 'unique:productos,codigo'],
            'nombre' => ['required', 'string', 'max:100'],
            'precio' => ['required', 'numeric', 'gt:0'],
            'existencias' => ['required', 'integer', 'min:0'],
        ]);
        Producto::create($datos);
        return redirect()->route('productos.index')->with('success', 'Registro creado correctamente.');
    }

    public function edit(Producto $producto)
    {
        return view('productos.edit', compact('producto'));
    }

    public function update(Request $request, Producto $producto)
    {
        $datos = $request->validate([
            'codigo' => ['required', 'string', Rule::unique('productos', 'codigo')->ignore($producto->id)],
            'nombre' => ['required', 'string', 'max:100'],
            'precio' => ['required', 'numeric', 'gt:0'],
            'existencias' => ['required', 'integer', 'min:0'],
        ]);
        $producto->update($datos);
        return redirect()->route('productos.index')->with('success', 'Registro actualizado correctamente.');
    }

    public function destroy(Producto $producto)
    {
        $producto->delete();
        return redirect()->route('productos.index')->with('success', 'Registro eliminado correctamente.');
    }
}