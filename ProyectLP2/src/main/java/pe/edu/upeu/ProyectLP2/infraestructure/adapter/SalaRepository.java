package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;

import java.util.List;

public interface SalaRepository extends JpaRepository<SalaEntity, Long> {



}
