package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.port.on.FuncionRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.FuncionMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.PeliculaEntity;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FuncionPersistenceAdapter implements FuncionRepositoryPort {

    private final FuncionRepository funcionRepository;
    private final FuncionMapper funcionMapper;
    private final PeliculaRepository peliculaRepository;
    private final SalaRepository salaRepository;;

    public FuncionPersistenceAdapter(FuncionRepository funcionRepository, FuncionMapper funcionMapper, PeliculaRepository peliculaRepository, SalaRepository salaRepository) {
        this.funcionRepository = funcionRepository;
        this.funcionMapper = funcionMapper;
        this.peliculaRepository = peliculaRepository;
        this.salaRepository = salaRepository;
    }

    @Override
    public Funcion save(Funcion funcion) {
        PeliculaEntity peliculaEntity = peliculaRepository.findById(funcion.getPelicula().getIdPelicula())
                .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));

        SalaEntity salaEntity = salaRepository.findById(funcion.getSala().getIdSala())
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));

        FuncionEntity entity = funcionMapper.toEntity(funcion);
        entity.setPelicula(peliculaEntity);
        entity.setSala(salaEntity);

        FuncionEntity saved = funcionRepository.save(entity);
        return funcionMapper.toDomainModel(saved);
    }

    @Override
    public Funcion update(Long id, Funcion funcion) {
        FuncionEntity existing = funcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Función no encontrada"));

        existing.setFecha(funcion.getFecha());
        existing.setPrecio(funcion.getPrecio());

        PeliculaEntity peliculaEntity = peliculaRepository.findById(funcion.getPelicula().getIdPelicula())
                .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));
        SalaEntity salaEntity = salaRepository.findById(funcion.getSala().getIdSala())
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));

        existing.setPelicula(peliculaEntity);
        existing.setSala(salaEntity);

        FuncionEntity updated = funcionRepository.save(existing);
        return funcionMapper.toDomainModel(updated);
    }

    @Override
    public Optional<Funcion> findById(Long id) {
        return funcionRepository.findById(id).map(funcionMapper::toDomainModel);


    }

    @Override
    public List<Funcion> findAll() {
        return funcionMapper.toDomainModelList(funcionRepository.findAll());


    }

    @Override
    public List<Funcion> findByPeliculaId(Long peliculaId) {
        return funcionMapper.toDomainModelList(funcionRepository.findByPelicula_IdPelicula(peliculaId));
    }

    @Override
    public void deleteById(Long id) {
        funcionRepository.deleteById(id);

    }
}
