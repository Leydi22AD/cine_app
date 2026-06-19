package pe.edu.upeu.ProyectLP2.app.usecase;

import org.springframework.stereotype.Service;
import pe.edu.upeu.ProyectLP2.domain.exception.FuncionAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.port.in.FuncionUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.on.FuncionRepositoryPort;

import java.util.List;
import java.util.Optional;


@Service
public class FuncionUseCaseImpl implements FuncionUseCase {

    private final FuncionRepositoryPort funcionRepositoryPort;

    public FuncionUseCaseImpl(FuncionRepositoryPort funcionRepositoryPort) {
        this.funcionRepositoryPort = funcionRepositoryPort;
    }

    @Override
    public Funcion crearFuncion(Funcion funcion) {
        // Buscamos en la base de datos todas las funciones de la película actual
        List<Funcion> existentes = funcionRepositoryPort.findByPeliculaId(
                funcion.getPelicula().getIdPelicula()
        );

        // Verificamos si ya existe una función en la misma sala, fecha y hora
        boolean yaExiste = existentes.stream().anyMatch(f ->
                f.getSala().getIdSala().equals(funcion.getSala().getIdSala()) &&
                        f.getFecha().equals(funcion.getFecha())
        );
        // Si ya existe una función con esas características, lanzamos una excepción
        if (yaExiste) {
            throw new FuncionAlreadyExistsException(
                    "Ya existe una función para esta película en la sala " +
                            funcion.getSala().getIdSala() + " en la fecha y hora indicada."
            );
        }
        // Si no existe duplicado, guardamos la función en la base de datos
        return funcionRepositoryPort.save(funcion);
    }

    @Override
    public Optional<Funcion> actualizarFuncion(Long id, Funcion funcion) {
        try {
            Funcion funcionActualizada = funcionRepositoryPort.update(id, funcion);
            return Optional.of(funcionActualizada);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Funcion> obtenerFuncionPorId(Long id) {
        return funcionRepositoryPort.findById(id);
    }

    @Override
    public boolean eliminarFuncion(Long id) {
        try {
            funcionRepositoryPort.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Funcion> listarFunciones() {
        return funcionRepositoryPort.findAll();
    }


    @Override
    public List<Funcion> listarFuncionesPorPelicula(Long peliculaId) {
        return funcionRepositoryPort.findByPeliculaId(peliculaId);
    }
}


