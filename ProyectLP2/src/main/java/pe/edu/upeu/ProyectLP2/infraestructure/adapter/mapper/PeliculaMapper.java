package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.PeliculaEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PeliculaMapper {

    @Mapping(target = "funciones", ignore = true)
    Pelicula toDomainModel(PeliculaEntity entity);

    @Mapping(target = "funciones", ignore = true)
    PeliculaEntity toEntity(Pelicula domain);

    List<Pelicula> toDomainModelList(List<PeliculaEntity> entities);
}
