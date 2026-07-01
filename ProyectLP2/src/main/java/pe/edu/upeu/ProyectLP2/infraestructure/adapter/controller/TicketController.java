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

    @PostMapping(value = {"", "/crear"})
    public ResponseEntity<TicketDto.TicketResponse> crearTicket(@RequestBody TicketDto.TicketRequest request) {
        var funcion = funcionUseCase.obtenerFuncionPorId(request.funcionId())
                .orElseThrow(() -> new IllegalArgumentException("Función no existe"));

        var asiento = asientoUseCase.obtenerAsientoPorId(request.asientoId())
                .orElseThrow(() -> new IllegalArgumentException("Asiento no existe"));

        var cliente = usuarioUseCase.obtenerUsuarioPorId(request.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        var ticket = new Ticket();
        ticket.setFuncion(funcion);
        ticket.setAsiento(asiento);
        ticket.setCliente(cliente);
        ticket.setPrecioUnitario(funcion.getPrecio());
        ticket.setFechaCompra(LocalDateTime.now());

        var creado = ticketUseCase.crearTicket(ticket);

        // Marcar asiento como ocupado DESPUÉS de crear el ticket
        asiento.setEstado("OCUPADO");
        asientoUseCase.actualizarAsiento(asiento.getIdAsiento(), asiento);

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

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        ticketUseCase.deleteAllTickets();
        return ResponseEntity.noContent().build();
    }

    // --- Mapeador auxiliar ---
    private TicketDto.TicketResponse mapToTicketResponse(Ticket ticket) {
        TicketDto.SalaResponse salaResponse = new TicketDto.SalaResponse(
                ticket.getFuncion().getSala().getIdSala(),
                ticket.getFuncion().getSala().getNumero()
        );

        TicketDto.FuncionResponse funcionResponse = new TicketDto.FuncionResponse(
                ticket.getFuncion().getIdFuncion(),
                ticket.getFuncion().getPelicula(),
                salaResponse,
                ticket.getFuncion().getFecha(),
                ticket.getFuncion().getPrecio()
        );

        TicketDto.AsientoResponse asientoResponse = new TicketDto.AsientoResponse(
                ticket.getAsiento().getIdAsiento(),
                ticket.getAsiento().getFila(),
                ticket.getAsiento().getColumna(),
                ticket.getAsiento().getEstado()
        );

        return new TicketDto.TicketResponse(
                ticket.getIdTicket(),
                ticket.getPrecioUnitario(),
                ticket.getFechaCompra(),
                funcionResponse,
                asientoResponse,
                ticket.getCliente()
        );
    }
}