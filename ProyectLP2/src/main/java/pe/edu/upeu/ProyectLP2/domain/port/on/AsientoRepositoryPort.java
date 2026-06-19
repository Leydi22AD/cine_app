package pe.edu.upeu.ProyectLP2.domain.port.on;

import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;

import java.util.List;
import java.util.Optional;

public interface AsientoRepositoryPort {

    Asiento save(Asiento asiento);
    Optional<Asiento> findById(Long id);
    List<Asiento> findAll();
    void deleteById(Long id);
    Asiento update(Long id, Asiento asiento);
    List<Asiento> findBySalaId(Long salaId);

}
