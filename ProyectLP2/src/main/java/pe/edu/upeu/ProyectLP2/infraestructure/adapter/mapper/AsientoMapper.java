package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsientoMapper {
    AsientoMapper INSTANCE = Mappers.getMapper(AsientoMapper.class);

    @Mapping(source = "idAsiento", target = "idAsiento")
    @Mapping(source = "fila", target = "fila")
    @Mapping(source = "columna", target = "columna")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "sala", target = "sala")
    Asiento toDomainModel(AsientoEntity entity);

    @Mapping(source = "idAsiento", target = "idAsiento")
    @Mapping(source = "fila", target = "fila")
    @Mapping(source = "columna", target = "columna")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "sala", target = "sala")
    AsientoEntity toEntity(Asiento domain);

    List<Asiento> toDomainModelList(List<AsientoEntity> entities);
}