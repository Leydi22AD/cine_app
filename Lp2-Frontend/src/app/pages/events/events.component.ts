import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EventService } from '../../services/event.service';
import Swal from 'sweetalert2';

declare var bootstrap: any;

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {
  // ======= VARIABLES =======
  searchEventName = '';
  selectedEventForModal: any = null;
  private imageModal: any;

  allEvents: any[] = [];
  filteredEvents: any[] = [];

  funciones: any[] = [];
  peliculas: any[] = [];
  salas: any[] = [];
  tickets: any[] = [];

  private eventService = inject(EventService);
  private router = inject(Router);

  // ======= CICLO DE VIDA =======
  ngOnInit(): void {
    this.loadEvents();
    this.cargarFunciones();
    this.cargarPeliculas();
    this.cargarSalas();
    this.cargarTickets();

    // 🔁 Detectar nuevas funciones creadas
    window.addEventListener('storage', (event) => {
      if (event.key === 'funcion_nueva') {
        console.log('Se detectó una nueva función, recargando...');
        this.cargarFunciones();
      }
    });
  }

  ngAfterViewInit(): void {
    const modalElement = document.getElementById('imagePreviewModal');
    if (modalElement) {
      this.imageModal = new bootstrap.Modal(modalElement);
    }
  }

  // ======= CARGA DE DATOS =======
  loadEvents(): void {
    this.eventService.getEvents().subscribe({
      next: (events) => {
        this.allEvents = events;
        this.filteredEvents = events;
      },
      error: (err) => console.error('Error al cargar eventos:', err)
    });
  }

  cargarFunciones(): void {
    this.eventService.getFunciones().subscribe({
      next: (data) => (this.funciones = data),
      error: (err) => console.error('Error al cargar funciones:', err)
    });
  }

  cargarPeliculas(): void {
    this.eventService.getPeliculas().subscribe({
      next: (data) => (this.peliculas = data),
      error: (err) => console.error('Error al cargar películas:', err)
    });
  }

  cargarSalas(): void {
    this.eventService.getSalas().subscribe({
      next: (data) => (this.salas = data),
      error: (err) => console.error('Error al cargar salas:', err)
    });
  }

  cargarTickets(): void {
    this.eventService.getTickets().subscribe({
      next: (data) => (this.tickets = data),
      error: (err) => console.error('Error al cargar tickets:', err)
    });
  }

  // ======= BÚSQUEDA =======
  searchEvent(): void {
    if (!this.searchEventName) {
      this.filteredEvents = this.allEvents;
      return;
    }
    this.filteredEvents = this.allEvents.filter((event) =>
      event.name.toLowerCase().includes(this.searchEventName.toLowerCase())
    );
  }

  // ======= NAVEGACIÓN =======
  goToNewEvent(): void {
    this.router.navigate(['/event/new']);
  }

  // ======= IMAGEN =======
  onFileSelected(event: Event, eventId: number): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.eventService.uploadEventImage(eventId, file).subscribe({
        next: (updatedEvent) => {
          const index = this.allEvents.findIndex((e) => e.id === eventId);
          if (index !== -1) {
            this.allEvents[index] = updatedEvent;
            this.searchEvent();
          }
        },
        error: (err) => console.error('Error al subir imagen:', err)
      });
    }
  }

  openImageModal(event: any): void {
    this.selectedEventForModal = event;
    if (this.imageModal) this.imageModal.show();
  }

  // ======= ELIMINAR EVENTO =======
  deleteEvent(eventId: number, eventName: string): void {
    Swal.fire({
      title: '¿Eliminar evento?',
      text: `Se eliminará "${eventName}" permanentemente.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eventService.deleteEvent(eventId).subscribe({
          next: () => {
            this.allEvents = this.allEvents.filter((e) => e.id !== eventId);
            this.filteredEvents = this.filteredEvents.filter((e) => e.id !== eventId);
            Swal.fire('Eliminado', 'El evento fue eliminado.', 'success');
          },
          error: (err) => Swal.fire('Error', 'No se pudo eliminar.', 'error')
        });
      }
    });
  }
}
