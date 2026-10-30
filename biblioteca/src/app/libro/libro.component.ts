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

    editandoId: number | null = null;

  editar(libro: Libro) {
    this.editandoId = libro.id;
    this.codigo = libro.codigo;
    this.titulo = libro.titulo;
    this.autor = libro.autor;
    this.anio = libro.anio;
    this.mensaje = '';
  }

  actualizar() {
    if (!this.validar()) return;
    const libro = this.libros.find(l => l.id === this.editandoId);
    if (libro) {
      libro.codigo = this.codigo.trim();
      libro.titulo = this.titulo.trim();
      libro.autor = this.autor.trim();
      libro.anio = this.anio!;
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
    if (!confirm('¿Seguro que desea eliminar este libro?')) return;
    this.libros = this.libros.filter(l => l.id !== id);
    this.guardarEnStorage();
    if (this.editandoId === id) this.cancelar();
  }
}