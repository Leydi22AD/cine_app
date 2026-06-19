package pe.edu.upeu.ProyectLP2.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.PeliculaEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties({"sala", "pelicula"})
public class Funcion {

    private Long idFuncion;

    private Pelicula pelicula;

    @JsonIgnore
    private Sala sala;

    private LocalDateTime fecha;

    private BigDecimal precio;

    private List<Ticket> Tickets;

    public Funcion() {
    }

    public Funcion(Long idFuncion, Pelicula pelicula, Sala sala, LocalDateTime fecha, BigDecimal precio, List<Ticket> tickets) {
        this.idFuncion = idFuncion;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.precio = precio;
        Tickets = tickets;
    }


    public Long getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(Long idFuncion) {
        this.idFuncion = idFuncion;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public List<Ticket> getTickets() {
        return Tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        Tickets = tickets;
    }
}
