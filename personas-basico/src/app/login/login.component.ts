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
  clave = '';

  constructor(private router: Router) {}

  iniciarSesion() {
    if (this.usuario === 'admin' && this.clave === '123456') {
      localStorage.setItem('sesionBasica', 'si');
      this.router.navigate(['/persona']);
    } else {
      alert('Usuario o contraseña incorrectos');
    }
  }
}