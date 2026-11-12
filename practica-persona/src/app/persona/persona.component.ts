import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

interface Persona {
  id: number;
  nombre: string;
  apellido: string;
  edad: number;
}

@Component({
  selector: 'app-persona',
  imports: [FormsModule],
  templateUrl: './persona.component.html',
  styleUrl: './persona.component.css'
})
export class PersonaComponent {
  personas: Persona[] = [];
  nombre = '';
  apellido = '';
  edad: number | null = null;
  mensaje = '';
  editandoId: number | null = null;

  constructor(private router: Router) {
    if (!localStorage.getItem('sesion')) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargar();
  }

  cargar() {
    const datos = localStorage.getItem('personas');
    if (datos) {
      this.personas = JSON.parse(datos);
    } else {
      this.personas = [
        { id: 1, nombre: 'Ana', apellido: 'Ejemplo', edad: 25 },
        { id: 2, nombre: 'Luis', apellido: 'Ejemplo', edad: 30 }
      ];
      this.guardarEnStorage();
    }
  }

  guardarEnStorage() {
    localStorage.setItem('personas', JSON.stringify(this.personas));
  }

  limpiar() {
    this.nombre = '';
    this.apellido = '';
    this.edad = null;
  }

  validar(): boolean {
    if (this.nombre.trim() === '' || this.apellido.trim() === '') {
      this.mensaje = 'Nombre y apellido son obligatorios.';
      return false;
    }
    if (this.edad === null || !Number.isInteger(this.edad) || this.edad < 0) {
      this.mensaje = 'La edad debe ser un entero mayor o igual que cero.';
      return false;
    }
    this.mensaje = '';
    return true;
  }

  crear() {
    if (!this.validar()) return;
    const id = this.personas.length > 0 ? Math.max(...this.personas.map(p => p.id)) + 1 : 1;
    this.personas.push({
      id,
      nombre: this.nombre.trim(),
      apellido: this.apellido.trim(),
      edad: this.edad!
    });
    this.guardarEnStorage();
    this.limpiar();
  }

  editar(persona: Persona) {
    this.editandoId = persona.id;
    this.nombre = persona.nombre;
    this.apellido = persona.apellido;
    this.edad = persona.edad;
    this.mensaje = '';
  }

  actualizar() {
    if (!this.validar()) return;
    const persona = this.personas.find(p => p.id === this.editandoId);
    if (persona) {
      persona.nombre = this.nombre.trim();
      persona.apellido = this.apellido.trim();
      persona.edad = this.edad!;
    }
    this.guardarEnStorage();
    this.cancelar();
  }

  cancelar() {
    this.editandoId = null;
    this.limpiar();
    this.mensaje = '';
  }

  eliminar(id: number) {
    if (!confirm('¿Seguro que desea eliminar esta persona?')) return;
    this.personas = this.personas.filter(p => p.id !== id);
    this.guardarEnStorage();
    if (this.editandoId === id) this.cancelar();
  }

  cerrarSesion() {
    localStorage.removeItem('sesion');
    this.router.navigate(['/login']);
  }
}