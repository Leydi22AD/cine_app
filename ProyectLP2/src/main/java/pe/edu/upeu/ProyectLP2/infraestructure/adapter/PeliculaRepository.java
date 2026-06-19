package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.PeliculaEntity;

import java.util.List;

public interface PeliculaRepository extends JpaRepository<PeliculaEntity, Long> {

    // Buscar películas por título (insensible a mayúsculas/minúsculas)
    List<PeliculaEntity> findByTitulo(String titulo);

}
