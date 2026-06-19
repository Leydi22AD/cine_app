import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  usuario = {
    nombre: '',
    email: '',
    password: ''
  };

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onRegister() {
    if (!this.usuario.nombre || !this.usuario.email || !this.usuario.password) {
      alert('Por favor complete todos los campos');
      return;
    }

    console.log('Enviando registro:', this.usuario);

    this.authService.register(this.usuario).subscribe({
      next: (response) => {
        console.log('Registro exitoso:', response);
        alert('Registro exitoso. Por favor inicia sesión.');
        // No guardar el token, redirigir a login
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Error completo en registro:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        
        if (error.status === 409) {
          alert('Error: El email ya está registrado.');
        } else if (error.status === 500) {
          alert('Error del servidor. Por favor intenta más tarde.');
        } else if (error.status === 0) {
          alert('No se puede conectar al servidor. Verifica que esté corriendo en el puerto 8082.');
        } else {
          alert('Error al registrar usuario: ' + (error.error?.message || error.message || 'Error desconocido'));
        }
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
