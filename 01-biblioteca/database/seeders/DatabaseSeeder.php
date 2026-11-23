<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        \App\Models\User::updateOrCreate(
            ['username' => 'admin'],
            ['name' => 'Administrador', 'password' => \Illuminate\Support\Facades\Hash::make('12345678')]
        );

        \App\Models\Libro::factory(10)->create();
    }
}
