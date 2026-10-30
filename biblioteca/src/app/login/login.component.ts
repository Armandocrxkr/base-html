import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  usuario = '';
  contrasena = '';
  mensaje = '';

  constructor(private router: Router) {
    if (localStorage.getItem('sesionBiblioteca')) {
      this.router.navigate(['/libros']);
    }
  }

  iniciarSesion() {
    if (this.usuario === 'bibliotecario' && this.contrasena === 'biblio123') {
      localStorage.setItem('sesionBiblioteca', 'activa');
      this.router.navigate(['/libros']);
    } else {
      this.mensaje = 'Usuario o contraseña incorrectos';
    }
  }
}