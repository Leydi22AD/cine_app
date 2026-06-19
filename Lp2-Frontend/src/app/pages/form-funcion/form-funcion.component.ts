import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { EventService } from '../../services/event.service';

@Component({
  selector: 'app-form-funcion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-funcion.component.html',
  styleUrls: ['./form-funcion.component.css']
})
export class FormFuncionComponent implements OnInit {
  private eventService = inject(EventService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  funcionId: number | null = null;
  modoEdicion = false;

  peliculas: any[] = [];
  salas: any[] = [];
  fechaMinima: string = '';

  funcion = {
    idPelicula: null,
    idSala: null,
    fecha: '',
    horario: '',
    precio: null
  };

  ngOnInit() {
    this.establecerFechaMinima();
    this.cargarPeliculas();
    this.cargarSalas();
    
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.funcionId = +params['id'];
        this.modoEdicion = true;
        // Esperar a que se carguen películas y salas antes de cargar la función
        setTimeout(() => this.cargarFuncion(this.funcionId!), 500);
      }
    });
  }

  establecerFechaMinima() {
    const hoy = new Date();
    this.fechaMinima = hoy.toISOString().split('T')[0];
  }

  cargarPeliculas() {
    this.eventService.getPeliculas().subscribe({
      next: (data) => {
        this.peliculas = data;
      },
      error: (err) => console.error('Error al cargar películas:', err)
    });
  }

  cargarSalas() {
    this.eventService.getSalas().subscribe({
      next: (data) => {
        this.salas = data;
      },
      error: (err) => console.error('Error al cargar salas:', err)
    });
  }

  cargarFuncion(id: number) {
    this.eventService.getFunciones().subscribe({
      next: (funciones) => {
        const funcionEncontrada = funciones.find(f => (f.idFuncion || f.id) === id);
        if (funcionEncontrada) {
          // Extraer fecha y hora del campo fechaHora
          let fechaStr = '';
          let horarioStr = '';
          
          if (funcionEncontrada.fechaHora) {
            const fecha = new Date(funcionEncontrada.fechaHora);
            fechaStr = fecha.toISOString().split('T')[0];
            horarioStr = fecha.toTimeString().substring(0, 5);
          }

          this.funcion = {
            idPelicula: funcionEncontrada.pelicula?.idPelicula || funcionEncontrada.pelicula?.id || null,
            idSala: funcionEncontrada.sala?.idSala || funcionEncontrada.sala?.id || null,
            fecha: fechaStr,
            horario: horarioStr,
            precio: funcionEncontrada.precio || null
          };
        }
      },
      error: (error) => {
        console.error('Error al cargar función:', error);
        Swal.fire('Error', 'No se pudo cargar la función', 'error');
      }
    });
  }

  guardarFuncion() {
    if (!this.funcion.idPelicula || !this.funcion.idSala || !this.funcion.fecha || !this.funcion.horario || !this.funcion.precio) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Por favor, llena todos los campos antes de guardar.'
      });
      return;
    }

    const funcionData = {
      peliculaId: this.funcion.idPelicula,
      salaId: this.funcion.idSala,
      fecha: this.funcion.fecha,
      horario: this.funcion.horario,
      precio: this.funcion.precio
    };

    if (this.modoEdicion && this.funcionId) {
      // Actualizar función existente
      this.eventService.updateFuncion(this.funcionId, funcionData).subscribe({
        next: (response) => {
          console.log('✅ Función actualizada:', response);
          Swal.fire({
            icon: 'success',
            title: 'Función actualizada',
            text: 'La función se ha actualizado correctamente.',
            confirmButtonColor: '#198754'
          }).then(() => {
            this.router.navigate(['/panel-control']);
          });
        },
        error: (err) => {
          console.error('❌ Error al actualizar:', err);
          const errorMsg = err.error?.message || err.message || 'No se pudo actualizar la función';
          Swal.fire({
            icon: 'error',
            title: 'Error al actualizar',
            text: errorMsg
          });
        }
      });
    } else {
      // Crear nueva función
      this.eventService.createFuncion(funcionData).subscribe({
        next: (response) => {
          console.log('✅ Función creada con éxito:', response);
          Swal.fire({
            icon: 'success',
            title: 'Función registrada',
            text: 'La función se ha guardado correctamente en la base de datos.',
            confirmButtonColor: '#28a745'
          }).then(() => {
            this.router.navigate(['/home']);
          });
        },
        error: (err) => {
          console.error('❌ Error al guardar la función:', err);
          const errorMsg = err.error?.message || err.message || 'No se pudo registrar la función';
          Swal.fire({
            icon: 'error',
            title: 'Error al guardar',
            text: errorMsg
          });
        }
      });
    }
  }
}
