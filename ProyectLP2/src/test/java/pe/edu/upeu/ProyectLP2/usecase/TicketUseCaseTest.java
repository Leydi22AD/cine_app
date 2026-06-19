package pe.edu.upeu.ProyectLP2.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.ProyectLP2.app.usecase.TicketUseCaseImpl;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.domain.model.Ticket;
import pe.edu.upeu.ProyectLP2.domain.port.on.TicketRepositoryPort;

import java.math.BigDecimal;
import java.util.Arrays;
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

        Funcion funcion = new Funcion();
        funcion.setPrecio(new BigDecimal("25.00"));

        Ticket ticket = new Ticket();
        ticket.setFuncion(funcion);

        when(ticketRepositoryPort.save(any(Ticket.class)))
                .thenReturn(ticket);

        Ticket result = ticketUseCase.crearTicket(ticket);

        assertNotNull(result);
        assertEquals(new BigDecimal("25.00"), result.getPrecioUnitario());

        verify(ticketRepositoryPort, times(1))
                .save(ticket);
    }

    @Test
    void whenFuncionIsNull_thenSaveTicketWithoutAssigningPrice() {

        Ticket ticket = new Ticket();

        when(ticketRepositoryPort.save(any(Ticket.class)))
                .thenReturn(ticket);

        Ticket result = ticketUseCase.crearTicket(ticket);

        assertNotNull(result);
        assertNull(result.getPrecioUnitario());

        verify(ticketRepositoryPort, times(1))
                .save(ticket);
    }

    @Test
    void whenTicketExists_thenReturnTicketById() {

        Long id = 1L;

        Ticket ticket = new Ticket();
        ticket.setIdTicket(id);

        when(ticketRepositoryPort.findById(id))
                .thenReturn(Optional.of(ticket));

        Optional<Ticket> result =
                ticketUseCase.obtenerTicketPorId(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getIdTicket());
    }

    @Test
    void whenListingTickets_thenReturnAllTickets() {

        List<Ticket> tickets =
                Arrays.asList(new Ticket(), new Ticket());

        when(ticketRepositoryPort.findAll())
                .thenReturn(tickets);

        List<Ticket> result =
                ticketUseCase.listarTickets();

        assertEquals(2, result.size());

        verify(ticketRepositoryPort, times(1))
                .findAll();
    }

    @Test
    void whenTicketExists_thenUpdateSuccessfully() {

        Long id = 1L;

        Ticket ticket = new Ticket();

        when(ticketRepositoryPort.findById(id))
                .thenReturn(Optional.of(new Ticket()));

        when(ticketRepositoryPort.update(id, ticket))
                .thenReturn(Optional.of(ticket));

        Optional<Ticket> result =
                ticketUseCase.actualizarTicket(id, ticket);

        assertTrue(result.isPresent());
        assertEquals(id, ticket.getIdTicket());

        verify(ticketRepositoryPort, times(1))
                .update(id, ticket);
    }

    @Test
    void whenTicketDoesNotExist_thenUpdateReturnsEmpty() {

        Long id = 99L;

        Ticket ticket = new Ticket();

        when(ticketRepositoryPort.findById(id))
                .thenReturn(Optional.empty());

        Optional<Ticket> result =
                ticketUseCase.actualizarTicket(id, ticket);

        assertTrue(result.isEmpty());

        verify(ticketRepositoryPort, never())
                .update(anyLong(), any(Ticket.class));
    }

    @Test
    void whenListingTicketsByFuncion_thenReturnFilteredTickets() {

        Long funcionId = 1L;

        List<Ticket> tickets =
                Arrays.asList(new Ticket(), new Ticket());

        when(ticketRepositoryPort.findByFuncionId(funcionId))
                .thenReturn(tickets);

        List<Ticket> result =
                ticketUseCase.listarTicketsPorFuncion(funcionId);

        assertEquals(2, result.size());

        verify(ticketRepositoryPort, times(1))
                .findByFuncionId(funcionId);
    }

    @Test
    void whenTicketExists_thenDeleteReturnsTrue() {

        Long id = 1L;

        Ticket ticket = new Ticket();

        when(ticketRepositoryPort.findById(id))
                .thenReturn(Optional.of(ticket));

        doNothing().when(ticketRepositoryPort)
                .deleteById(id);

        boolean result =
                ticketUseCase.anularTicket(id);

        assertTrue(result);

        verify(ticketRepositoryPort, times(1))
                .deleteById(id);
    }

    @Test
    void whenTicketDoesNotExist_thenDeleteReturnsFalse() {

        Long id = 99L;

        when(ticketRepositoryPort.findById(id))
                .thenReturn(Optional.empty());

        boolean result =
                ticketUseCase.anularTicket(id);

        assertFalse(result);

        verify(ticketRepositoryPort, never())
                .deleteById(anyLong());
    }
}