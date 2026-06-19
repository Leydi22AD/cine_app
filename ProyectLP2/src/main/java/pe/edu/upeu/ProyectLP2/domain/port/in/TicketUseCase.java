package pe.edu.upeu.ProyectLP2.domain.port.in;

import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketUseCase {


    Ticket crearTicket(Ticket ticket);

    Optional<Ticket> obtenerTicketPorId(Long id);

    List<Ticket> listarTickets();                       // devuelve todos los tickets

    Optional<Ticket> actualizarTicket(Long id, Ticket ticket);


    List<Ticket> listarTicketsPorFuncion(Long funcionId); // tickets filtrados por función

    boolean anularTicket(Long id);                      // anular / eliminar                    // anular / eliminar
}
