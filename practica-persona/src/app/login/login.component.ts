import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  imports: [FormsModule],
  selector: 'app-login',
  styleUrl: './login.component.css',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  usuario: string = "";
  password: string = "";
  mensaje: string = "";


  constructor(private router: Router) {}
  
  iniciarSesion(){
    if(this.usuario === "admin" && this.password === "1234"){
      localStorage.setItem("sesion", "activa");
      this.router.navigate(['/persona']);
    }
    else{
      this.mensaje = "Usuario o contraseña incorrecto";
    }
  }
}



