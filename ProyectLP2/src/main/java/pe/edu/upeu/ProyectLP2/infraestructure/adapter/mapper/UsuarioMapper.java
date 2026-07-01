package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "email", target = "correo")
    Usuario toDomainModel(UsuarioEntity entity);

    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);

    @Mapping(source = "correo", target = "email")
    @Mapping(target = "tickets", ignore = true)
    UsuarioEntity toEntity(Usuario domainModel);
}
