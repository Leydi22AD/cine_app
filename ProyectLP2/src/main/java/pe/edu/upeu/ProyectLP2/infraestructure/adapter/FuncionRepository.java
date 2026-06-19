package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;

import java.util.List;

public interface FuncionRepository extends JpaRepository<FuncionEntity, Long> {

    // Buscar todas las funciones de una película
    List<FuncionEntity> findByPelicula_IdPelicula(Long peliculaId);

}
