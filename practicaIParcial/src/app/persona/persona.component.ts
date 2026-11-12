import { Component } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { FormsModule } from '@angular/forms';

@Component({
  imports: [FormsModule, LoginComponent],
  selector: 'app-persona',
  styleUrl: './persona.component.css',
  templateUrl: './persona.component.html',
})
export class PersonaComponent {}
