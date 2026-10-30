import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mascota',
  imports: [],
  templateUrl: './mascota.component.html',
  styleUrl: './mascota.component.css'
})
export class MascotaComponent {
  constructor(private router: Router) {
    if (!localStorage.getItem('sesionVeterinaria')) {
      this.router.navigate(['/login']);
    }
  }

  cerrarSesion() {
    localStorage.removeItem('sesionVeterinaria');
    this.router.navigate(['/login']);
  }
}