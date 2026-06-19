package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuncionDto() {

    // DTO para crear o actualizar una función
    public record FuncionRequest(
            String fecha,        // "2025-11-13"
            String horario,      // "18:00"
            BigDecimal precio,
            Long peliculaId,
            Long salaId
    ) {}

    // DTO para responder al cliente (simplificado para Postman)
    public record FuncionResponse(
            Long id,
            String fecha,
            String horario,
            BigDecimal precio,
            Long peliculaId,
            String tituloPelicula,
            Long salaId,
            Integer numeroSala,
            FilasColumnasDto sala,
            PeliculaSimpleDto pelicula
    ) {}
    
    public record FilasColumnasDto(
            Long idSala,
            Integer numero,
            Integer filas,
            Integer columnas
    ) {}
    
    public record PeliculaSimpleDto(
            Long idPelicula,
            String titulo,
            String genero,
            Integer duracion,
            String descripcion,
            String urlPoster,
            String urlTrailer
    ) {}

}
