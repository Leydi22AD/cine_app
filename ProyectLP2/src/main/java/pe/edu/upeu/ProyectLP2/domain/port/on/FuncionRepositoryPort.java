package pe.edu.upeu.ProyectLP2.domain.port.on;

import pe.edu.upeu.ProyectLP2.domain.model.Funcion;

import java.util.List;
import java.util.Optional;

public interface FuncionRepositoryPort {

    // Guarda o actualiza función (proyección de película)
    Funcion save(Funcion funcion);

    Funcion update(Long id, Funcion funcion);  // Actualizar

    // Busca función por ID
    Optional<Funcion> findById(Long id);

    // Devuelve todas las funciones
    List<Funcion> findAll();

    // Busca funciones de una película en particular
    List<Funcion> findByPeliculaId(Long peliculaId);

    // Elimina función por ID
    void deleteById(Long id);

}
