package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.domain.port.in.UsuarioUseCase;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.UsuarioDto;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.Rol;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    // Dependencia hacia la capa de casos de uso (lógica de negocio).
    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    // 🔹 Crear usuario
    @PostMapping("/crear")
    public ResponseEntity<UsuarioDto.UsuarioResponse> crearUsuario(@RequestBody UsuarioDto.UsuarioRequest request) {

        // Convertimos el DTO de request a objeto de dominio Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());

        // Si el rol viene vacío, por defecto se asigna ADMIN
        usuario.setRol(request.rol() != null ? request.rol() : Rol.CLIENTE);

        // Llamamos al caso de uso para registrar al usuario
        Usuario creado = usuarioUseCase.registrarUsuario(usuario);

        // Convertimos el objeto de dominio a DTO de respuesta
        UsuarioDto.UsuarioResponse response = mapToUsuarioResponse(creado);

        // Retornamos con código 201 (CREATED)
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //  Listar todos
    @GetMapping
    public ResponseEntity<List<UsuarioDto.UsuarioResponse>> listarUsuarios() {

        // Obtenemos los usuarios desde el caso de uso
        List<UsuarioDto.UsuarioResponse> usuarios = usuarioUseCase.listarUsuarios()
                .stream()
                // Convertimos cada Usuario a DTO Response
                .map(this::mapToUsuarioResponse)
                .collect(Collectors.toList());
        // Retornamos la lista con código 200 (OK)
        return ResponseEntity.ok(usuarios);
    }

    // 🔹 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto.UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id) {

        // Buscamos en la capa de negocio y convertimos a Response
        return usuarioUseCase.obtenerUsuarioPorId(id)
                .map(usuario -> ResponseEntity.ok(mapToUsuarioResponse(usuario)))

                // Si no existe, retornamos 404 (Not Found)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto.UsuarioResponse> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDto.UsuarioRequest request) {

        // Convertimos el request en un objeto de dominio
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());
        usuario.setRol(request.rol() != null ? request.rol() : Rol.ADMIN);

        // Llamamos al caso de uso para actualizarlo
        return usuarioUseCase.actualizarUsuario(id, usuario)
                .map(u -> ResponseEntity.ok(mapToUsuarioResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioUseCase.eliminarUsuario(id)) {
            // Si se eliminó, devolvemos 204 (No Content)
            return ResponseEntity.noContent().build();
        } else {
            // Si no existe, devolvemos 404
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDto.UsuarioResponse> obtenerUsuarioPorEmail(@PathVariable String email) {
        return usuarioUseCase.obtenerUsuarioPorEmail(email)

                // Si existe, devolvemos 200 con el usuario
                .map(usuario -> ResponseEntity.ok(mapToUsuarioResponse(usuario)))

                // Si no existe, devolvemos 404
                .orElse(ResponseEntity.notFound().build());
    }


    // 🔹 Método privado de utilidad que convierte un objeto de dominio Usuario en un DTO Response
    private UsuarioDto.UsuarioResponse mapToUsuarioResponse(Usuario usuario) {
        return new UsuarioDto.UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );

    }



}
