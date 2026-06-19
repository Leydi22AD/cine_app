package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.domain.port.on.UsuarioRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.UsuarioMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;


import java.util.List;
import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    // Repositorio JPA que maneja operaciones con la BD (Spring Data).
    private final UsuarioRepository usuarioRepository;

    // Mapper que convierte entre entidad (UsuarioEntity) y modelo de dominio (Usuario).
    private final UsuarioMapper usuarioMapper;

    public UsuarioPersistenceAdapter(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Usuario save(Usuario usuario) {

        // Convertimos el objeto de dominio a entidad de persistencia.
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);

        // Guardamos en la BD usando el repositorio JPA.
        UsuarioEntity savedUsuario = usuarioRepository.save(usuarioEntity);

        // Convertimos la entidad guardada de nuevo a dominio y la retornamos.
        return usuarioMapper.toDomainModel(savedUsuario);

    }

    @Override
    public Optional<Usuario> findById(Long id) {

        // Buscamos en la BD y convertimos la entidad encontrada a modelo de dominio.
        return usuarioRepository.findById(id).map(usuarioMapper::toDomainModel);

    }

    @Override
    public Optional<Usuario> findByEmail(String email) {

        // Igual que findById, pero buscando por email.
        return usuarioRepository.findByEmail(email).map(usuarioMapper::toDomainModel);
    }

    @Override
    public List<Usuario> findAll() {
        List<UsuarioEntity> usuarioEntities = usuarioRepository.findAll();
        return usuarioMapper.toDomainModelList(usuarioEntities);

    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario update(Long id, Usuario c) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado " + id));

        usuarioEntity.setNombre(c.getNombre());
        usuarioEntity.setEmail(c.getEmail());
        usuarioEntity.setPassword(c.getPassword());
        usuarioEntity.setRol(c.getRol());

        UsuarioEntity updatedUsuario = usuarioRepository.save(usuarioEntity);
        return usuarioMapper.toDomainModel(updatedUsuario);
}
}
