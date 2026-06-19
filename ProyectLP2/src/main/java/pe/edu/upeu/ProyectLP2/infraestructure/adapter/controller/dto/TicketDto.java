package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketDto() {
    public record TicketRequest(Long funcionId, Long asientoId, Long clienteId) {}
    public record TicketResponse(
            Long id,
            BigDecimal precioUnitario,
            LocalDateTime fechaCompra,
            Funcion funcion,
            Asiento asiento,
            Usuario cliente
    ) {}
}