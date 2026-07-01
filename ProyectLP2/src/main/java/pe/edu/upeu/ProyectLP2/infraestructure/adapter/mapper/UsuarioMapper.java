package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "correo", ignore = true)
    Usuario toDomainModel(UsuarioEntity entity);

    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);

    @InheritInverseConfiguration
    @Mapping(target = "tickets", ignore = true)
    UsuarioEntity toEntity(Usuario domainModel);
}