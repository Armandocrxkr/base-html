<?php
namespace Database\Factories;
use Illuminate\Database\Eloquent\Factories\Factory;

class EstudianteFactory extends Factory
{
    public function definition(): array
    {
        return [
            'numero_cuenta' => '2026' . fake()->unique()->numerify('#######'),
            'nombre' => fake()->name(),
            'correo' => fake()->unique()->safeEmail(),
            'fecha_nacimiento' => fake()->date('Y-m-d', '-17 years'),
        ];
    }
}