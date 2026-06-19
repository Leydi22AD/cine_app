import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EventService } from '../../services/event.service';

@Component({
  selector: 'app-form-ticket',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-ticket.component.html',
  styleUrls: ['./form-ticket.component.css']
})
export class FormTicketComponent implements OnInit {
  private eventService = inject(EventService);
  private router = inject(Router);

  funciones: any[] = [];
  
  ticket = {
    idFuncion: null,
    idAsiento: null,
    idCliente: null,
    precioUnitario: null,
    fechaCompra: new Date().toISOString().slice(0, 16)
  };

  ngOnInit() {
    this.cargarFunciones();
  }

  cargarFunciones() {
    this.eventService.getFunciones().subscribe({
      next: (data) => {
        this.funciones = data;
      },
      error: (err) => console.error('Error al cargar funciones:', err)
    });
  }

  guardarTicket() {
    if (!this.ticket.idFuncion || !this.ticket.precioUnitario) {
      alert('Por favor complete los campos obligatorios');
      return;
    }

    this.eventService.createTicket(this.ticket).subscribe({
      next: (response) => {
        console.log('Ticket guardado:', response);
        alert('Ticket registrado correctamente en la base de datos');
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error('Error al guardar ticket:', error);
        alert('Error al guardar el ticket. Verifique que el backend esté corriendo.');
      }
    });
  }
}
