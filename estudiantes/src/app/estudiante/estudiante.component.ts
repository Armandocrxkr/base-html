import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface Estudiante {
  id: number;
  cuenta: string;
  nombre: string;
  carrera: string;
  fechaIngreso: string;
}

@Component({
  selector: 'app-estudiante',
  imports: [],
  templateUrl: './estudiante.component.html',
  styleUrl: './estudiante.component.css'
})
export class EstudianteComponent {
  estudiantes: Estudiante[] = [];

  constructor(private router: Router) {
    if (!localStorage.getItem('sesionEstudiantes')) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargar();
  }

  cargar() {
    const datos = localStorage.getItem('registrosEstudiantes');
    this.estudiantes = datos ? JSON.parse(datos) : [];
  }

  cerrarSesion() {
    localStorage.removeItem('sesionEstudiantes');
    this.router.navigate(['/login']);
  }

    cuenta = '';
  nombre = '';
  carrera = '';
  fechaIngreso = '';
  mensaje = '';

  guardarEnStorage() {
    localStorage.setItem('registrosEstudiantes', JSON.stringify(this.estudiantes));
  }

  limpiar() {
    this.cuenta = '';
    this.nombre = '';
    this.carrera = '';
    this.fechaIngreso = '';
  }

  validar(): boolean {
    if (this.cuenta.trim() === '' || this.nombre.trim() === '' || this.carrera.trim() === '') {
      this.mensaje = 'Cuenta, nombre y carrera son obligatorios.';
      return false;
    }
    if (this.fechaIngreso === '') {
      this.mensaje = 'La fecha de ingreso es obligatoria.';
      return false;
    }
    this.mensaje = '';
    return true;
  }

  crear() {
    if (!this.validar()) return;
    const id = this.estudiantes.length > 0 ? Math.max(...this.estudiantes.map(e => e.id)) + 1 : 1;
    this.estudiantes.push({
      id,
      cuenta: this.cuenta.trim(),
      nombre: this.nombre.trim(),
      carrera: this.carrera.trim(),
      fechaIngreso: this.fechaIngreso
    });
    this.guardarEnStorage();
    this.limpiar();
  }
}