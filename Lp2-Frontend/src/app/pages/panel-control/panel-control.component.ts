import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-panel-control',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panel-control.component.html',
  styleUrl: './panel-control.component.css'
})
export class PanelControlComponent implements OnInit {
  peliculas: any[] = [];
  salas: any[] = [];
  funciones: any[] = [];
  tickets: any[] = [];
  
  stats = {
    totalPeliculas: 0,
    totalSalas: 0,
    totalFunciones: 0,
    totalTickets: 0,
    funcionesActivas: 0,
    funcionesExpiradas: 0,
    ingresosTotales: 0
  };

  vistaActual: 'peliculas' | 'salas' | 'funciones' | 'tickets' = 'peliculas';

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    public router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    if (!user || user.rol !== 'ADMIN') {
      Swal.fire({
        title: 'Acceso denegado',
        text: 'No tienes permisos para acceder al panel de control',
        icon: 'error',
        confirmButtonColor: '#198754'
      }).then(() => {
        this.router.navigate(['/home']);
      });
      return;
    }

    this.cargarDatos();
  }

  cargarDatos() {
    // Cargar películas
    this.eventService.getPeliculas().subscribe({
      next: (data) => {
        this.peliculas = data;
        this.stats.totalPeliculas = data.length;
      },
      error: (error) => console.error('Error al cargar películas:', error)
    });

    // Cargar salas
    this.eventService.getSalas().subscribe({
      next: (data) => {
        this.salas = data;
        this.stats.totalSalas = data.length;
      },
      error: (error) => console.error('Error al cargar salas:', error)
    });

    // Cargar funciones
    this.eventService.getFunciones().subscribe({
      next: (data) => {
        this.funciones = data.map(f => ({
          ...f,
          estaExpirada: this.verificarSiFuncionExpirada(f)
        }));
        this.stats.totalFunciones = data.length;
        this.stats.funcionesActivas = this.funciones.filter(f => !f.estaExpirada).length;
        this.stats.funcionesExpiradas = this.funciones.filter(f => f.estaExpirada).length;
      },
      error: (error) => console.error('Error al cargar funciones:', error)
    });

    // Cargar tickets
    this.eventService.getTickets().subscribe({
      next: (data) => {
        console.log('Tickets recibidos del backend:', data);
        this.tickets = data;
        this.stats.totalTickets = data.length;
        this.stats.ingresosTotales = data.reduce((sum, t) => sum + (t.precioUnitario || 0), 0);
      },
      error: (error) => console.error('Error al cargar tickets:', error)
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

  cambiarVista(vista: 'peliculas' | 'salas' | 'funciones' | 'tickets') {
    this.vistaActual = vista;
  }

  // Métodos de edición
  editarPelicula(id: number) {
    // Aquí puedes navegar a un formulario de edición o abrir un modal
    Swal.fire({
      title: 'Editar Película',
      text: 'Redirigiendo al formulario de edición...',
      icon: 'info',
      timer: 1500,
      showConfirmButton: false
    });
    // Por ahora, reutilizamos el formulario de registro
    // En el futuro, podrías crear una ruta específica para edición
    this.router.navigate(['/registrar-pelicula'], { queryParams: { id: id } });
  }

  editarSala(id: number) {
    Swal.fire({
      title: 'Editar Sala',
      text: 'Redirigiendo al formulario de edición...',
      icon: 'info',
      timer: 1500,
      showConfirmButton: false
    });
    this.router.navigate(['/registrar-sala'], { queryParams: { id: id } });
  }

  editarFuncion(id: number) {
    Swal.fire({
      title: 'Editar Función',
      text: 'Redirigiendo al formulario de edición...',
      icon: 'info',
      timer: 1500,
      showConfirmButton: false
    });
    this.router.navigate(['/registrar-funcion'], { queryParams: { id: id } });
  }

  // Métodos de eliminación
  eliminarPelicula(id: number) {
    Swal.fire({
      title: '¿Eliminar película?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eventService.deletePelicula(id).subscribe({
          next: () => {
            Swal.fire('Eliminada', 'La película ha sido eliminada', 'success');
            this.cargarDatos();
          },
          error: (error) => {
            console.error('Error al eliminar película:', error);
            Swal.fire('Error', 'No se pudo eliminar la película', 'error');
          }
        });
      }
    });
  }

  eliminarSala(id: number) {
    Swal.fire({
      title: '¿Eliminar sala?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eventService.deleteSala(id).subscribe({
          next: () => {
            Swal.fire('Eliminada', 'La sala ha sido eliminada', 'success');
            this.cargarDatos();
          },
          error: (error) => {
            console.error('Error al eliminar sala:', error);
            Swal.fire('Error', 'No se pudo eliminar la sala', 'error');
          }
        });
      }
    });
  }

  eliminarFuncion(id: number) {
    Swal.fire({
      title: '¿Eliminar función?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eventService.deleteFuncion(id).subscribe({
          next: () => {
            Swal.fire('Eliminada', 'La función ha sido eliminada', 'success');
            this.cargarDatos();
          },
          error: (error) => {
            console.error('Error al eliminar función:', error);
            Swal.fire('Error', 'No se pudo eliminar la función', 'error');
          }
        });
      }
    });
  }

  irARegistroPelicula() {
    this.router.navigate(['/registrar-pelicula']);
  }

  irARegistroSala() {
    this.router.navigate(['/registrar-sala']);
  }

  irARegistroFuncion() {
    this.router.navigate(['/registrar-funcion']);
  }
}
