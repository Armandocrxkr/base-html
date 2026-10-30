import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Tarea {
  id: number;
  titulo: string;
  descripcion: string;
  fechaLimite: string;
  estado: string;
}

@Component({
  selector: 'app-tarea',
  imports: [FormsModule],
  templateUrl: './tarea.component.html',
  styleUrl: './tarea.component.css'
})
export class TareaComponent {
  tareas: Tarea[] = [];

  constructor(private router: Router) {
    if (!localStorage.getItem('sesionTareas')) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargar();
  }

  cargar() {
    const datos = localStorage.getItem('agendaTareas');
    this.tareas = datos ? JSON.parse(datos) : [];
  }

  cerrarSesion() {
    localStorage.removeItem('sesionTareas');
    this.router.navigate(['/login']);
  }

    titulo = '';
  descripcion = '';
  fechaLimite = '';
  estado = 'Pendiente';
  mensaje = '';

  guardarEnStorage() {
    localStorage.setItem('agendaTareas', JSON.stringify(this.tareas));
  }

  limpiar() {
    this.titulo = '';
    this.descripcion = '';
    this.fechaLimite = '';
    this.estado = 'Pendiente';
  }

  validar(): boolean {
    if (this.titulo.trim() === '' || this.descripcion.trim() === '') {
      this.mensaje = 'Título y descripción son obligatorios.';
      return false;
    }
    if (this.fechaLimite === '') {
      this.mensaje = 'La fecha límite es obligatoria.';
      return false;
    }
    if (this.estado !== 'Pendiente' && this.estado !== 'Completada') {
      this.mensaje = 'El estado debe ser Pendiente o Completada.';
      return false;
    }
    this.mensaje = '';
    return true;
  }

  crear() {
    if (!this.validar()) return;
    const id = this.tareas.length > 0 ? Math.max(...this.tareas.map(t => t.id)) + 1 : 1;
    this.tareas.push({
      id,
      titulo: this.titulo.trim(),
      descripcion: this.descripcion.trim(),
      fechaLimite: this.fechaLimite,
      estado: this.estado
    });
    this.guardarEnStorage();
    this.limpiar();
  }
}