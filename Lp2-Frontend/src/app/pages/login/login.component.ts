import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  credentials = {
    email: '',
    password: ''
  };

  errorMessage = '';
  isLoading = false;

  login() {
    if (!this.credentials.email || !this.credentials.password) {
      this.errorMessage = 'Por favor complete todos los campos';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response.usuario);
        this.isLoading = false;
        
        // El token y usuario ya fueron guardados en el AuthService
        
        // Redirigir según el rol
        if (response.usuario.rol === 'ADMIN') {
          this.router.navigate(['/home']);
        } else {
          this.router.navigate(['/user/home']); // Ruta para usuarios CLIENTE
        }
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error en login:', error);
        
        if (error.status === 401) {
          this.errorMessage = 'Credenciales inválidas. Verifique su email y contraseña.';
        } else if (error.status === 0) {
          this.errorMessage = 'No se puede conectar al servidor. Verifique que el backend esté corriendo.';
        } else {
          this.errorMessage = 'Error al iniciar sesión. Verifique sus credenciales.';
        }
      }
    });
  }
}
