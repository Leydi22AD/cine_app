import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-mis-tickets',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-tickets.component.html',
  styleUrl: './mis-tickets.component.css'
})
export class MisTicketsComponent implements OnInit {
  tickets: any[] = [];
  currentUser: any;
  loading: boolean = true;

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUser = this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.cargarMisTickets();
  }

  cargarMisTickets() {
    const usuarioId = this.currentUser.id || this.currentUser.idUsuario;
    
    this.eventService.getTickets().subscribe({
      next: (tickets) => {
        console.log('Tickets recibidos:', tickets);
        console.log('Usuario actual ID:', usuarioId);
        
        // Filtrar tickets del usuario actual
        this.tickets = tickets.filter((ticket: any) => {
          const ticketUsuarioId = ticket.cliente?.idUsuario || ticket.cliente?.id;
          console.log('Comparando:', ticketUsuarioId, 'con', usuarioId);
          return ticketUsuarioId === usuarioId;
        });
        
        console.log('Tickets filtrados:', this.tickets);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar tickets:', error);
        this.loading = false;
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  volverAlInicio() {
    this.router.navigate(['/user/home']);
  }
}
