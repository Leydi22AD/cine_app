package pe.edu.upeu.ProyectLP2.domain.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ticket {

    private Long idTicket;
    private Funcion funcion;
    private Asiento asiento;
    private Usuario cliente;
    private BigDecimal precioUnitario;
    private LocalDateTime fechaCompra;

    public Ticket() {
    }

    public Ticket(Long idTicket, Funcion funcion, Asiento asiento, Usuario cliente, BigDecimal precioUnitario, LocalDateTime fechaCompra) {
        this.idTicket = idTicket;
        this.funcion = funcion;
        this.asiento = asiento;
        this.cliente = cliente;
        this.precioUnitario = precioUnitario;
        this.fechaCompra = fechaCompra;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}
