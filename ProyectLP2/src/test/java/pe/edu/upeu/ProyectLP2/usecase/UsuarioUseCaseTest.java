package pe.edu.upeu.ProyectLP2.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.ProyectLP2.app.usecase.UsuarioUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.exception.UsuarioAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.domain.port.on.UsuarioRepositoryPort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    // Inyectamos directamente tu implementación real
    @InjectMocks
    private UsuarioUseCaseImpl usuarioUseCase;

    @Test
    void whenUserDoesNotExist_thenRegisterSuccessfully() {
        // Preparación de datos (GIVEN)
        Usuario usuario = new Usuario();
        usuario.setEmail("leydi@upeu.edu.pe");
        usuario.setNombre("Leydi Arevalo");

        when(usuarioRepositoryPort.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(usuario);

        // Acción (WHEN)
        Usuario result = usuarioUseCase.registrarUsuario(usuario);

        // Verificación (THEN)
        assertNotNull(result);
        assertEquals("leydi@upeu.edu.pe", result.getEmail());
        verify(usuarioRepositoryPort, times(1)).findByEmail(usuario.getEmail());
        verify(usuarioRepositoryPort, times(1)).save(usuario);
    }

    @Test
    void whenUserEmailAlreadyExists_thenThrowUsuarioAlreadyExistsException() {
        // Preparación de datos (GIVEN)
        Usuario usuario = new Usuario();
        usuario.setEmail("leydi@upeu.edu.pe");

        // Simulamos que el email ya está registrado en el puerto de persistencia
        when(usuarioRepositoryPort.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // Acción y Verificación (WHEN / THEN)
        assertThrows(UsuarioAlreadyExistsException.class, () -> {
            usuarioUseCase.registrarUsuario(usuario);
        });

        // Aseguramos que nunca se llame al método save si hay duplicado
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void whenUserExists_thenDeleteReturnsTrue() {
        Long idEliminar = 1L;
        Usuario usuarioMock = new Usuario();

        when(usuarioRepositoryPort.findById(idEliminar)).thenReturn(Optional.of(usuarioMock));
        doNothing().when(usuarioRepositoryPort).deleteById(idEliminar);

        boolean eliminado = usuarioUseCase.eliminarUsuario(idEliminar);

        assertTrue(eliminado);
        verify(usuarioRepositoryPort, times(1)).deleteById(idEliminar);
    }

    @Test
    void whenUserDoesNotExist_thenDeleteReturnsFalse() {
        Long idInexistente = 99L;

        when(usuarioRepositoryPort.findById(idInexistente)).thenReturn(Optional.empty());

        boolean eliminado = usuarioUseCase.eliminarUsuario(idInexistente);

        assertFalse(eliminado);
        verify(usuarioRepositoryPort, never()).deleteById(anyLong());
    }
}