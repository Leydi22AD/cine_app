package pe.edu.upeu.ProyectLP2.domain.port.on;

import pe.edu.upeu.ProyectLP2.domain.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryPort {

    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long id);
    List<Ticket> findAll();
    List<Ticket> findByFuncionId(Long funcionId);
    Optional<Ticket> update(Long id, Ticket ticket);
    void deleteById(Long id);
}
