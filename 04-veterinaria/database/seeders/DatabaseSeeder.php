<?php
namespace Database\Seeders;
use App\Models\Mascota;
use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    public function run(): void
    {
        User::updateOrCreate(['username' => 'admin'], ['name' => 'Administrador', 'password' => Hash::make('12345678')]);
        Mascota::factory(10)->create();
    }
}