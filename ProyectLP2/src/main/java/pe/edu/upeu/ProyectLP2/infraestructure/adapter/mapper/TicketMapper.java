package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses= { FuncionMapper.class, AsientoMapper.class, UsuarioMapper.class, PeliculaMapper.class })
public interface TicketMapper {

    @Mapping(target = "funcion", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    Ticket toDomainModel(TicketEntity entity);

    @Mapping(target = "asiento", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    TicketEntity toEntity(Ticket ticket);

    List<Ticket> toDomainModelList(List<TicketEntity> entities);
}