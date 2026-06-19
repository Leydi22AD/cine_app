package pe.edu.upeu.ProyectLP2.domain.port.in;

import pe.edu.upeu.ProyectLP2.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

// los métodos de las operaciones que podrá realizar un caso de uso sobre "Usuario"
public interface UsuarioUseCase {

    // Recibe un objeto Usuario y devuelve el mismo usuario ya registrado.
    Usuario registrarUsuario(Usuario usuario);

    // Recibe el id del usuario y un objeto Usuario con los nuevos datos.
    // Retorna un Optional que contendrá el usuario actualizado si existe, o vacío si no.
    Optional<Usuario> actualizarUsuario(Long id, Usuario usuario);

    // Método para eliminar un usuario según su id.
    boolean eliminarUsuario(Long id);


    // Método para listar todos los usuarios registrados.
    List<Usuario> listarUsuarios();

    // Método para buscar un usuario por su id.
    Optional<Usuario> obtenerUsuarioPorId(Long id);

    // Método para buscar un usuario por su email.
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
}
