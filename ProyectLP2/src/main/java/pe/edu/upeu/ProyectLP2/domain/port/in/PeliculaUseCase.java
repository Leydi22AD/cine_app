package pe.edu.upeu.ProyectLP2.domain.port.in;

import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;

import java.util.List;
import java.util.Optional;

public interface PeliculaUseCase {

    Pelicula registrarPelicula(Pelicula pelicula);
    Optional<Pelicula> actualizarPelicula(Long id, Pelicula pelicula);
    boolean eliminarPelicula(Long id);
    Optional<Pelicula> obtenerPeliculaPorId(Long id);
    List<Pelicula> buscarPeliculasPorTitulo(String titulo);
    List<Pelicula> listarPeliculas();

}
