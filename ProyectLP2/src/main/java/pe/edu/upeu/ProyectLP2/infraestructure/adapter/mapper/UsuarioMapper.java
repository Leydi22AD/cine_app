package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import org.mapstruct.Mapper;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // MapStruct mapeará automáticamente los campos con el mismo nombre (idUsuario, nombre, email, password, rol)
    Usuario toDomainModel(UsuarioEntity entity);

    List<Usuario> toDomainModelList(List<UsuarioEntity> entities);

    // La relación inversa no necesita mapeos especiales si los nombres coinciden.
    UsuarioEntity toEntity(Usuario domainModel);
}
