package pe.edu.upeu.ProyectLP2.infraestructure.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_funcion", nullable = false)
    private FuncionEntity funcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_asiento", nullable = false)
    private AsientoEntity asiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity cliente; // comprador

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    public TicketEntity() {}

    public TicketEntity(Long idTicket, FuncionEntity funcion, AsientoEntity asiento, UsuarioEntity cliente, BigDecimal precioUnitario, LocalDateTime fechaCompra) {
        this.idTicket = idTicket;
        this.funcion = funcion;
        this.asiento = asiento;
        this.cliente = cliente;
        this.precioUnitario = precioUnitario;
        this.fechaCompra = fechaCompra;
    }

    // getters & setters...
    public Long getIdTicket() { return idTicket; }
    public void setIdTicket(Long idTicket) { this.idTicket = idTicket; }
    public FuncionEntity getFuncion() { return funcion; }
    public void setFuncion(FuncionEntity funcion) { this.funcion = funcion; }
    public AsientoEntity getAsiento() { return asiento; }
    public void setAsiento(AsientoEntity asiento) { this.asiento = asiento; }
    public UsuarioEntity getCliente() { return cliente; }
    public void setCliente(UsuarioEntity cliente) { this.cliente = cliente; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
}
