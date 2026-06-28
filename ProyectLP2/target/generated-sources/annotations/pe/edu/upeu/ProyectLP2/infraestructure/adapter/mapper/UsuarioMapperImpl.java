package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T17:40:11-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public Usuario toDomainModel(UsuarioEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setIdUsuario( entity.getIdUsuario() );
        usuario.setNombre( entity.getNombre() );
        usuario.setEmail( entity.getEmail() );
        usuario.setPassword( entity.getPassword() );
        usuario.setRol( entity.getRol() );

        return usuario;
    }

    @Override
    public UsuarioEntity toEntity(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        usuarioEntity.setIdUsuario( usuario.getIdUsuario() );
        usuarioEntity.setNombre( usuario.getNombre() );
        usuarioEntity.setEmail( usuario.getEmail() );
        usuarioEntity.setPassword( usuario.getPassword() );
        usuarioEntity.setRol( usuario.getRol() );

        return usuarioEntity;
    }

    @Override
    public List<Usuario> toDomainModelList(List<UsuarioEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Usuario> list = new ArrayList<Usuario>( entities.size() );
        for ( UsuarioEntity usuarioEntity : entities ) {
            list.add( toDomainModel( usuarioEntity ) );
        }

        return list;
    }
}
