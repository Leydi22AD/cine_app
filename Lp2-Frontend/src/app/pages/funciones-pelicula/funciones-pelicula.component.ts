import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-funciones-pelicula',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './funciones-pelicula.component.html',
  styleUrl: './funciones-pelicula.component.css'
})
export class FuncionesPeliculaComponent implements OnInit {
  pelicula: any = null;
  funciones: any[] = [];
  currentUser: any;
  peliculaId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    private authService: AuthService
  ) {
    this.currentUser = this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.peliculaId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarPelicula();
    this.cargarFunciones();
  }

  cargarPelicula() {
    this.eventService.getPeliculas().subscribe({
      next: (data) => {
        this.pelicula = data.find((p: any) => p.idPelicula === this.peliculaId);
      },
      error: (error) => {
        console.error('Error al cargar película:', error);
      }
    });
  }

  cargarFunciones() {
    this.eventService.getFunciones().subscribe({
      next: (data) => {
        console.log('Todas las funciones:', data);
        this.funciones = data
          .filter((f: any) => {
            const match = f.peliculaId === this.peliculaId || f.pelicula?.idPelicula === this.peliculaId;
            console.log('Comparando función:', f, 'con peliculaId:', this.peliculaId, 'match:', match);
            return match;
          })
          .map((f: any) => ({
            ...f,
            estaExpirada: this.verificarSiFuncionExpirada(f)
          }));
        console.log('Funciones filtradas:', this.funciones);
      },
      error: (error) => {
        console.error('Error al cargar funciones:', error);
      }
    });
  }

  verificarSiFuncionExpirada(funcion: any): boolean {
    if (!funcion.fecha || !funcion.horario) return false;
    
    try {
      const fechaHoraStr = `${funcion.fecha}T${funcion.horario}`;
      const fechaHoraFuncion = new Date(fechaHoraStr);
      const ahora = new Date();
      return fechaHoraFuncion < ahora;
    } catch (error) {
      console.error('Error al verificar fecha de función:', error);
      return false;
    }
  }

  seleccionarFuncion(funcion: any) {
    if (funcion.estaExpirada) {
      return; // No hacer nada si la función está expirada
    }
    console.log('Navegando a asientos con función:', funcion);
    const funcionId = funcion.id || funcion.idFuncion;
    console.log('ID de función:', funcionId);
    this.router.navigate(['/user/asientos', funcionId]);
  }

  volver() {
    this.router.navigate(['/user/home']);
  }

  logout() {
    this.authService.logout();
  }
}
