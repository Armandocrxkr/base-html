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
    if (localStorage.getItem('sesionVeterinaria')) {
      this.router.navigate(['/mascotas']);
    }
  }

  iniciarSesion() {
    if (this.usuario === 'veterinario' && this.contrasena === 'mascotas123') {
      localStorage.setItem('sesionVeterinaria', 'activa');
      this.router.navigate(['/mascotas']);
    } else {
      this.mensaje = 'Usuario o contraseña incorrectos';
    }
  }
}