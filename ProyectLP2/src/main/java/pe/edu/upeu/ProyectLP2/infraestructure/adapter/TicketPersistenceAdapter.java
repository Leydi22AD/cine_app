package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.domain.port.on.TicketRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.TicketMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

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

        UsuarioEntity cliente = usuarioRepository.findById(ticket.getCliente().getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        TicketEntity entity = ticketMapper.toEntity(ticket);
        entity.setFuncion(funcion);
        entity.setAsiento(asiento);
        entity.setCliente(cliente);

        TicketEntity savedEntity = ticketRepository.save(entity);

        // **LA SOLUCIÓN CLAVE**: Usamos el nuevo método para cargar todos los detalles.
        TicketEntity fullEntity = ticketRepository.findTicketDetailsById(savedEntity.getIdTicket())
                .orElseThrow(() -> new EntityNotFoundException("Error al recuperar el ticket recién guardado."));

        return ticketMapper.toDomainModel(fullEntity);
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        // Usamos la consulta optimizada también para las búsquedas por ID.
        return ticketRepository.findTicketDetailsById(id).map(ticketMapper::toDomainModel);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketMapper.toDomainModelList(ticketRepository.findAll());
    }

    @Override
    public Optional<Ticket> update(Long id, Ticket ticket) {
        if (!ticketRepository.existsById(id)) {
            return Optional.empty();
        }

        // Mapeamos a la entidad para guardar
        TicketEntity entityToUpdate = ticketMapper.toEntity(ticket);
        entityToUpdate.setIdTicket(id); // Aseguramos el ID

        ticketRepository.save(entityToUpdate);

        // Devolvemos la entidad completamente cargada
        return ticketRepository.findTicketDetailsById(id).map(ticketMapper::toDomainModel);
    }

    @Override
    public List<Ticket> findByFuncionId(Long funcionId) {
        return ticketMapper.toDomainModelList(ticketRepository.findByFuncion_IdFuncion(funcionId));
    }

    @Override
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        ticketRepository.deleteAll();
    }
}