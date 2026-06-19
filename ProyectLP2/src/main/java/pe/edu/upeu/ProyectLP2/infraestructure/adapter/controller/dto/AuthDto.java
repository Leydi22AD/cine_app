package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

import pe.edu.upeu.ProyectLP2.domain.model.Usuario;

public class AuthDto {
    
    // Request para login
    public record LoginRequest(
            String email,
            String password
    ) {}

    // Response después del login
    public record AuthResponse(
            String token,
            UsuarioResponse usuario
    ) {}

    // Respuesta con datos del usuario
    public record UsuarioResponse(
            Long id,
            String nombre,
            String email,
            String rol
    ) {
        public static UsuarioResponse fromDomain(Usuario usuario) {
            return new UsuarioResponse(
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getRol().name()
            );
        }
    }

    // Request para registro
    public record RegisterRequest(
            String nombre,
            String email,
            String password
    ) {}
}
