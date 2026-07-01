package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    /**
     * Mapea la entidad (con el campo 'email') al modelo de dominio (con el campo 'correo').
     */
    @Mapping(source = "email", target = "correo")
    Usuario toDomainModel(UsuarioEntity entity);

    /**
     * Mapea una lista de entidades a una lista de modelos de dominio.
     */
    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);

    /**
     * Mapea el modelo de dominio (con 'correo') a la entidad (con 'email').
     * No es necesario ignorar 'tickets' porque no existe en la entidad de destino.
     */
    @Mapping(source = "correo", target = "email")
    UsuarioEntity toEntity(Usuario domainModel);
}
