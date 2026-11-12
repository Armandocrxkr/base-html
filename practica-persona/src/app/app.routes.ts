import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { PersonaComponent } from './persona/persona.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'persona', component: PersonaComponent }
];
