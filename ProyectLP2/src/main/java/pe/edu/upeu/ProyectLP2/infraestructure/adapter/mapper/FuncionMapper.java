package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;

import java.util.List;


@Mapper(componentModel = "spring", uses = {SalaMapper.class, PeliculaMapper.class})
public interface FuncionMapper {
    @Mapping(target = "tickets", ignore = true)
    Funcion toDomainModel(FuncionEntity entity);

    @Mapping(target = "tickets", ignore = true)
    FuncionEntity toEntity(Funcion domain);

    List<Funcion> toDomainModelList(List<FuncionEntity> entities);
}


