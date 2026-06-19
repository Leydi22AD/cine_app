import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

interface AuthResponse {
  token: string;
  usuario: {
    id: number;
    nombre: string;
    email: string;
    rol: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = 'http://localhost:8082/api/v1/auth';
  
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    // Cargar usuario desde localStorage al iniciar
    const user = localStorage.getItem('currentUser');
    if (user) {
      this.currentUserSubject.next(JSON.parse(user));
    }
  }

  login(credentials: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // Guardar token y usuario en localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('currentUser', JSON.stringify(response.usuario));
        this.currentUserSubject.next(response.usuario);
      })
    );
  }

  register(usuario: { nombre: string; email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, usuario).pipe(
      tap(response => {
        // Guardar token y usuario en localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('currentUser', JSON.stringify(response.usuario));
        this.currentUserSubject.next(response.usuario);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): any {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user && user.rol === 'ADMIN';
  }

  isCliente(): boolean {
    const user = this.getCurrentUser();
    return user && user.rol === 'CLIENTE';
  }
}
