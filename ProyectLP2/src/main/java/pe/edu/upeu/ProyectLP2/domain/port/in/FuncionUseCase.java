package pe.edu.upeu.ProyectLP2.domain.port.in;

import pe.edu.upeu.ProyectLP2.domain.model.Funcion;

import java.util.List;
import java.util.Optional;

public interface FuncionUseCase {
    // Crea una nueva función (fecha, hora, sala, precio)
    Funcion crearFuncion(Funcion funcion);

    // Actualiza una función existente
    Optional<Funcion> actualizarFuncion(Long id, Funcion funcion);

    Optional<Funcion> obtenerFuncionPorId(Long id);

    // Elimina una función según su ID
    boolean eliminarFuncion(Long id);

    List<Funcion> listarFunciones();

    // Lista todas las funciones de una película
    List<Funcion> listarFuncionesPorPelicula(Long peliculaId);

}
