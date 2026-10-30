import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Libro {
  id: number;
  codigo: string;
  titulo: string;
  autor: string;
  anio: number;
}

@Component({
  selector: 'app-libro',
  imports: [FormsModule],
  templateUrl: './libro.component.html',
  styleUrl: './libro.component.css'
})
export class LibroComponent {
  libros: Libro[] = [];

  constructor(private router: Router) {
    if (!localStorage.getItem('sesionBiblioteca')) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargar();
  }

  cargar() {
    const datos = localStorage.getItem('librosBiblioteca');
    this.libros = datos ? JSON.parse(datos) : [];
  }

  cerrarSesion() {
    localStorage.removeItem('sesionBiblioteca');
    this.router.navigate(['/login']);
  }

    codigo = '';
  titulo = '';
  autor = '';
  anio: number | null = null;
  mensaje = '';

  guardarEnStorage() {
    localStorage.setItem('librosBiblioteca', JSON.stringify(this.libros));
  }

  limpiar() {
    this.codigo = '';
    this.titulo = '';
    this.autor = '';
    this.anio = null;
  }

  validar(): boolean {
    if (this.codigo.trim() === '' || this.titulo.trim() === '' || this.autor.trim() === '') {
      this.mensaje = 'Código, título y autor son obligatorios.';
      return false;
    }
    if (this.anio === null || !Number.isInteger(this.anio) || this.anio < 1000 || this.anio > 9999) {
      this.mensaje = 'El año debe ser un entero de cuatro cifras.';
      return false;
    }
    this.mensaje = '';
    return true;
  }

  crear() {
    if (!this.validar()) return;
    const id = this.libros.length > 0 ? Math.max(...this.libros.map(l => l.id)) + 1 : 1;
    this.libros.push({
      id,
      codigo: this.codigo.trim(),
      titulo: this.titulo.trim(),
      autor: this.autor.trim(),
      anio: this.anio!
    });
    this.guardarEnStorage();
    this.limpiar();
  }
}