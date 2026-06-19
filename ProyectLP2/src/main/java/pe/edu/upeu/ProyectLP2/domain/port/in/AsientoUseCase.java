package pe.edu.upeu.ProyectLP2.domain.port.in;


import pe.edu.upeu.ProyectLP2.domain.model.Asiento;

import java.util.List;
import java.util.Optional;

public interface AsientoUseCase {

    Asiento crearAsiento(Asiento asiento);
    Optional<Asiento> actualizarAsiento(Long id, Asiento asiento);
    boolean eliminarAsiento(Long id);
    Optional<Asiento> obtenerAsientoPorId(Long id);
    List<Asiento> listarAsientosPorSala(Long salaId);
    List<Asiento> findAll();



}
