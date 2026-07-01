package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = AsientoMapper.class)
public interface SalaMapper {

    @Mapping(target = "asientos", ignore = true)
    Sala toDomainModel(SalaEntity entity);

    @Mapping(target = "asientos", ignore = true)
    SalaEntity toEntity(Sala domain);

    List<Sala> toDomainModelList(List<SalaEntity> entities);
}