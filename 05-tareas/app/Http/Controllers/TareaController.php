<?php
namespace App\Http\Controllers;
use App\Models\Tarea;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class TareaController extends Controller
{
    public function index()
    {
        $tareas = Tarea::all();
        return view('tareas.index', compact('tareas'));
    }
}