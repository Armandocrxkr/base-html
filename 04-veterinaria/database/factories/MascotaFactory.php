<?php
namespace Database\Factories;
use Illuminate\Database\Eloquent\Factories\Factory;

class MascotaFactory extends Factory
{
    public function definition(): array
    {
        return [
            'codigo' => 'MAS-' . fake()->unique()->numerify('####'),
            'nombre' => fake()->firstName(),
            'especie' => fake()->randomElement(['perro', 'gato', 'otro']),
            'propietario' => fake()->name(),
            'fecha_nacimiento' => fake()->date('Y-m-d', 'today'),
        ];
    }
}