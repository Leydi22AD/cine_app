package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;


import org.mapstruct.Mapper;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Convierte de Entity a Domain
    Usuario toDomainModel(UsuarioEntity entity);

    // Convierte de Domain a Entity
    UsuarioEntity toEntity(Usuario usuario);

    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);


}
