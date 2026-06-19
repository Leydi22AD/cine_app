package pe.edu.upeu.ProyectLP2.domain.exception;

public class CineAlreadyExistsException extends RuntimeException {
    public CineAlreadyExistsException(String message) {
        super(message);
    }
}
