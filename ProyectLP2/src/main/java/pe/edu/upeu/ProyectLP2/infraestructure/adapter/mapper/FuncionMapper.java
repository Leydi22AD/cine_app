package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PeliculaMapper.class, SalaMapper.class, UsuarioMapper.class})
public interface FuncionMapper {

    @Mapping(source = "pelicula", target = "pelicula")
    @Mapping(source = "sala", target = "sala")
    @Mapping(source = "tickets", target = "tickets")
    Funcion toDomainModel(FuncionEntity entity);

    List<Funcion> toDomainModelList(List<FuncionEntity> entities);

    @InheritInverseConfiguration
    FuncionEntity toEntity(Funcion domainModel);
}
