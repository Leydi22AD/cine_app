package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        FuncionMapper.class,
        AsientoMapper.class,
        UsuarioMapper.class
})
public interface TicketMapper {

    @Mapping(source = "funcion", target = "funcion")
    @Mapping(source = "asiento", target = "asiento")
    @Mapping(source = "cliente", target = "cliente")
    Ticket toDomainModel(TicketEntity entity);

    List<Ticket> toDomainModelList(List<TicketEntity> entities);

    @InheritInverseConfiguration
    TicketEntity toEntity(Ticket domainModel);
}
