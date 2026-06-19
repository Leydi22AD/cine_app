package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SalaMapper.class})
public interface AsientoMapper {
    Asiento toDomainModel(AsientoEntity entity);
    AsientoEntity toEntity(Asiento asiento);
    List<Asiento> toDomainModelList(List<AsientoEntity> entities);
}


