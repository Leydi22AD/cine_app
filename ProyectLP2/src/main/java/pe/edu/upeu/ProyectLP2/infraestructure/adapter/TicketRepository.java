package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    // Buscar todos los tickets por función
    List<TicketEntity> findByFuncion_IdFuncion(Long funcionId);
}
