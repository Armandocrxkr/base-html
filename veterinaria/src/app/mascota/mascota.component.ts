import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Mascota {
  id: number;
  nombre: string;
  especie: string;
  edad: number;
  propietario: string;
}

@Component({
  selector: 'app-mascota',
  imports: [FormsModule],
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

    nombre = '';
  especie = '';
  edad: number | null = null;
  propietario = '';
  mensaje = '';

  guardarEnStorage() {
    localStorage.setItem('mascotasVeterinaria', JSON.stringify(this.mascotas));
  }

  limpiar() {
    this.nombre = '';
    this.especie = '';
    this.edad = null;
    this.propietario = '';
  }

  validar(): boolean {
    if (this.nombre.trim() === '' || this.especie.trim() === '' || this.propietario.trim() === '') {
      this.mensaje = 'Nombre, especie y propietario son obligatorios.';
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
    const id = this.mascotas.length > 0 ? Math.max(...this.mascotas.map(m => m.id)) + 1 : 1;
    this.mascotas.push({
      id,
      nombre: this.nombre.trim(),
      especie: this.especie.trim(),
      edad: this.edad!,
      propietario: this.propietario.trim()
    });
    this.guardarEnStorage();
    this.limpiar();
  }

    editandoId: number | null = null;

  editar(mascota: Mascota) {
    this.editandoId = mascota.id;
    this.nombre = mascota.nombre;
    this.especie = mascota.especie;
    this.edad = mascota.edad;
    this.propietario = mascota.propietario;
    this.mensaje = '';
  }

  actualizar() {
    if (!this.validar()) return;
    const mascota = this.mascotas.find(m => m.id === this.editandoId);
    if (mascota) {
      mascota.nombre = this.nombre.trim();
      mascota.especie = this.especie.trim();
      mascota.edad = this.edad!;
      mascota.propietario = this.propietario.trim();
    }
    this.guardarEnStorage();
    this.cancelar();
  }

  cancelar() {
    this.editandoId = null;
    this.limpiar();
    this.mensaje = '';
  }
}