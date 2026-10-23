import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Persona {
  id: number;
  dni: string;
  nombre: string;
  fechaNacimiento: string;
}

@Component({
  selector: 'app-persona',
  imports: [FormsModule],
  templateUrl: './persona.component.html',
  styleUrl: './persona.component.css'
})
export class PersonaComponent {
  personas: Persona[] = [];

  dni = '';
  nombre = '';
  fechaNacimiento = '';
  indiceEditar = -1;

  constructor(private router: Router) {
    if (localStorage.getItem('sesionBasica') !== 'si') {
      this.router.navigate(['/login']);
      return;
    }

    const datos = localStorage.getItem('personasBasico');
    if (datos !== null) {
      this.personas = JSON.parse(datos);
    }
  }

  cerrarSesion() {
    localStorage.removeItem('sesionBasica');
    this.router.navigate(['/login']);
  }

  editar(indice: number) {
    this.indiceEditar = indice;
    this.dni = this.personas[indice].dni;
    this.nombre = this.personas[indice].nombre;
    this.fechaNacimiento = this.personas[indice].fechaNacimiento;
  }

  eliminar(indice: number) {
    if (confirm('¿Eliminar esta persona?')) {
      this.personas.splice(indice, 1);
      this.guardarEnLocalStorage();
      this.limpiar();
    }
  }

  guardar() {
    if (this.dni.trim() === '' || this.nombre.trim() === '' ||
        this.fechaNacimiento === '') {
      alert('Complete los tres campos');
      return;
    }
    if (this.indiceEditar === -1) {
      let nuevoId = 1;
      for (const persona of this.personas) {
        if (persona.id >= nuevoId) {
          nuevoId = persona.id + 1;
        }
      }
      const nuevaPersona: Persona = {
        id: nuevoId,
        dni: this.dni.trim(),
        nombre: this.nombre.trim(),
        fechaNacimiento: this.fechaNacimiento
      };
      this.personas.push(nuevaPersona);
    } else {
      this.personas[this.indiceEditar].dni = this.dni.trim();
      this.personas[this.indiceEditar].nombre = this.nombre.trim();
      this.personas[this.indiceEditar].fechaNacimiento =
        this.fechaNacimiento;
    }
    this.guardarEnLocalStorage();
    this.limpiar();
  }

  limpiar() {
    this.dni = '';
    this.nombre = '';
    this.fechaNacimiento = '';
    this.indiceEditar = -1;
  }

  guardarEnLocalStorage() {
    localStorage.setItem('personasBasico',
      JSON.stringify(this.personas));
  }
}