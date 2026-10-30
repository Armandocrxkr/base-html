import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { TareaComponent } from './tarea/tarea.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'tareas', component: TareaComponent }
];