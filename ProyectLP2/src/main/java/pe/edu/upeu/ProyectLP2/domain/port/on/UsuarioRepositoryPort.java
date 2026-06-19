package pe.edu.upeu.ProyectLP2.domain.port.on;

import pe.edu.upeu.ProyectLP2.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    // Método para guardar un usuario en el repositorio.
    Usuario save(Usuario usuario);                 // crear o actualizar

    // Método para buscar un usuario en el repositorio por su id.
    Optional<Usuario> findById(Long id);

    // Método para buscar un usuario en el repositorio por su email.
    Optional<Usuario> findByEmail(String email);

    // Método para obtener todos los usuarios del repositorio.
    List<Usuario> findAll();

    // Método para eliminar un usuario del repositorio a partir de su id.
    void deleteById(Long id);

    // Método para actualizar un usuario en el repositorio.
    // Recibe el id del usuario a actualizar y el objeto con los nuevos datos.
    // Devuelve el usuario actualizado
    Usuario update(Long id, Usuario c);

}
