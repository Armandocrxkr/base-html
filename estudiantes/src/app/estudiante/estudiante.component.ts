import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-estudiante',
  imports: [],
  templateUrl: './estudiante.component.html',
  styleUrl: './estudiante.component.css'
})
export class EstudianteComponent {
  constructor(private router: Router) {
    if (!localStorage.getItem('sesionEstudiantes')) {
      this.router.navigate(['/login']);
    }
  }

  cerrarSesion() {
    localStorage.removeItem('sesionEstudiantes');
    this.router.navigate(['/login']);
  }
}