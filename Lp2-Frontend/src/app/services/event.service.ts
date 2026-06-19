import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IEvent } from '../interfaces/event.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private http = inject(HttpClient);

  // 👇 URL base de tu API
  private apiUrl = 'http://localhost:8082/api/v1'; // ajusta el puerto según tu backend

  constructor() {}

  // ==========================
  // EVENTOS
  // ==========================
  getEvents(): Observable<IEvent[]> {
    return this.http.get<IEvent[]>(`${this.apiUrl}/events`);
  }

  createEvent(eventData: Partial<IEvent>): Observable<IEvent> {
    return this.http.post<IEvent>(`${this.apiUrl}/events`, eventData);
  }

  deleteEvent(eventId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/events/${eventId}`);
  }

  updateEvent(eventId: number, eventData: Partial<IEvent>): Observable<IEvent> {
    return this.http.put<IEvent>(`${this.apiUrl}/events/${eventId}`, eventData);
  }

  uploadEventImage(eventId: number, file: File): Observable<IEvent> {
    const formData = new FormData();
    formData.append('file', file); // esta clave debe coincidir con @RequestParam("file") del backend
    return this.http.post<IEvent>(`${this.apiUrl}/events/${eventId}/upload-image`, formData);
  }

  getEventById(eventId: number): Observable<IEvent> {
    return this.http.get<IEvent>(`${this.apiUrl}/events/id/${eventId}`);
  }

  // ==========================
  // FUNCIONES
  // ==========================
  getFunciones(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/funciones`);
  }

  createFuncion(funcionData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/funciones`, funcionData);
  }

  updateFuncion(id: number, funcionData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/funciones/${id}`, funcionData);
  }

  deleteFuncion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/funciones/${id}`);
  }

  // ==========================
  // PELÍCULAS
  // ==========================
  getPeliculas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/peliculas`);
  }

  createPelicula(peliculaData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/peliculas`, peliculaData);
  }

  updatePelicula(id: number, peliculaData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/peliculas/${id}`, peliculaData);
  }

  deletePelicula(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/peliculas/${id}`);
  }

  // ==========================
  // SALAS
  // ==========================
  getSalas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/salas`);
  }

  createSala(salaData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/salas`, salaData);
  }

  updateSala(id: number, salaData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/salas/${id}`, salaData);
  }

  deleteSala(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/salas/${id}`);
  }

  // ==========================
  // TICKETS
  // ==========================
  getTickets(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tickets`);
  }

  createTicket(ticketData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/tickets`, ticketData);
  }

  deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/tickets/${id}`);
  }

  // ==========================
  // ASIENTOS
  // ==========================
  getAsientosPorSala(salaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/asientos/sala/${salaId}`);
  }

  actualizarAsiento(id: number, asientoData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/asientos/${id}`, asientoData);
  }
}
