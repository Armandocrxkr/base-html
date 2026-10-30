import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LibroComponent } from './libro/libro.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'libros', component: LibroComponent }
];