package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.domain.port.on.TicketRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.TicketMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.util.List;
import java.util.Optional;

@Component
public class TicketPersistenceAdapter implements TicketRepositoryPort {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final FuncionRepository funcionRepository;
    private final AsientoRepository asientoRepository;
    private final UsuarioRepository usuarioRepository;


    public TicketPersistenceAdapter(TicketRepository ticketRepository, TicketMapper ticketMapper, FuncionRepository funcionRepository, AsientoRepository asientoRepository, UsuarioRepository usuarioRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.funcionRepository = funcionRepository;
        this.asientoRepository = asientoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        FuncionEntity funcion = funcionRepository.findById(ticket.getFuncion().getIdFuncion())
                .orElseThrow(() -> new EntityNotFoundException("Función no encontrada"));

        AsientoEntity asiento = asientoRepository.findById(ticket.getAsiento().getIdAsiento())
                .orElseThrow(() -> new EntityNotFoundException("Asiento no encontrado"));


        TicketEntity entity = ticketMapper.toEntity(ticket);
        entity.setFuncion(funcion);
        entity.setAsiento(asiento);

        TicketEntity saved = ticketRepository.save(entity);
        return ticketMapper.toDomainModel(saved);    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id).map(ticketMapper::toDomainModel);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketMapper.toDomainModelList(ticketRepository.findAll());
    }

    @Override
    public Optional<Ticket> update(Long id, Ticket ticket) {
        TicketEntity existing = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado"));

        existing.setPrecioUnitario(ticket.getPrecioUnitario());

        // Actualizar relaciones
        existing.setFuncion(funcionRepository.findById(ticket.getFuncion().getIdFuncion())
                .orElseThrow(() -> new EntityNotFoundException("Función no encontrada")));

        existing.setAsiento(asientoRepository.findById(ticket.getAsiento().getIdAsiento())
                .orElseThrow(() -> new EntityNotFoundException("Asiento no encontrado")));

        existing.setCliente(usuarioRepository.findById(ticket.getCliente().getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado")));

        TicketEntity updated = ticketRepository.save(existing);
        return Optional.of(ticketMapper.toDomainModel(updated));
    }

    @Override
    public List<Ticket> findByFuncionId(Long funcionId) {
        return ticketMapper.toDomainModelList(ticketRepository.findByFuncion_IdFuncion(funcionId));
    }

    @Override
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);

    }
}