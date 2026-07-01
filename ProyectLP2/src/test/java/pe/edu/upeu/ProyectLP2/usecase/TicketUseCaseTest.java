package pe.edu.upeu.ProyectLP2.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.ProyectLP2.app.usecase.TicketUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.domain.port.on.TicketRepositoryPort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketUseCaseTest {

    @Mock
    private TicketRepositoryPort ticketRepositoryPort;

    @InjectMocks
    private TicketUseCaseImpl ticketUseCase;

    @Test
    void whenFuncionHasPrice_thenAssignPriceAndSaveTicket() {
        // Arrange
        Funcion funcion = new Funcion();
        funcion.setIdFuncion(1L);
        funcion.setPrecio(new BigDecimal("25.00"));

        Asiento asiento = new Asiento();
        asiento.setIdAsiento(1L);

        Ticket ticket = new Ticket();
        ticket.setFuncion(funcion);
        ticket.setAsiento(asiento);

        when(ticketRepositoryPort.findByFuncionId(1L)).thenReturn(Collections.emptyList());
        when(ticketRepositoryPort.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket result = ticketUseCase.crearTicket(ticket);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("25.00"), result.getPrecioUnitario());
        verify(ticketRepositoryPort, times(1)).save(ticket);
    }

    @Test
    void whenFuncionIsNull_thenSaveTicketWithoutAssigningPrice() {
        // Arrange
        Ticket ticket = new Ticket();
        Asiento asiento = new Asiento();
        asiento.setIdAsiento(1L);
        ticket.setAsiento(asiento);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ticketUseCase.crearTicket(ticket);
        });

        verify(ticketRepositoryPort, never()).save(any(Ticket.class));
    }

    @Test
    void whenTicketExists_thenReturnTicketById() {
        // Arrange
        Long id = 1L;
        Ticket ticket = new Ticket();
        ticket.setIdTicket(id);
        when(ticketRepositoryPort.findById(id)).thenReturn(Optional.of(ticket));

        // Act
        Optional<Ticket> result = ticketUseCase.obtenerTicketPorId(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getIdTicket());
    }

    @Test
    void whenListingTickets_thenReturnAllTickets() {
        // Arrange
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        when(ticketRepositoryPort.findAll()).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketUseCase.listarTickets();

        // Assert
        assertEquals(2, result.size());
        verify(ticketRepositoryPort, times(1)).findAll();
    }

    @Test
    void whenTicketExists_thenUpdateSuccessfully() {
        // Arrange
        Long id = 1L;
        Ticket ticket = new Ticket();
        when(ticketRepositoryPort.findById(id)).thenReturn(Optional.of(new Ticket()));
        when(ticketRepositoryPort.update(id, ticket)).thenReturn(Optional.of(ticket));

        // Act
        Optional<Ticket> result = ticketUseCase.actualizarTicket(id, ticket);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, ticket.getIdTicket());
        verify(ticketRepositoryPort, times(1)).update(id, ticket);
    }

    @Test
    void whenTicketDoesNotExist_thenUpdateReturnsEmpty() {
        // Arrange
        Long id = 99L;
        Ticket ticket = new Ticket();
        when(ticketRepositoryPort.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Ticket> result = ticketUseCase.actualizarTicket(id, ticket);

        // Assert
        assertTrue(result.isEmpty());
        verify(ticketRepositoryPort, never()).update(anyLong(), any(Ticket.class));
    }

    @Test
    void whenListingTicketsByFuncion_thenReturnFilteredTickets() {
        // Arrange
        Long funcionId = 1L;
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        when(ticketRepositoryPort.findByFuncionId(funcionId)).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketUseCase.listarTicketsPorFuncion(funcionId);

        // Assert
        assertEquals(2, result.size());
        verify(ticketRepositoryPort, times(1)).findByFuncionId(funcionId);
    }

    @Test
    void whenTicketExists_thenDeleteReturnsTrue() {
        // Arrange
        Long id = 1L;
        Ticket ticket = new Ticket();
        when(ticketRepositoryPort.findById(id)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketRepositoryPort).deleteById(id);

        // Act
        boolean result = ticketUseCase.anularTicket(id);

        // Assert
        assertTrue(result);
        verify(ticketRepositoryPort, times(1)).deleteById(id);
    }

    @Test
    void whenTicketDoesNotExist_thenDeleteReturnsFalse() {
        // Arrange
        Long id = 99L;
        when(ticketRepositoryPort.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = ticketUseCase.anularTicket(id);

        // Assert
        assertFalse(result);
        verify(ticketRepositoryPort, never()).deleteById(anyLong());
    }
}