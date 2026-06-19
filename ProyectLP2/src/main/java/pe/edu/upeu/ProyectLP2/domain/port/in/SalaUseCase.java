package pe.edu.upeu.ProyectLP2.domain.port.in;

import pe.edu.upeu.ProyectLP2.domain.model.Sala;

import java.util.List;
import java.util.Optional;

public interface SalaUseCase {

    Sala registrarSala(Sala sala);
    Optional<Sala> actualizarSala(Long id, Sala sala);
    boolean eliminarSala(Long id);
    Optional<Sala> obtenerSalaPorId(Long id);
    List<Sala> listarSalas();
}
