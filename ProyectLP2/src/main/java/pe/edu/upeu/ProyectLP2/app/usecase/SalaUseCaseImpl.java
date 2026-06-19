package pe.edu.upeu.ProyectLP2.app.usecase;

import org.springframework.stereotype.Service;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.exception.AsientoAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.port.in.SalaUseCase;
import pe.edu.upeu.ProyectLP2.domain.port.on.AsientoRepositoryPort;
import pe.edu.upeu.ProyectLP2.domain.port.on.SalaRepositoryPort;

import java.util.List;
import java.util.Optional;

@Service
public class SalaUseCaseImpl implements SalaUseCase {

    private final SalaRepositoryPort salaRepositoryPort;
    private final AsientoRepositoryPort asientoRepositoryPort;

    public SalaUseCaseImpl(SalaRepositoryPort salaRepositoryPort, AsientoRepositoryPort asientoRepositoryPort) {
        this.salaRepositoryPort = salaRepositoryPort;
        this.asientoRepositoryPort = asientoRepositoryPort;
    }

    @Override
    public Sala registrarSala(Sala sala) {
        // Guardamos directamente la sala (ya viene con cine asignado)
        Sala salaSaved = salaRepositoryPort.save(sala);
        
        // Generamos automáticamente los asientos según filas y columnas
        generarAsientos(salaSaved);
        
        return salaSaved;
    }
    
    private void generarAsientos(Sala sala) {
        // Evitar NPE y valores inválidos
        int filas = sala.getFilas() == null ? 0 : sala.getFilas();
        int columnas = sala.getColumnas() == null ? 0 : sala.getColumnas();

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 1; columna <= columnas; columna++) {
                Asiento asiento = new Asiento();
                asiento.setFila(fila + 1); // 1, 2, 3...
                asiento.setColumna(columna); // 1, 2, 3...
                asiento.setEstado("LIBRE");
                asiento.setSala(sala);

                try {
                    asientoRepositoryPort.save(asiento);
                } catch (AsientoAlreadyExistsException e) {
                    // Si por alguna razón ya existe, continuar con el siguiente asiento
                    // (evita que una excepción interrumpa la generación completa)
                }
            }
        }
    }

    @Override
    public Optional<Sala> actualizarSala(Long id, Sala sala) {
        return salaRepositoryPort.findById(id).map(existingSala -> {
        sala.setIdSala(id); // mantenemos el ID
        return salaRepositoryPort.update(id, sala);
    });
    }

    @Override
    public boolean eliminarSala(Long id) {
        return salaRepositoryPort.findById(id).map(sala -> {
            salaRepositoryPort.deleteById(id);
            return true;
        }).orElse(false);
    }

    @Override
    public Optional<Sala> obtenerSalaPorId(Long id) {
        return salaRepositoryPort.findById(id);
    }

    @Override
    public List<Sala> listarSalas() {
        return salaRepositoryPort.findAll();
    }
}
