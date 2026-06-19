package pe.edu.upeu.ProyectLP2.app.usecase;

import org.springframework.stereotype.Service;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.port.in.AsientoUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.on.AsientoRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
public class AsientoUseCaseImpl implements AsientoUseCase {

    private final AsientoRepositoryPort asientoRepositoryPort;

    public AsientoUseCaseImpl(AsientoRepositoryPort asientoRepositoryPort) {
        this.asientoRepositoryPort = asientoRepositoryPort;
    }

    @Override
    public Asiento crearAsiento(Asiento asiento) {
        return asientoRepositoryPort.save(asiento);
    }

    @Override
    public Optional<Asiento> actualizarAsiento(Long id, Asiento asiento) {
        return asientoRepositoryPort.findById(id).map(existing -> {
            asiento.setIdAsiento(id); // mantener ID
            return asientoRepositoryPort.update(id , asiento);
        });
    }

    @Override
    public boolean eliminarAsiento(Long id) {
        return asientoRepositoryPort.findById(id).map(a -> {
            asientoRepositoryPort.deleteById(id);
            return true;
        }).orElse(false);    }

    @Override
    public Optional<Asiento> obtenerAsientoPorId(Long id) {
        return asientoRepositoryPort.findById(id);
    }

    @Override
    public List<Asiento> listarAsientosPorSala(Long salaId) {
        return asientoRepositoryPort.findBySalaId(salaId);
    }

    @Override
    public List<Asiento> findAll() {
        return asientoRepositoryPort.findAll();
    }
}
