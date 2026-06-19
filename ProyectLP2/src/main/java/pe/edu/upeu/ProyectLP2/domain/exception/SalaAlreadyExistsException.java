package pe.edu.upeu.ProyectLP2.domain.exception;

public class SalaAlreadyExistsException extends RuntimeException {
    public SalaAlreadyExistsException(String message) {
        super(message);
    }
}
