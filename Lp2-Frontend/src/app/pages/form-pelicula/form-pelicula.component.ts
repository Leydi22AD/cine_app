import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { EventService } from '../../services/event.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-form-pelicula',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-pelicula.component.html',
  styleUrls: ['./form-pelicula.component.css']
})
export class FormPeliculaComponent implements OnInit {
  private eventService = inject(EventService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  peliculaId: number | null = null;
  modoEdicion = false;

  pelicula = {
    titulo: '',
    genero: '',
    duracion: null,
    formato: '',
    idioma: '',
    poster: '',
    director: '',
    descripcion: '',
    trailer: ''
  };

  ngOnInit() {
    // Verificar si estamos en modo edición
    this.route.queryParams.subscribe(params => {
      if (params['id']) {
        this.peliculaId = +params['id'];
        this.modoEdicion = true;
        this.cargarPelicula(this.peliculaId);
      }
    });
  }

  cargarPelicula(id: number) {
    this.eventService.getPeliculas().subscribe({
      next: (peliculas) => {
        const peliculaEncontrada = peliculas.find(p => (p.idPelicula || p.id) === id);
        if (peliculaEncontrada) {
          this.pelicula = {
            titulo: peliculaEncontrada.titulo || '',
            genero: peliculaEncontrada.genero || '',
            duracion: peliculaEncontrada.duracion || null,
            formato: peliculaEncontrada.formato || '',
            idioma: peliculaEncontrada.idioma || '',
            poster: peliculaEncontrada.poster || '',
            director: peliculaEncontrada.director || '',
            descripcion: peliculaEncontrada.descripcion || '',
            trailer: peliculaEncontrada.trailer || ''
          };
        }
      },
      error: (error) => {
        console.error('Error al cargar película:', error);
        Swal.fire('Error', 'No se pudo cargar la película', 'error');
      }
    });
  }

  guardarPelicula() {
    if (!this.pelicula.titulo || !this.pelicula.genero || !this.pelicula.duracion) {
      Swal.fire('Campos incompletos', 'Por favor complete los campos obligatorios', 'warning');
      return;
    }

    // Preparar datos asegurando tipos correctos
    const peliculaData = {
      titulo: this.pelicula.titulo,
      genero: this.pelicula.genero,
      duracion: Number(this.pelicula.duracion),
      formato: this.pelicula.formato || null,
      idioma: this.pelicula.idioma || null,
      poster: this.pelicula.poster || null,
      director: this.pelicula.director || null,
      descripcion: this.pelicula.descripcion || null,
      trailer: this.pelicula.trailer || null
    };

    if (this.modoEdicion && this.peliculaId) {
      // Actualizar película existente
      this.eventService.updatePelicula(this.peliculaId, peliculaData).subscribe({
        next: (response) => {
          console.log('Película actualizada:', response);
          Swal.fire({
            title: '¡Éxito!',
            text: 'Película actualizada correctamente',
            icon: 'success',
            confirmButtonColor: '#198754'
          }).then(() => {
            this.router.navigate(['/panel-control']);
          });
        },
        error: (error) => {
          console.error('Error al actualizar película:', error);
          Swal.fire('Error', 'No se pudo actualizar la película', 'error');
        }
      });
    } else {
      // Crear nueva película
      this.eventService.createPelicula(peliculaData).subscribe({
        next: (response) => {
          console.log('Película guardada:', response);
          Swal.fire({
            title: '¡Éxito!',
            text: 'Película registrada correctamente',
            icon: 'success',
            confirmButtonColor: '#198754'
          }).then(() => {
            this.router.navigate(['/home']);
          });
        },
        error: (error) => {
          console.error('Error al guardar película:', error);
          Swal.fire('Error', 'No se pudo guardar la película', 'error');
        }
      });
    }
  }
}

