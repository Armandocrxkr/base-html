import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { EstudianteComponent } from './estudiante/estudiante.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'estudiantes', component: EstudianteComponent }
];