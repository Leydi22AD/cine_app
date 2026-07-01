package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketDto() {
    public record TicketRequest(Long funcionId, Long asientoId, Long clienteId) {}

    public record FuncionResponse(Long id, Pelicula pelicula, Sala sala, LocalDateTime fecha, BigDecimal precio) {}

    public record TicketResponse(
            Long id,
            BigDecimal precioUnitario,
            LocalDateTime fechaCompra,
            FuncionResponse funcion,
            Asiento asiento,
            Usuario cliente
    ) {}
}