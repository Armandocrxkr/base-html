<?php
namespace Database\Seeders;
use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;
class UsuarioDemoSeeder extends Seeder
{
    public function run(): void
    {
        User::updateOrCreate(
            ['email' => 'admin@clase.test'],
            [
                'name' => 'Administrador de práctica',
                'password' => Hash::make('Clase2026!'),
            ]
        );
    }
}