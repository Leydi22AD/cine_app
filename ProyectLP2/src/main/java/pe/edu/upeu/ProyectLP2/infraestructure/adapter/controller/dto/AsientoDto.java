package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

import pe.edu.upeu.ProyectLP2.domain.model.Sala;

public record AsientoDto() {

    // Lo que recibimos al crear/actualizar un Asiento
    public record AsientoRequest(
            Integer fila,
            Integer columna,
            String estado,
            Long salaId
    ) {}

    // Lo que devolvemos al cliente
    public record AsientoResponse(
            Long id,
            Integer fila,
            Integer columna,
            String estado,
            Sala sala
    ) {}
}
