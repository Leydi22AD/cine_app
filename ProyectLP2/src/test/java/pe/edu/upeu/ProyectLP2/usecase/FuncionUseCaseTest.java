package pe.edu.upeu.ProyectLP2.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.ProyectLP2.app.usecase.FuncionUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.exception.FuncionAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.domain.port.on.FuncionRepositoryPort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionUseCaseTest {

    @Mock
    private FuncionRepositoryPort funcionRepositoryPort;

    @InjectMocks
    private FuncionUseCaseImpl funcionUseCase;

    @Test
    void whenFunctionDoesNotExist_thenCreateSuccessfully() {

        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(1L);

        Sala sala = new Sala();
        sala.setIdSala(1L);

        Funcion funcion = new Funcion();
        funcion.setPelicula(pelicula);
        funcion.setSala(sala);
        funcion.setFecha(LocalDateTime.now());

        when(funcionRepositoryPort.findByPeliculaId(1L))
                .thenReturn(Collections.emptyList());

        when(funcionRepositoryPort.save(funcion))
                .thenReturn(funcion);

        Funcion result = funcionUseCase.crearFuncion(funcion);

        assertNotNull(result);

        verify(funcionRepositoryPort).save(funcion);
    }

    @Test
    void whenFunctionAlreadyExists_thenThrowException() {

        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(1L);

        Sala sala = new Sala();
        sala.setIdSala(1L);

        LocalDateTime fecha = LocalDateTime.now();

        Funcion existente = new Funcion();
        existente.setPelicula(pelicula);
        existente.setSala(sala);
        existente.setFecha(fecha);

        Funcion nueva = new Funcion();
        nueva.setPelicula(pelicula);
        nueva.setSala(sala);
        nueva.setFecha(fecha);

        when(funcionRepositoryPort.findByPeliculaId(1L))
                .thenReturn(List.of(existente));

        assertThrows(
                FuncionAlreadyExistsException.class,
                () -> funcionUseCase.crearFuncion(nueva)
        );

        verify(funcionRepositoryPort, never())
                .save(any());
    }

    @Test
    void whenUpdateSuccessful_thenReturnUpdatedFunction() {

        Long id = 1L;

        Funcion funcion = new Funcion();

        when(funcionRepositoryPort.update(id, funcion))
                .thenReturn(funcion);

        Optional<Funcion> result =
                funcionUseCase.actualizarFuncion(id, funcion);

        assertTrue(result.isPresent());
    }

    @Test
    void whenUpdateFails_thenReturnEmptyOptional() {

        Long id = 1L;

        Funcion funcion = new Funcion();

        when(funcionRepositoryPort.update(id, funcion))
                .thenThrow(new RuntimeException());

        Optional<Funcion> result =
                funcionUseCase.actualizarFuncion(id, funcion);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenFunctionExists_thenReturnById() {

        Long id = 1L;

        Funcion funcion = new Funcion();

        when(funcionRepositoryPort.findById(id))
                .thenReturn(Optional.of(funcion));

        Optional<Funcion> result =
                funcionUseCase.obtenerFuncionPorId(id);

        assertTrue(result.isPresent());
    }

    @Test
    void whenDeleteSuccessful_thenReturnTrue() {

        Long id = 1L;

        doNothing().when(funcionRepositoryPort)
                .deleteById(id);

        boolean result =
                funcionUseCase.eliminarFuncion(id);

        assertTrue(result);
    }

    @Test
    void whenDeleteFails_thenReturnFalse() {

        Long id = 1L;

        doThrow(new RuntimeException())
                .when(funcionRepositoryPort)
                .deleteById(id);

        boolean result =
                funcionUseCase.eliminarFuncion(id);

        assertFalse(result);
    }

    @Test
    void whenListFunctions_thenReturnAllFunctions() {

        List<Funcion> funciones =
                Arrays.asList(new Funcion(), new Funcion());

        when(funcionRepositoryPort.findAll())
                .thenReturn(funciones);

        List<Funcion> result =
                funcionUseCase.listarFunciones();

        assertEquals(2, result.size());
    }

    @Test
    void whenListFunctionsByMovie_thenReturnFilteredFunctions() {

        Long peliculaId = 1L;

        List<Funcion> funciones =
                Arrays.asList(new Funcion(), new Funcion());

        when(funcionRepositoryPort.findByPeliculaId(peliculaId))
                .thenReturn(funciones);

        List<Funcion> result =
                funcionUseCase.listarFuncionesPorPelicula(peliculaId);

        assertEquals(2, result.size());

        verify(funcionRepositoryPort)
                .findByPeliculaId(peliculaId);
    }
}