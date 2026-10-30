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
    if (localStorage.getItem('sesionTienda')) {
      this.router.navigate(['/productos']);
    }
  }

  iniciarSesion() {
    if (this.usuario === 'vendedor' && this.contrasena === 'tienda123') {
      localStorage.setItem('sesionTienda', 'activa');
      this.router.navigate(['/productos']);
    } else {
      this.mensaje = 'Usuario o contraseña incorrectos';
    }
  }
}