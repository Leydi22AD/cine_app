package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    /**
     * Mapea una entidad UsuarioEntity a un modelo de dominio Usuario.
     */
    Usuario toDomainModel(UsuarioEntity entity);

    /**
     * Mapea una lista de entidades UsuarioEntity a una lista de modelos de dominio Usuario.
     */
    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);

    /**
     * Mapea un modelo de dominio Usuario a una entidad UsuarioEntity.
     * Se ignora la propiedad 'tickets' para evitar ciclos de mapeo en relaciones bidireccionales.
     */
    @Mapping(target = "tickets", ignore = true)
    UsuarioEntity toEntity(Usuario domainModel);
}
