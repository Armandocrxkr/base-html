import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Producto {
  id: number;
  codigo: string;
  nombre: string;
  precio: number;
  existencias: number; 
}

@Component({
  selector: 'app-producto',
  imports: [FormsModule],
  templateUrl: './producto.component.html',
  styleUrl: './producto.component.css'
})
export class ProductoComponent {
  productos: Producto[] = [];

  constructor(private router: Router) {
    if (!localStorage.getItem('sesionTienda')) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargar();
  }

  cargar() {
    const datos = localStorage.getItem('productosTienda');
    this.productos = datos ? JSON.parse(datos) : [];
  }

  cerrarSesion() {
    localStorage.removeItem('sesionTienda');
    this.router.navigate(['/login']);
  }

    codigo = '';
  nombre = '';
  precio: number | null = null;
  existencias: number | null = null;
  mensaje = '';

  guardarEnStorage() {
    localStorage.setItem('productosTienda', JSON.stringify(this.productos));
  }

  limpiar() {
    this.codigo = '';
    this.nombre = '';
    this.precio = null;
    this.existencias = null;
  }

  validar(): boolean {
    if (this.codigo.trim() === '' || this.nombre.trim() === '') {
      this.mensaje = 'Código y nombre son obligatorios.';
      return false;
    }
    if (this.precio === null || !(this.precio > 0)) {
      this.mensaje = 'El precio debe ser mayor que cero.';
      return false;
    }
    if (this.existencias === null || !Number.isInteger(this.existencias) || this.existencias < 0) {
      this.mensaje = 'Las existencias deben ser un entero mayor o igual que cero.';
      return false;
    }
    this.mensaje = '';
    return true;
  }

  crear() {
    if (!this.validar()) return;
    const id = this.productos.length > 0 ? Math.max(...this.productos.map(p => p.id)) + 1 : 1;
    this.productos.push({
      id,
      codigo: this.codigo.trim(),
      nombre: this.nombre.trim(),
      precio: this.precio!,
      existencias: this.existencias!
    });
    this.guardarEnStorage();
    this.limpiar();
  }
}