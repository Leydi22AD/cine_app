package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T17:40:11-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Autowired
    private FuncionMapper funcionMapper;
    @Autowired
    private AsientoMapper asientoMapper;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    public Ticket toDomainModel(TicketEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setIdTicket( entity.getIdTicket() );
        ticket.setFuncion( funcionMapper.toDomainModel( entity.getFuncion() ) );
        ticket.setAsiento( asientoMapper.toDomainModel( entity.getAsiento() ) );
        ticket.setCliente( usuarioMapper.toDomainModel( entity.getCliente() ) );
        ticket.setPrecioUnitario( entity.getPrecioUnitario() );
        ticket.setFechaCompra( entity.getFechaCompra() );

        return ticket;
    }

    @Override
    public TicketEntity toEntity(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketEntity ticketEntity = new TicketEntity();

        ticketEntity.setIdTicket( ticket.getIdTicket() );
        ticketEntity.setFuncion( funcionMapper.toEntity( ticket.getFuncion() ) );
        ticketEntity.setAsiento( asientoMapper.toEntity( ticket.getAsiento() ) );
        ticketEntity.setCliente( usuarioMapper.toEntity( ticket.getCliente() ) );
        ticketEntity.setPrecioUnitario( ticket.getPrecioUnitario() );
        ticketEntity.setFechaCompra( ticket.getFechaCompra() );

        return ticketEntity;
    }

    @Override
    public List<Ticket> toDomainModelList(List<TicketEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Ticket> list = new ArrayList<Ticket>( entities.size() );
        for ( TicketEntity ticketEntity : entities ) {
            list.add( toDomainModel( ticketEntity ) );
        }

        return list;
    }
}
