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
    if (localStorage.getItem('sesionTareas')) {
      this.router.navigate(['/tareas']);
    }
  }

  iniciarSesion() {
    if (this.usuario === 'usuario' && this.contrasena === 'tareas123') {
      localStorage.setItem('sesionTareas', 'activa');
      this.router.navigate(['/tareas']);
    } else {
      this.mensaje = 'Usuario o contraseña incorrectos';
    }
  }
}