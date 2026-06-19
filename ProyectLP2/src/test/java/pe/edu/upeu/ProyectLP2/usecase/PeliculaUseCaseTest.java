package pe.edu.upeu.ProyectLP2.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.ProyectLP2.app.usecase.PeliculaUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.port.on.PeliculaRepositoryPort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaUseCaseTest {

    @Mock
    private PeliculaRepositoryPort peliculaRepositoryPort;

    @InjectMocks
    private PeliculaUseCaseImpl peliculaUseCase;

    @Test
    void whenMovieIsValid_thenRegisterSuccessfully() {

        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(1L);
        pelicula.setTitulo("Avatar");

        when(peliculaRepositoryPort.save(pelicula))
                .thenReturn(pelicula);

        Pelicula result =
                peliculaUseCase.registrarPelicula(pelicula);

        assertNotNull(result);
        assertEquals("Avatar", result.getTitulo());

        verify(peliculaRepositoryPort, times(1))
                .save(pelicula);
    }

    @Test
    void whenMovieExists_thenUpdateSuccessfully() {

        Long id = 1L;

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("Titanic");

        when(peliculaRepositoryPort.update(id, pelicula))
                .thenReturn(pelicula);

        Optional<Pelicula> result =
                peliculaUseCase.actualizarPelicula(id, pelicula);

        assertTrue(result.isPresent());
        assertEquals("Titanic", result.get().getTitulo());

        verify(peliculaRepositoryPort)
                .update(id, pelicula);
    }

    @Test
    void whenUpdateFails_thenReturnEmptyOptional() {

        Long id = 1L;

        Pelicula pelicula = new Pelicula();

        when(peliculaRepositoryPort.update(id, pelicula))
                .thenThrow(new RuntimeException());

        Optional<Pelicula> result =
                peliculaUseCase.actualizarPelicula(id, pelicula);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenMovieExists_thenReturnMovieById() {

        Long id = 1L;

        Pelicula pelicula = new Pelicula();
        pelicula.setIdPelicula(id);

        when(peliculaRepositoryPort.findById(id))
                .thenReturn(Optional.of(pelicula));

        Optional<Pelicula> result =
                peliculaUseCase.obtenerPeliculaPorId(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getIdPelicula());
    }

    @Test
    void whenSearchingByTitle_thenReturnMovies() {

        List<Pelicula> peliculas = Arrays.asList(
                new Pelicula(),
                new Pelicula()
        );

        when(peliculaRepositoryPort.findByTitulo("Avatar"))
                .thenReturn(peliculas);

        List<Pelicula> result =
                peliculaUseCase.buscarPeliculasPorTitulo("Avatar");

        assertEquals(2, result.size());

        verify(peliculaRepositoryPort)
                .findByTitulo("Avatar");
    }

    @Test
    void whenListingMovies_thenReturnAllMovies() {

        List<Pelicula> peliculas = Arrays.asList(
                new Pelicula(),
                new Pelicula(),
                new Pelicula()
        );

        when(peliculaRepositoryPort.findAll())
                .thenReturn(peliculas);

        List<Pelicula> result =
                peliculaUseCase.listarPeliculas();

        assertEquals(3, result.size());

        verify(peliculaRepositoryPort)
                .findAll();
    }

    @Test
    void whenMovieExists_thenDeleteSuccessfully() {

        Long id = 1L;

        Pelicula pelicula = new Pelicula();

        when(peliculaRepositoryPort.findById(id))
                .thenReturn(Optional.of(pelicula));

        doNothing()
                .when(peliculaRepositoryPort)
                .deleteById(id);

        boolean result =
                peliculaUseCase.eliminarPelicula(id);

        assertTrue(result);

        verify(peliculaRepositoryPort)
                .deleteById(id);
    }

    @Test
    void whenMovieDoesNotExist_thenDeleteReturnsFalse() {

        Long id = 99L;

        when(peliculaRepositoryPort.findById(id))
                .thenReturn(Optional.empty());

        boolean result =
                peliculaUseCase.eliminarPelicula(id);

        assertFalse(result);

        verify(peliculaRepositoryPort, never())
                .deleteById(anyLong());
    }
}