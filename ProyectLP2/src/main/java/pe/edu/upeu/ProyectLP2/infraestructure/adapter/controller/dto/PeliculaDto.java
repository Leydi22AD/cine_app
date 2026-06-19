package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;


public record  PeliculaDto() {

    // DTO para la petición (crear/actualizar una película)

    public record PeliculaRequest(

    String titulo,
    String genero,
    Integer duracion,
    String formato,
    String idioma,
    String poster,
    String director,
    String descripcion,
    String trailer
    ) {}

    // DTO de respuesta (lo que devuelves al cliente)
    public record PeliculaResponse(
            Long idPelicula,
            String titulo,
            String genero,
            Integer duracion,
            String formato,
            String idioma,
            String poster,
            String director,
            String descripcion,
            String trailer


    ) {}

}
