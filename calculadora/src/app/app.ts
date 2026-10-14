import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  pantalla = signal('0');
  private operando: number | null = null;
  private operador: string | null = null;
  private nuevoNumero = true;

  digito(d: string) {
    if (this.nuevoNumero || this.pantalla() === '0') {
      this.pantalla.set(d);
      this.nuevoNumero = false;
    } else {
      this.pantalla.update(p => p + d);
    }
  }

  punto() {
    if (this.nuevoNumero) {
      this.pantalla.set('0.');
      this.nuevoNumero = false;
    } else if (!this.pantalla().includes('.')) {
      this.pantalla.update(p => p + '.');
    }
  }

  operacion(op: string) {
    if (this.operador && !this.nuevoNumero) {
      this.igual();
    }
    this.operando = parseFloat(this.pantalla());
    this.operador = op;
    this.nuevoNumero = true;
  }

  igual() {
    if (this.operador === null || this.operando === null) return;
    const a = this.operando;
    const b = parseFloat(this.pantalla());
    let r: number;
    switch (this.operador) {
      case '+': r = a + b; break;
      case '-': r = a - b; break;
      case '×': r = a * b; break;
      case '÷':
        if (b === 0) {
          this.pantalla.set('Error');
          this.operando = null;
          this.operador = null;
          this.nuevoNumero = true;
          return;
        }
        r = a / b;
        break;
      default: return;
    }
    this.pantalla.set(String(r));
    this.operando = null;
    this.operador = null;
    this.nuevoNumero = true;
  }

  limpiar() {
    this.pantalla.set('0');
    this.operando = null;
    this.operador = null;
    this.nuevoNumero = true;
  }
}