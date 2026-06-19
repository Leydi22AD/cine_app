package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.port.in.SalaUseCase;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.SalaDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/salas")
public class SalaController {

    private final SalaUseCase salaUseCase;

    public SalaController(SalaUseCase salaUseCase) {
        this.salaUseCase = salaUseCase;
    }

    // Listar todas las salas
    @GetMapping
    public ResponseEntity<List<SalaDto.SalaResponse>> listarSalas() {
        List<SalaDto.SalaResponse> salas = salaUseCase.listarSalas()
                .stream()
                .map(this::mapToSalaResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    // Crear nueva sala (soporta POST en '/api/v1/salas' y '/api/v1/salas/crear')
    @PostMapping(value = {"", "/crear"})
    public ResponseEntity<SalaDto.SalaResponse> createSala(@RequestBody SalaDto.SalaRequest salaRequest) {
        Sala sala = new Sala();
        sala.setNumero(salaRequest.numero());
        sala.setFilas(salaRequest.filas());
        sala.setColumnas(salaRequest.columnas());



        Sala createdSala = salaUseCase.registrarSala(sala);

        return new ResponseEntity<>(mapToSalaResponse(createdSala), HttpStatus.CREATED);
    }

    // Obtener sala por id
    @GetMapping("/{id}")
    public ResponseEntity<SalaDto.SalaResponse> getSalaById(@PathVariable Long id) {
        return salaUseCase.obtenerSalaPorId(id)
                .map(sala -> ResponseEntity.ok(mapToSalaResponse(sala)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar sala
    @PutMapping("/{id}")
    public ResponseEntity<SalaDto.SalaResponse> updateSala(
            @PathVariable Long id,
            @RequestBody SalaDto.SalaRequest request) {

        Sala sala = new Sala();
        sala.setNumero(request.numero());
        sala.setFilas(request.filas());
        sala.setColumnas(request.columnas());

        return salaUseCase.actualizarSala(id, sala)
                .map(updated -> ResponseEntity.ok(mapToSalaResponse(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar sala
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSala(@PathVariable Long id) {
        if (salaUseCase.eliminarSala(id)) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }

    // Endpoint temporal para regenerar asientos de una sala
    @PostMapping("/{id}/regenerar-asientos")
    public ResponseEntity<String> regenerarAsientos(@PathVariable Long id) {
        return salaUseCase.obtenerSalaPorId(id)
            .map(sala -> {
                salaUseCase.registrarSala(sala); // Esto regenerará los asientos
                return ResponseEntity.ok("Asientos regenerados para sala " + id);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Método de ayuda para mapear de Domain a Response
    private SalaDto.SalaResponse mapToSalaResponse(Sala sala) {


        return new SalaDto.SalaResponse(
                sala.getIdSala(),
                sala.getNumero(),
                sala.getFilas(),
                sala.getColumnas()

        );
    }
}
