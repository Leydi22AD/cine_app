package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.port.in.PeliculaUseCase;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.PeliculaDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/peliculas")
public class PeliculaController {

    private final PeliculaUseCase peliculaUseCase;

    public PeliculaController(PeliculaUseCase peliculaUseCase) {
        this.peliculaUseCase = peliculaUseCase;
    }

        // Listar todas las películas
        @GetMapping
        public ResponseEntity<List<PeliculaDto.PeliculaResponse>> listarPeliculas() {
        List<PeliculaDto.PeliculaResponse> peliculas = peliculaUseCase.listarPeliculas()
            .stream()
            .map(this::mapToPeliculaResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(peliculas);
        }

        // Crear nueva película (soporta POST en '/api/v1/peliculas' y '/api/v1/peliculas/crear')
        @PostMapping(value = {"", "/crear"})
        public ResponseEntity<PeliculaDto.PeliculaResponse> crearPelicula(
            @RequestBody PeliculaDto.PeliculaRequest request) {

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(request.titulo());
        pelicula.setGenero(request.genero());
        pelicula.setDuracion(request.duracion());
        pelicula.setFormato(request.formato());
        pelicula.setIdioma(request.idioma());
        pelicula.setPoster(request.poster());
        pelicula.setDirector(request.director());
        pelicula.setDescripcion(request.descripcion());
        pelicula.setTrailer(request.trailer());

        Pelicula creada = peliculaUseCase.registrarPelicula(pelicula);

        return new ResponseEntity<>(mapToPeliculaResponse(creada), HttpStatus.CREATED);
    }

    // Obtener película por id
    @GetMapping("/{id}")
    public ResponseEntity<PeliculaDto.PeliculaResponse> obtenerPeliculaPorId(@PathVariable Long id) {
        return peliculaUseCase.obtenerPeliculaPorId(id)
                .map(pelicula -> ResponseEntity.ok(mapToPeliculaResponse(pelicula)))
                .orElse(ResponseEntity.notFound().build());
    }
    // Actualizar película
    @PutMapping("/{id}")
    public ResponseEntity<PeliculaDto.PeliculaResponse> actualizarPelicula(
            @PathVariable Long id,
            @RequestBody PeliculaDto.PeliculaRequest request) {

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(request.titulo());
        pelicula.setGenero(request.genero());
        pelicula.setDuracion(request.duracion());
        pelicula.setFormato(request.formato());
        pelicula.setIdioma(request.idioma());
        pelicula.setPoster(request.poster());
        pelicula.setDirector(request.director());
        pelicula.setDescripcion(request.descripcion());
        pelicula.setTrailer(request.trailer());



        return peliculaUseCase.actualizarPelicula(id, pelicula)
                .map(p -> ResponseEntity.ok(mapToPeliculaResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar película
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPelicula(@PathVariable Long id) {
        if (peliculaUseCase.eliminarPelicula(id)) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }

    // Buscar películas por título
    @GetMapping("/buscar/{titulo}")
    public ResponseEntity<List<PeliculaDto.PeliculaResponse>> buscarPeliculasPorTitulo(@PathVariable String titulo) {
        List<PeliculaDto.PeliculaResponse> peliculas = peliculaUseCase.buscarPeliculasPorTitulo(titulo)
                .stream()
                .map(this::mapToPeliculaResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(peliculas);
    }
    // Mapeo de Domain -> DTO Response
    private PeliculaDto.PeliculaResponse mapToPeliculaResponse(Pelicula pelicula) {
        return new PeliculaDto.PeliculaResponse(
                pelicula.getIdPelicula(),
                pelicula.getTitulo(),
                pelicula.getGenero(),
                pelicula.getDuracion(),
                pelicula.getFormato(),
                pelicula.getIdioma(),
                pelicula.getPoster(),
                pelicula.getDirector(),
                pelicula.getDescripcion(),
                pelicula.getTrailer()

        );
    }


}
