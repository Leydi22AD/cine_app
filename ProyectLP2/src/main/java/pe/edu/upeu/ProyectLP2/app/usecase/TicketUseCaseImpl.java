package pe.edu.upeu.ProyectLP2.app.usecase;

import org.springframework.stereotype.Service;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.domain.port.in.TicketUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.on.TicketRepositoryPort;
import pe.edu.upeu.ProyectLP2.domain.exception.AsientoAlreadyExistsException; // O la excepción que mapee a tu 409

import java.util.List;
import java.util.Optional;

@Service
public class TicketUseCaseImpl implements TicketUseCase {

    public final TicketRepositoryPort ticketRepositoryPort;

    public TicketUseCaseImpl(TicketRepositoryPort ticketRepositoryPort) {
        this.ticketRepositoryPort = ticketRepositoryPort;
    }
    @Override
    public Ticket crearTicket(Ticket ticket) {
        if (ticket.getAsiento() != null && ticket.getAsiento().getIdAsiento() != null && ticket.getFuncion() != null) {
            Long asientoId = ticket.getAsiento().getIdAsiento();
            Long funcionId = ticket.getFuncion().getIdFuncion();

            if (funcionId != null) {
                List<Ticket> ticketsExistentes = ticketRepositoryPort.findByFuncionId(funcionId);
                boolean yaOcupado = ticketsExistentes.stream()
                        .anyMatch(t -> t.getAsiento() != null && asientoId.equals(t.getAsiento().getIdAsiento()));

                if (yaOcupado) {
                    throw new pe.edu.upeu.ProyectLP2.domain.exception.AsientoAlreadyExistsException("El asiento ya está ocupado.");
                }
            }
        }

        if (ticket.getFuncion() != null && ticket.getFuncion().getPrecio() != null) {
            ticket.setPrecioUnitario(ticket.getFuncion().getPrecio());
        }

        return ticketRepositoryPort.save(ticket);
    }

    @Override
    public Optional<Ticket> obtenerTicketPorId(Long id) {
        return ticketRepositoryPort.findById(id);
    }

    @Override
    public List<Ticket> listarTickets() {
        return ticketRepositoryPort.findAll();
    }

    @Override
    public Optional<Ticket> actualizarTicket(Long id, Ticket ticket) {
        Optional<Ticket> existing = ticketRepositoryPort.findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        ticket.setIdTicket(id);
        return ticketRepositoryPort.update(id, ticket);
    }

    @Override
    public List<Ticket> listarTicketsPorFuncion(Long funcionId) {
        return ticketRepositoryPort.findByFuncionId(funcionId);
    }

    @Override
    public boolean anularTicket(Long id) {
        return ticketRepositoryPort.findById(id)
                .map(ticket -> {
                    ticketRepositoryPort.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}