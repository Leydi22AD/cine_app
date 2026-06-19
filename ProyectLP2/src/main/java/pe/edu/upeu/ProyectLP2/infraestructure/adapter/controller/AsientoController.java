package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.app.usecase.AsientoUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.port.in.AsientoUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.in.SalaUseCase;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.AsientoDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/asientos")
public class AsientoController {

    private final AsientoUseCase asientoUseCase;
    private final SalaUseCase salaUseCase;

    public AsientoController(AsientoUseCase asientoUseCase, SalaUseCase salaUseCase) {
        this.asientoUseCase = asientoUseCase;
        this.salaUseCase = salaUseCase;
    }

    // ✅ Crear asiento
    @PostMapping("/crear")
    public ResponseEntity<AsientoDto.AsientoResponse> createAsiento(
            @RequestBody AsientoDto.AsientoRequest request) {


        // 🔹 1. Buscar la sala en BD por su id
        Sala sala = salaUseCase.obtenerSalaPorId(request.salaId())
                .orElseThrow(() -> new RuntimeException("Sala no encontrada con ID: " + request.salaId()));

        // 🔹 2. Crear el asiento y asignarle la sala
        Asiento asiento = new Asiento();
        asiento.setFila(request.fila());
        asiento.setColumna(request.columna());
        asiento.setEstado(request.estado());
        asiento.setSala(sala);

        // 🔹 3. Guardar
        Asiento creado = asientoUseCase.crearAsiento(asiento);

        // 🔹 4. Devolver respuesta con sala y cine completos
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToAsientoResponse(creado));
    }

    // ✅ Obtener asiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<AsientoDto.AsientoResponse> getAsientoById(@PathVariable Long id) {
        return asientoUseCase.obtenerAsientoPorId(id)
                .map(a -> ResponseEntity.ok(mapToAsientoResponse(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Listar asientos por sala
    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<AsientoDto.AsientoResponse>> getAsientosBySala(@PathVariable Long salaId) {
        List<AsientoDto.AsientoResponse> lista = asientoUseCase.listarAsientosPorSala(salaId)
                .stream()
                .map(this::mapToAsientoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // ✅ Listar todos los asientos (opcional, pero útil)
    @GetMapping
    public ResponseEntity<List<AsientoDto.AsientoResponse>> listarTodos() {
        List<AsientoDto.AsientoResponse> lista = asientoUseCase.findAll()
                .stream()
                .map(this::mapToAsientoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // ✅ Actualizar asiento
    @PutMapping("/{id}")
    public ResponseEntity<AsientoDto.AsientoResponse> updateAsiento(
            @PathVariable Long id,
            @RequestBody AsientoDto.AsientoRequest request) {

        Sala sala = new Sala();
        sala.setIdSala(request.salaId());

        Asiento asiento = new Asiento();
        asiento.setFila(request.fila());
        asiento.setColumna(request.columna());
        asiento.setEstado(request.estado());
        asiento.setSala(sala);

        return asientoUseCase.actualizarAsiento(id, asiento)
                .map(a -> ResponseEntity.ok(mapToAsientoResponse(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar asiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsiento(@PathVariable Long id) {
        boolean eliminado = asientoUseCase.eliminarAsiento(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // 🔧 Mapeador auxiliar (modelo → DTO)
    private AsientoDto.AsientoResponse mapToAsientoResponse(Asiento asiento) {
        return new AsientoDto.AsientoResponse(
                asiento.getIdAsiento(),
                asiento.getFila(),
                asiento.getColumna(),
                asiento.getEstado(),
                asiento.getSala()
        );
    }
}
