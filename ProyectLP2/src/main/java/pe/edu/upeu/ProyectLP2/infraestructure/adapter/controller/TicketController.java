package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.domain.port.in.*;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.TicketDto;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketUseCase ticketUseCase;
    private final FuncionUseCase funcionUseCase;
    private final AsientoUseCase asientoUseCase;
    private final UsuarioUseCase usuarioUseCase;

    public TicketController(TicketUseCase ticketUseCase, FuncionUseCase funcionUseCase, AsientoUseCase asientoUseCase, UsuarioUseCase usuarioUseCase) {
        this.ticketUseCase = ticketUseCase;
        this.funcionUseCase = funcionUseCase;
        this.asientoUseCase = asientoUseCase;
        this.usuarioUseCase = usuarioUseCase;
    }

    // Crear ticket (soporta POST en '/api/v1/tickets' y '/api/v1/tickets/crear')
    @PostMapping(value = {"", "/crear"})
    public ResponseEntity<TicketDto.TicketResponse> crearTicket(@RequestBody TicketDto.TicketRequest request) {
        var funcion = funcionUseCase.obtenerFuncionPorId(request.funcionId())
                .orElseThrow(() -> new IllegalArgumentException("Función no existe"));

        var asiento = asientoUseCase.obtenerAsientoPorId(request.asientoId())
                .orElseThrow(() -> new IllegalArgumentException("Asiento no existe"));

        if (!"LIBRE".equalsIgnoreCase(asiento.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // FIX: Correctly compare Sala IDs from domain objects
        if (asiento.getSala() == null || funcion.getSala() == null || !asiento.getSala().getIdSala().equals(funcion.getSala().getIdSala())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var ticket = new Ticket();
        ticket.setFuncion(funcion);
        ticket.setAsiento(asiento);

        var cliente = usuarioUseCase.obtenerUsuarioPorId(request.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
        ticket.setCliente(cliente);

        ticket.setPrecioUnitario(funcion.getPrecio());
        ticket.setFechaCompra(LocalDateTime.now());

        // marcar ocupado
        asiento.setEstado("OCUPADO");
        asientoUseCase.actualizarAsiento(asiento.getIdAsiento(), asiento);

        var creado = ticketUseCase.crearTicket(ticket);
        return new ResponseEntity<>(mapToTicketResponse(creado), HttpStatus.CREATED);
    }


    // Listar todos los tickets
    @GetMapping
    public ResponseEntity<List<TicketDto.TicketResponse>> listarTodosLosTickets() {
        List<TicketDto.TicketResponse> tickets = ticketUseCase.listarTickets()
                .stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    // Listar tickets por función
    @GetMapping("/funcion/{funcionId}")
    public ResponseEntity<List<TicketDto.TicketResponse>> listarTicketsPorFuncion(@PathVariable Long funcionId) {
        List<TicketDto.TicketResponse> tickets = ticketUseCase.listarTicketsPorFuncion(funcionId)
                .stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto.TicketResponse> obtenerTicketPorId(@PathVariable Long id) {
        return ticketUseCase.obtenerTicketPorId(id)
                .map(t -> ResponseEntity.ok(mapToTicketResponse(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        if (ticketUseCase.anularTicket(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- Mapeador auxiliar ---
    private TicketDto.TicketResponse mapToTicketResponse(Ticket ticket) {
        return new TicketDto.TicketResponse(
                ticket.getIdTicket(),
                ticket.getPrecioUnitario(),
                ticket.getFechaCompra(),
                ticket.getFuncion(),
                ticket.getAsiento(),
                ticket.getCliente()
        );
    }
}