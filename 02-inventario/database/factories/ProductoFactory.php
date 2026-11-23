<?php
namespace Database\Factories;
use Illuminate\Database\Eloquent\Factories\Factory;

class ProductoFactory extends Factory
{
    public function definition(): array
    {
        return [
            'codigo' => 'PRD-' . fake()->unique()->numerify('####'),
            'nombre' => fake()->words(2, true),
            'precio' => fake()->randomFloat(2, 10, 500),
            'existencias' => fake()->numberBetween(0, 50),
        ];
    }
}