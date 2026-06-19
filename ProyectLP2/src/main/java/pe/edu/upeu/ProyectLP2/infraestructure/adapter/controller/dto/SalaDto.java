package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

public record SalaDto() {

        // DTO para la petición (crear/actualizar sala)
        public record SalaRequest(
                Integer numero,
                Integer filas,
                Integer columnas

        ) {}

        // DTO de respuesta (lo que devuelves al cliente)
        public record SalaResponse(
                Long idSala,
                Integer numero,
                Integer filas,
                Integer columnas

        ) {}
}
