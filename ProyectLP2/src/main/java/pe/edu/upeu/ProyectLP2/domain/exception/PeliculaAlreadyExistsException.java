package pe.edu.upeu.ProyectLP2.domain.exception;

public class PeliculaAlreadyExistsException extends RuntimeException {
    public PeliculaAlreadyExistsException(String message) {
        super(message);
    }
}
