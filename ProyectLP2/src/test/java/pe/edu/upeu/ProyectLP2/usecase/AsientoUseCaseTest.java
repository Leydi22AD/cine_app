package pe.edu.upeu.ProyectLP2.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.ProyectLP2.app.usecase.AsientoUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.port.on.AsientoRepositoryPort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsientoUseCaseTest {

    @Mock
    private AsientoRepositoryPort asientoRepositoryPort;

    @InjectMocks
    private AsientoUseCaseImpl asientoUseCase;

    private Asiento asiento;

    @BeforeEach
    void setUp() {
        asiento = new Asiento();
        asiento.setIdAsiento(1L);
        asiento.setFila(1);
        asiento.setColumna(1);
        asiento.setEstado("LIBRE");
    }

    @Test
    void deberiaCrearAsiento() {
        when(asientoRepositoryPort.save(asiento)).thenReturn(asiento);

        Asiento resultado = asientoUseCase.crearAsiento(asiento);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAsiento());
        verify(asientoRepositoryPort, times(1)).save(asiento);
    }

    @Test
    void deberiaActualizarAsientoCuandoExiste() {
        Asiento actualizado = new Asiento(1L, 2, 2, "OCUPADO", null);

        when(asientoRepositoryPort.findById(1L)).thenReturn(Optional.of(asiento));
        when(asientoRepositoryPort.update(1L, actualizado)).thenReturn(actualizado);

        Optional<Asiento> resultado = asientoUseCase.actualizarAsiento(1L, actualizado);

        assertTrue(resultado.isPresent());
        assertEquals(2, resultado.get().getFila());
        assertEquals("OCUPADO", resultado.get().getEstado());

        verify(asientoRepositoryPort).findById(1L);
        verify(asientoRepositoryPort).update(1L, actualizado);
    }

    @Test
    void noDebeActualizarSiNoExiste() {
        when(asientoRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        Optional<Asiento> resultado = asientoUseCase.actualizarAsiento(1L, asiento);

        assertTrue(resultado.isEmpty());
        verify(asientoRepositoryPort, never()).update(anyLong(), any());
    }

    @Test
    void deberiaEliminarAsientoSiExiste() {
        when(asientoRepositoryPort.findById(1L)).thenReturn(Optional.of(asiento));

        boolean resultado = asientoUseCase.eliminarAsiento(1L);

        assertTrue(resultado);
        verify(asientoRepositoryPort).deleteById(1L);
    }

    @Test
    void noDebeEliminarSiNoExiste() {
        when(asientoRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        boolean resultado = asientoUseCase.eliminarAsiento(1L);

        assertFalse(resultado);
        verify(asientoRepositoryPort, never()).deleteById(anyLong());
    }

    @Test
    void deberiaObtenerAsientoPorId() {
        when(asientoRepositoryPort.findById(1L)).thenReturn(Optional.of(asiento));

        Optional<Asiento> resultado = asientoUseCase.obtenerAsientoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdAsiento());
    }

    @Test
    void deberiaListarAsientosPorSala() {
        List<Asiento> lista = List.of(asiento);

        when(asientoRepositoryPort.findBySalaId(10L)).thenReturn(lista);

        List<Asiento> resultado = asientoUseCase.listarAsientosPorSala(10L);

        assertEquals(1, resultado.size());
        verify(asientoRepositoryPort).findBySalaId(10L);
    }

    @Test
    void deberiaListarTodosLosAsientos() {
        List<Asiento> lista = List.of(asiento);

        when(asientoRepositoryPort.findAll()).thenReturn(lista);

        List<Asiento> resultado = asientoUseCase.findAll();

        assertEquals(1, resultado.size());
        verify(asientoRepositoryPort).findAll();
    }
}