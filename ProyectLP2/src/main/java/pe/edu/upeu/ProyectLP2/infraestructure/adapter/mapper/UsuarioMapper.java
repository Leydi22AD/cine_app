package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toDomainModel(UsuarioEntity entity);

    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);

    UsuarioEntity toEntity(Usuario domainModel);
}
