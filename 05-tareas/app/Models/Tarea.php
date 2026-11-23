<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Tarea extends Model
{
    use HasFactory;
    protected $fillable = ['titulo', 'descripcion', 'responsable', 'fecha_limite', 'estado'];
}