package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.UsuarioEntity;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Buscar usuario por email (devuelve un Optional porque puede o no existir)
    Optional<UsuarioEntity> findByEmail(String email);
}
