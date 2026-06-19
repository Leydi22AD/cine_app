import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { EventService } from '../../services/event.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-form-sala',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-sala.component.html',
  styleUrls: ['./form-sala.component.css']
})
export class FormSalaComponent implements OnInit {
  private eventService = inject(EventService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  salaId: number | null = null;
  modoEdicion = false;

  sala = {
    numero: null,
    filas: null,
    columnas: null
  };

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.salaId = +params['id'];
        this.modoEdicion = true;
        this.cargarSala(this.salaId);
      }
    });
  }

  cargarSala(id: number) {
    this.eventService.getSalas().subscribe({
      next: (salas) => {
        const salaEncontrada = salas.find(s => (s.idSala || s.id) === id);
        if (salaEncontrada) {
          this.sala = {
            numero: salaEncontrada.numero || null,
            filas: salaEncontrada.filas || null,
            columnas: salaEncontrada.columnas || null
          };
        }
      },
      error: (error) => {
        console.error('Error al cargar sala:', error);
        Swal.fire('Error', 'No se pudo cargar la sala', 'error');
      }
    });
  }

  guardarSala() {
    if (!this.sala.numero || !this.sala.filas || !this.sala.columnas) {
      Swal.fire('Campos incompletos', 'Por favor complete todos los campos', 'warning');
      return;
    }

    if (this.modoEdicion && this.salaId) {
      // Actualizar sala existente
      this.eventService.updateSala(this.salaId, this.sala).subscribe({
        next: (response) => {
          console.log('Sala actualizada:', response);
          Swal.fire({
            title: '¡Éxito!',
            text: 'Sala actualizada correctamente',
            icon: 'success',
            confirmButtonColor: '#198754'
          }).then(() => {
            this.router.navigate(['/panel-control']);
          });
        },
        error: (error) => {
          console.error('Error al actualizar sala:', error);
          Swal.fire('Error', 'No se pudo actualizar la sala', 'error');
        }
      });
    } else {
      // Crear nueva sala
      this.eventService.createSala(this.sala).subscribe({
        next: (response) => {
          console.log('Sala guardada:', response);
          Swal.fire({
            title: '¡Éxito!',
            text: 'Sala registrada correctamente',
            icon: 'success',
            confirmButtonColor: '#198754'
          }).then(() => {
            this.router.navigate(['/home']);
          });
        },
        error: (error) => {
          console.error('Error al guardar sala:', error);
          Swal.fire('Error', 'No se pudo guardar la sala', 'error');
        }
      });
    }
  }
}
