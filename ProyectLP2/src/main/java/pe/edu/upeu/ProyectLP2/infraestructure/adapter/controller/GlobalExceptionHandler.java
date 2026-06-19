package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pe.edu.upeu.ProyectLP2.domain.exception.AsientoAlreadyExistsException;
import pe.edu.upeu.ProyectLP2.domain.exception.FuncionAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AsientoAlreadyExistsException.class)
    public ResponseEntity<String> handleAsientoExists(AsientoAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(FuncionAlreadyExistsException.class)
    public ResponseEntity<String> handleFuncionExists(FuncionAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        if (msg.contains("no encontrada") || msg.contains("no existe") || msg.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
