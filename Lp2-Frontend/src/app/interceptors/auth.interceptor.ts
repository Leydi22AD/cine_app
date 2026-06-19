import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  // Clonar la petición y agregar el token si existe
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // Manejar errores de autenticación
  return next(authReq).pipe(
    catchError((error) => {
      if (error.status === 401) {
        // Token expirado o inválido
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
