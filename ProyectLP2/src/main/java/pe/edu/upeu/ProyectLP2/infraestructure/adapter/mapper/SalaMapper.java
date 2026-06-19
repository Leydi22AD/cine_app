package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaMapper {

    @Mapping(target = "asientos", ignore = true) // si Sala (domain) tiene asientos
    Sala toDomainModel(SalaEntity entity);

    @Mapping(target = "asientos", ignore = true) // si SalaEntity también la tiene
    SalaEntity toEntity(Sala domain);

    List<Sala> toDomainModelList(List<SalaEntity> entities);
}
