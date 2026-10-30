import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface Mascota {
  id: number;
  nombre: string;
  especie: string;
  edad: number;
  propietario: string;
}

@Component({
  selector: 'app-mascota',
  imports: [],
  templateUrl: './mascota.component.html',
  styleUrl: './mascota.component.css'
})
export class MascotaComponent {
  mascotas: Mascota[] = [];

  constructor(private router: Router) {
    if (!localStorage.getItem('sesionVeterinaria')) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargar();
  }

  cargar() {
    const datos = localStorage.getItem('mascotasVeterinaria');
    this.mascotas = datos ? JSON.parse(datos) : [];
  }

  cerrarSesion() {
    localStorage.removeItem('sesionVeterinaria');
    this.router.navigate(['/login']);
  }
}