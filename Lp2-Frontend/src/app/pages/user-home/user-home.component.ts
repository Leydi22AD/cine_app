import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { EventService } from '../../services/event.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-user-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-home.component.html',
  styleUrl: './user-home.component.css'
})
export class UserHomeComponent implements OnInit {
  peliculas: any[] = [];
  currentUser: any;

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUser = this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.cargarPeliculas();
  }

  cargarPeliculas() {
    this.eventService.getPeliculas().subscribe({
      next: (data) => {
        this.peliculas = data;
      },
      error: (error) => {
        console.error('Error al cargar películas:', error);
      }
    });
  }

  verFunciones(pelicula: any) {
    this.router.navigate(['/user/funciones', pelicula.idPelicula]);
  }

  logout() {
    this.authService.logout();
  }
}
