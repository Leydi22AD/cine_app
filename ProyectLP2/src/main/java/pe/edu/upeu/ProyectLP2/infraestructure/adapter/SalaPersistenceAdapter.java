package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.exception.SalaAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.port.on.SalaRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.SalaMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;

import java.util.List;
import java.util.Optional;

@Component
public class SalaPersistenceAdapter implements SalaRepositoryPort {

    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    public SalaPersistenceAdapter(SalaRepository salaRepository, SalaMapper salaMapper) {
        this.salaRepository = salaRepository;
        this.salaMapper = salaMapper;
    }

    @Override
    public Sala save(Sala sala) {
        SalaEntity salaEntity = salaMapper.toEntity(sala);
        SalaEntity savedEntity = salaRepository.save(salaEntity);
        return salaMapper.toDomainModel(savedEntity);

    }

    @Override
    public Optional<Sala> findById(Long id) {
        return salaRepository.findById(id)
                .map(salaMapper::toDomainModel);    }

    @Override
    public List<Sala> findAll() {
        List<SalaEntity> salaEntities = salaRepository.findAll();
        return salaMapper.toDomainModelList(salaEntities);
    }


    @Override
    public void deleteById(Long id) {
        salaRepository.deleteById(id);

    }

    @Override
    public Sala update(Long id,Sala sala) {
        SalaEntity entity = salaRepository.findById(id)
                .orElseThrow(() -> new SalaAlreadyExistsException("Sala no encontrada con id: " + id));

        entity.setNumero(sala.getNumero());
        entity.setFilas(sala.getFilas());
        entity.setColumnas(sala.getColumnas());

        SalaEntity updated = salaRepository.save(entity);
        return salaMapper.toDomainModel(updated);    }
}
