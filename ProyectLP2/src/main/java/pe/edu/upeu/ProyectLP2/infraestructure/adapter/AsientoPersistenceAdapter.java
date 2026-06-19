package pe.edu.upeu.ProyectLP2.infraestructure.adapter;

import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.exception.AsientoAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.port.on.AsientoRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.AsientoMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;

import java.util.List;
import java.util.Optional;

@Component
public class AsientoPersistenceAdapter implements AsientoRepositoryPort {

    private final AsientoRepository asientoRepository;
    private final AsientoMapper asientoMapper;

    public AsientoPersistenceAdapter(AsientoRepository asientoRepository, AsientoMapper asientoMapper) {
        this.asientoRepository = asientoRepository;
        this.asientoMapper = asientoMapper;
    }

    @Override
    public Asiento save(Asiento asiento) {
        // Validamos si ya existe un asiento en esa fila y columna dentro de la misma sala
        Long salaId = asiento.getSala().getIdSala();
        Optional<AsientoEntity> existente = asientoRepository.findBySala_IdSalaAndFilaAndColumna(
                salaId, asiento.getFila(), asiento.getColumna()
        );

        if (existente.isPresent()) {
            throw new AsientoAlreadyExistsException("El asiento ya existe en la sala (fila: "
                    + asiento.getFila() + ", columna: " + asiento.getColumna() + ")");
        }

        AsientoEntity entity = asientoMapper.toEntity(asiento);
        AsientoEntity savedEntity = asientoRepository.save(entity);
        return asientoMapper.toDomainModel(savedEntity);
    }

    @Override
    public Optional<Asiento> findById(Long id) {
        return asientoRepository.findById(id).map(asientoMapper::toDomainModel);
    }

    @Override
    public List<Asiento> findAll() {
        return asientoMapper.toDomainModelList(asientoRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        asientoRepository.deleteById(id);
    }

    @Override
    public Asiento update(Long id, Asiento asiento) {
        AsientoEntity asientoEntity = asientoRepository.findById(id)
                .orElseThrow(() -> new AsientoAlreadyExistsException("Asiento no encontrado con id: " + id));

        asientoEntity.setFila(asiento.getFila());
        asientoEntity.setColumna(asiento.getColumna());
        asientoEntity.setEstado(asiento.getEstado());

        AsientoEntity updatedEntity = asientoRepository.save(asientoEntity);
        return asientoMapper.toDomainModel(updatedEntity);
    }

    @Override
    public List<Asiento> findBySalaId(Long salaId) {
        List<AsientoEntity> entities = asientoRepository.findBySala_IdSala(salaId);
        return asientoMapper.toDomainModelList(entities);
    }
}