package pe.edu.upeu.ProyectLP2.domain.port.on;

import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;

import java.util.List;
import java.util.Optional;

public interface PeliculaRepositoryPort {

    Pelicula save(Pelicula pelicula);
    Optional<Pelicula> findById(Long id);
    List<Pelicula> findAll();
    List<Pelicula> findByTitulo(String titulo);
    void deleteById(Long id);
    Pelicula update(Long id, Pelicula pelicula);



}
