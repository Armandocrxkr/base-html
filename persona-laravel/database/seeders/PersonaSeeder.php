<?php

namespace Database\Seeders;

use App\Models\Persona;
use Illuminate\Database\Seeder;

class PersonaSeeder extends Seeder
{
    public function run(): void
    {
        if (Persona::count() === 0) {
            Persona::factory()->count(10)->create();
        }
    }
}