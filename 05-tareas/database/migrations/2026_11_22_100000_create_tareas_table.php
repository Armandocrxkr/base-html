<?php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void
    {
        Schema::create('tareas', function (Blueprint $table) {
            $table->id();
            $table->string('titulo', 150);
            $table->string('descripcion', 500);
            $table->string('responsable', 100);
            $table->date('fecha_limite');
            $table->string('estado');
            $table->timestamps();
        });
    }
    public function down(): void { Schema::dropIfExists('tareas'); }
};