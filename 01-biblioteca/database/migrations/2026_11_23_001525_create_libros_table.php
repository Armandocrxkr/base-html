<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
public function up(): void
{
    Schema::create('libros', function (Blueprint $table) {
        $table->id();
        $table->string('codigo')->unique();
        $table->string('titulo', 150);
        $table->string('autor', 100);
        $table->integer('anio_publicacion');
        $table->timestamps();
    });
}

    
    public function down(): void
    {
        Schema::dropIfExists('libros');
    }
};
