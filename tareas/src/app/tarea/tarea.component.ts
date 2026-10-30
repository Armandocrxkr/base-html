import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface Tarea {
  id: number;
  titulo: string;
  descripcion: string;
  fechaLimite: string;
  estado: string;
}

@Component({
  selector: 'app-tarea',
  imports: [],
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
}