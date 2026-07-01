package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.TicketEntity;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findByFuncion_IdFuncion(Long funcionId);

    /**
     * Consulta personalizada para obtener un ticket con todas sus relaciones cargadas (EAGER).
     * Esto evita la LazyInitializationException en las capas superiores.
     */
    @Query("SELECT t FROM TicketEntity t " +
            "JOIN FETCH t.funcion f " +
            "JOIN FETCH f.sala s " +
            "JOIN FETCH f.pelicula p " +
            "JOIN FETCH t.asiento a " +
            "JOIN FETCH t.cliente c " +
            "WHERE t.idTicket = :id")
    Optional<TicketEntity> findTicketDetailsById(@Param("id") Long id);
}