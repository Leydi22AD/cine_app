package pe.edu.upeu.ProyectLP2.app.usecase;

import org.springframework.stereotype.Service;
import pe.edu.upeu.ProyectLP2.domain.exception.UsuarioAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.domain.port.in.UsuarioUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.on.UsuarioRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {


    // Inyectamos el puerto de repositorio para comunicarnos con la capa de persistencia
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public UsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Validar que no exista un usuario con el mismo email

        // Validar que no exista un usuario con el mismo email antes de guardar
        if (usuarioRepositoryPort.findByEmail(usuario.getEmail()).isPresent()) {
            // Si ya existe, lanzamos una excepción personalizada
            throw new UsuarioAlreadyExistsException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        // Si no existe, lo guardamos en el repositorio
        return usuarioRepositoryPort.save(usuario);    }

    @Override
    public Optional<Usuario> actualizarUsuario(Long id, Usuario usuario) {
        try{
            // Usamos el repositorio para actualizar los datos del usuario
            Usuario clienteAc = usuarioRepositoryPort.update(id,usuario);

            // Retornamos el usuario actualizado envuelto en un Optional
            return Optional.of(clienteAc);
        } catch (Exception e) {
            // Si ocurre un error (ejemplo: no existe el usuario), devolvemos un Optional vacío
            return Optional.empty();
        }

    }

    @Override
    public boolean eliminarUsuario(Long id) {

        // Primero buscamos si existe el usuario con ese id
        return usuarioRepositoryPort.findById(id).map(u -> {

            // Si existe, lo eliminamos
            usuarioRepositoryPort.deleteById(id);
            return true;
        }).orElse(false);    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositoryPort.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepositoryPort.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepositoryPort.findByEmail(email);
    }
}
