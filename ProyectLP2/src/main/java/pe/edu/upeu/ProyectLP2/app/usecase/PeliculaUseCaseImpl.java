package pe.edu.upeu.ProyectLP2.app.usecase;

import org.springframework.stereotype.Service;
import pe.edu.upeu.ProyectLP2.domain.exception.PeliculaAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.port.in.PeliculaUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.on.PeliculaRepositoryPort;


import java.util.List;
import java.util.Optional;

@Service
public class PeliculaUseCaseImpl implements PeliculaUseCase {

    private final PeliculaRepositoryPort peliculaRepositoryPort;

    public PeliculaUseCaseImpl(PeliculaRepositoryPort peliculaRepositoryPort) {
        this.peliculaRepositoryPort = peliculaRepositoryPort;
    }

    @Override
    public Pelicula registrarPelicula(Pelicula pelicula) {
        // Verificamos si ya existe una película con el mismo título en el mismo cine

        return peliculaRepositoryPort.save(pelicula);

    }

    @Override
    public Optional<Pelicula> actualizarPelicula(Long id, Pelicula pelicula) {
        try {
            Pelicula peliculaAc = peliculaRepositoryPort.update(id, pelicula);
            return Optional.of(peliculaAc);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Pelicula> obtenerPeliculaPorId(Long id) {
        return peliculaRepositoryPort.findById(id);
    }

    @Override
    public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
        return peliculaRepositoryPort.findByTitulo(titulo);
    }

    @Override
    public List<Pelicula> listarPeliculas() {
        return peliculaRepositoryPort.findAll();
    }

    @Override
    public boolean eliminarPelicula(Long id) {
        Optional<Pelicula> pelicula = peliculaRepositoryPort.findById(id);
        if (pelicula.isPresent()) {
            peliculaRepositoryPort.deleteById(id);
            return true;
        } else {
            return false; // No existe → false
        }
    }
}
