package pe.edu.upeu.ProyectLP2.infraestructure.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "funcion")
public class FuncionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuncion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pelicula", nullable = false)
    private PeliculaEntity pelicula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sala", nullable = false)
    private SalaEntity sala;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @OneToMany(mappedBy = "funcion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TicketEntity> Tickets;

    public FuncionEntity() {
    }


    public FuncionEntity(Long idFuncion, PeliculaEntity pelicula, SalaEntity sala, LocalDateTime fecha, BigDecimal precio, List<TicketEntity> tickets) {
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

    public PeliculaEntity getPelicula() {
        return pelicula;
    }

    public void setPelicula(PeliculaEntity pelicula) {
        this.pelicula = pelicula;
    }

    public SalaEntity getSala() {
        return sala;
    }

    public void setSala(SalaEntity sala) {
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

    public List<TicketEntity> getTickets() {
        return Tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        Tickets = tickets;
    }
}