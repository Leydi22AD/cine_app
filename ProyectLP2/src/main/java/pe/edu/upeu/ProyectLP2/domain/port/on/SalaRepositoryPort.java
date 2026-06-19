package pe.edu.upeu.ProyectLP2.domain.port.on;

import pe.edu.upeu.ProyectLP2.domain.model.Sala;

import java.util.List;
import java.util.Optional;

public interface SalaRepositoryPort {

    Sala save(Sala sala);
    Optional<Sala> findById(Long id);
    List<Sala> findAll();
    void deleteById(Long id);
    Sala update(Long id, Sala sala);

}
