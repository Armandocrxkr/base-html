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
    if (localStorage.getItem('sesionEstudiantes')) {
      this.router.navigate(['/estudiantes']);
    }
  }

  iniciarSesion() {
    if (this.usuario === 'registro' && this.contrasena === 'alumnos123') {
      localStorage.setItem('sesionEstudiantes', 'activa');
      this.router.navigate(['/estudiantes']);
    } else {
      this.mensaje = 'Usuario o contraseña incorrectos';
    }
  }
}