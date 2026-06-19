import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-seleccion-asientos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seleccion-asientos.component.html',
  styleUrl: './seleccion-asientos.component.css'
})
export class SeleccionAsientosComponent implements OnInit {
  funcion: any = null;
  sala: any = null;
  currentUser: any;
  funcionId: number = 0;
  
  // Para mapa visual de asientos
  matrizAsientos: any[][] = [];
  asientosSeleccionados: any[] = [];
  letrasFila: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'];
  asientosDisponibles: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    private authService: AuthService
  ) {
    this.currentUser = this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.funcionId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarFuncion();
  }

  cargarFuncion() {
    this.eventService.getFunciones().subscribe({
      next: (data) => {
        this.funcion = data.find((f: any) => 
          f.id === this.funcionId || f.idFuncion === this.funcionId
        );
        if (this.funcion && this.funcion.sala) {
          this.sala = this.funcion.sala;
          this.cargarAsientos();
        }
      },
      error: (error) => {
        console.error('Error al cargar función:', error);
        Swal.fire('Error', 'No se pudo cargar la información de la función', 'error');
      }
    });
  }

  cargarAsientos() {
    const salaId = this.sala.idSala || this.sala.id;
    
    this.eventService.getAsientosPorSala(salaId).subscribe({
      next: (asientos) => {
        console.log('Asientos cargados:', asientos);
        
        if (asientos && asientos.length > 0) {
          this.construirMatrizAsientos(asientos);
          this.calcularAsientosDisponibles(asientos);
        } else {
          console.warn('No se encontraron asientos para la sala');
        }
      },
      error: (error) => {
        console.error('Error al cargar asientos:', error);
        Swal.fire('Error', 'No se pudieron cargar los asientos', 'error');
      }
    });
  }

  construirMatrizAsientos(asientos: any[]) {
    this.matrizAsientos = [];
    const filas = this.sala.filas;
    const columnas = this.sala.columnas;

    for (let fila = 1; fila <= filas; fila++) {
      const asientosFila = asientos
        .filter(a => a.fila === fila)
        .sort((a, b) => a.columna - b.columna)
        .map(a => ({
          ...a,
          seleccionado: false
        }));
      
      if (asientosFila.length > 0) {
        this.matrizAsientos.push(asientosFila);
      }
    }
  }

  calcularAsientosDisponibles(asientos: any[]) {
    this.asientosDisponibles = asientos.filter(a => a.estado === 'LIBRE').length;
  }

  toggleAsiento(asiento: any) {
    if (asiento.estado === 'OCUPADO') {
      Swal.fire({
        title: 'Asiento ocupado',
        text: 'Este asiento ya está reservado',
        icon: 'warning',
        confirmButtonColor: '#198754'
      });
      return;
    }

    asiento.seleccionado = !asiento.seleccionado;

    if (asiento.seleccionado) {
      this.asientosSeleccionados.push(asiento);
    } else {
      this.asientosSeleccionados = this.asientosSeleccionados.filter(
        a => a.idAsiento !== asiento.idAsiento && a.id !== asiento.id
      );
    }
  }

  confirmarCompra() {
    if (this.asientosSeleccionados.length === 0) {
      Swal.fire({
        title: 'Selecciona asientos',
        text: 'Debes seleccionar al menos un asiento',
        icon: 'warning',
        confirmButtonColor: '#198754'
      });
      return;
    }

    const total = this.asientosSeleccionados.length * this.funcion.precio;

    Swal.fire({
      title: '¿Confirmar Ticket?',
      html: `
        <p><strong>Película:</strong> ${this.funcion.pelicula?.titulo || 'N/A'}</p>
        <p><strong>Sala:</strong> ${this.sala.numero}</p>
        <p><strong>Asientos:</strong> ${this.asientosSeleccionados.map(a => 
          this.letrasFila[a.fila - 1] + a.columna
        ).join(', ')}</p>
        <p><strong>Total:</strong> S/ ${total.toFixed(2)}</p>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#d32f2f',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, generar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.crearTickets();
      }
    });
  }

  crearTickets() {
    let ticketsCreados = 0;
    const totalTickets = this.asientosSeleccionados.length;

    this.asientosSeleccionados.forEach((asiento) => {
      const ticketData = {
        clienteId: this.currentUser.id || this.currentUser.idUsuario,
        funcionId: this.funcionId,
        asientoId: asiento.idAsiento || asiento.id
      };

      this.eventService.createTicket(ticketData).subscribe({
        next: () => {
          // Actualizar estado del asiento a OCUPADO
          const asientoUpdateData = { ...asiento, estado: 'OCUPADO' };
          this.eventService.actualizarAsiento(asiento.idAsiento || asiento.id, asientoUpdateData).subscribe({
            next: () => {
              ticketsCreados++;
              
              if (ticketsCreados === totalTickets) {
                Swal.fire({
                  title: '¡Ticket generado!',
                  text: `Se ${ticketsCreados === 1 ? 'ha generado' : 'han generado'} ${ticketsCreados} ticket(s) exitosamente`,
                  icon: 'success',
                  confirmButtonColor: '#d32f2f'
                }).then(() => {
                  this.router.navigate(['/user/home']);
                });
              }
            },
            error: (error) => {
              console.error('Error al actualizar asiento:', error);
            }
          });
        },
        error: (error) => {
          console.error('Error al crear ticket:', error);
          Swal.fire('Error', 'No se pudo completar la compra', 'error');
        }
      });
    });
  }

  volver() {
    this.router.navigate(['/user/funciones', this.funcion.pelicula?.idPelicula || this.funcion.peliculaId]);
  }
}
