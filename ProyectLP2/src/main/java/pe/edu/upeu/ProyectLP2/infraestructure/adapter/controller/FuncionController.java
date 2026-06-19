package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.domain.exception.FuncionAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.port.in.FuncionUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.in.PeliculaUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.in.SalaUseCase;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.FuncionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funciones")
public class FuncionController {

    private final FuncionUseCase funcionUseCase;
    private final PeliculaUseCase peliculaUseCase;
    private final SalaUseCase salaUseCase;

    public FuncionController(FuncionUseCase funcionUseCase, PeliculaUseCase peliculaUseCase, SalaUseCase salaUseCase) {
        this.funcionUseCase = funcionUseCase;
        this.peliculaUseCase = peliculaUseCase;
        this.salaUseCase = salaUseCase;
    }

    // Crear función (soporta POST en '/api/v1/funciones' y '/api/v1/funciones/crear')
    @PostMapping(value = {"", "/crear"})
    public ResponseEntity<?> crearFuncion(
            @RequestBody FuncionDto.FuncionRequest request) {

        try {

            // Validar campos obligatorios
            if (request.peliculaId() == null || request.salaId() == null || request.fecha() == null || request.horario() == null || request.precio() == null || request.precio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Campos de función obligatorios o precio inválido");
            }

            Pelicula pelicula = peliculaUseCase.obtenerPeliculaPorId(request.peliculaId())
                    .orElseThrow(() -> new RuntimeException("Película no encontrada"));

            Sala sala = salaUseCase.obtenerSalaPorId(request.salaId())
                    .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

            LocalDateTime fechaHora = combinarFechaHora(
                    request.fecha(),
                    request.horario()
            );

            Funcion funcion = new Funcion();
            funcion.setFecha(fechaHora);
            funcion.setPrecio(request.precio());
            funcion.setPelicula(pelicula);
            funcion.setSala(sala);

            Funcion creada = funcionUseCase.crearFuncion(funcion);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(mapToFuncionResponse(creada));

        } catch (FuncionAlreadyExistsException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        }
    }

    // Obtener función por ID
    @GetMapping("/{id}")
    public ResponseEntity<FuncionDto.FuncionResponse> obtenerFuncionPorId(@PathVariable Long id) {
        return funcionUseCase.listarFunciones()
                .stream()
                .filter(f -> f.getIdFuncion().equals(id))
                .findFirst()
                .map(f -> ResponseEntity.ok(mapToFuncionResponse(f)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todas las funciones
    @GetMapping
    public ResponseEntity<List<FuncionDto.FuncionResponse>> listarFunciones() {
        List<FuncionDto.FuncionResponse> funciones = funcionUseCase.listarFunciones()
                .stream()
                .map(this::mapToFuncionResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(funciones);
    }

    // Listar funciones por película
    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<List<FuncionDto.FuncionResponse>> listarFuncionesPorPelicula(@PathVariable Long peliculaId) {
        List<FuncionDto.FuncionResponse> funciones = funcionUseCase.listarFuncionesPorPelicula(peliculaId)
                .stream()
                .map(this::mapToFuncionResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(funciones);
    }

    //  Actualizar función
    @PutMapping("/{id}")
    public ResponseEntity<FuncionDto.FuncionResponse> actualizarFuncion(
            @PathVariable Long id,
            @RequestBody FuncionDto.FuncionRequest request) {

        LocalDateTime fechaHora = combinarFechaHora(request.fecha(), request.horario());
        
        // Validar que la fecha no sea anterior a hoy
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha de la función no puede ser anterior a la fecha actual");
        }

        Funcion funcion = new Funcion();
        funcion.setFecha(fechaHora);
        funcion.setPrecio(request.precio());

        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(request.peliculaId());
        funcion.setPelicula(pelicula);

        Sala sala = new Sala();
        sala.setIdSala(request.salaId());
        funcion.setSala(sala);

        return funcionUseCase.actualizarFuncion(id, funcion)
                .map(f -> ResponseEntity.ok(mapToFuncionResponse(f)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar función
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFuncion(@PathVariable Long id) {
        if (funcionUseCase.eliminarFuncion(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    // --- Método auxiliar para combinar fecha y hora ---
    private LocalDateTime combinarFechaHora(String fechaStr, String horarioStr) {
        // Parsear fecha: "2025-11-13T09:00" o "2025-11-13"
        LocalDate fecha;
        if (fechaStr.contains("T")) {
            // Si viene en formato datetime-local del HTML5
            return LocalDateTime.parse(fechaStr);
        } else {
            fecha = LocalDate.parse(fechaStr);
        }
        
        // Parsear horario: "09:00" o "09:00:00"
        LocalTime hora;
        if (horarioStr != null && !horarioStr.isEmpty()) {
            hora = LocalTime.parse(horarioStr);
        } else {
            hora = LocalTime.of(0, 0);
        }
        
        return LocalDateTime.of(fecha, hora);
    }

    // --- Mapeador auxiliar ---
    private FuncionDto.FuncionResponse mapToFuncionResponse(Funcion funcion) {
        // Extraer fecha y horario del LocalDateTime
        String fecha = funcion.getFecha().toLocalDate().toString();
        String horario = funcion.getFecha().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        
        return new FuncionDto.FuncionResponse(
                funcion.getIdFuncion(),
                fecha,
                horario,
                funcion.getPrecio(),
                funcion.getPelicula().getIdPelicula(),
                funcion.getPelicula().getTitulo(),
                funcion.getSala().getIdSala(),
                funcion.getSala().getNumero(),
                new FuncionDto.FilasColumnasDto(
                    funcion.getSala().getIdSala(),
                    funcion.getSala().getNumero(),
                    funcion.getSala().getFilas(),
                    funcion.getSala().getColumnas()
                ),
                new FuncionDto.PeliculaSimpleDto(
                    funcion.getPelicula().getIdPelicula(),
                    funcion.getPelicula().getTitulo(),
                    funcion.getPelicula().getGenero(),
                    funcion.getPelicula().getDuracion(),
                    funcion.getPelicula().getDescripcion(),
                    funcion.getPelicula().getUrlPoster(),
                    funcion.getPelicula().getUrlTrailer()
                )
        );
    }
}
