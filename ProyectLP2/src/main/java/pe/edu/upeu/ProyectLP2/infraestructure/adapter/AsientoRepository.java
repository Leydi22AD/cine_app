package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsientoRepository extends JpaRepository<AsientoEntity, Long> {

    // Buscar asientos por sala
    List<AsientoEntity> findBySala_IdSala(Long salaId);

    // Verificar si un asiento (fila y columna) ya existe en una sala
    Optional<AsientoEntity> findBySala_IdSalaAndFilaAndColumna(Long salaId, Integer fila, Integer columna);

}
