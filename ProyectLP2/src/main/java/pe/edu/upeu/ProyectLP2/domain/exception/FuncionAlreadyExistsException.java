package pe.edu.upeu.ProyectLP2.domain.exception;

public class FuncionAlreadyExistsException extends RuntimeException {
    public FuncionAlreadyExistsException(String message) {
        super(message);
    }
}
