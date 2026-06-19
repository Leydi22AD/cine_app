package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.ProyectLP2.domain.model.Usuario;
import pe.edu.upeu.ProyectLP2.domain.port.on.UsuarioRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.AuthDto;
import pe.edu.upeu.ProyectLP2.infraestructure.security.JwtService;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.Rol;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioRepositoryPort usuarioRepositoryPort,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponse> login(@RequestBody AuthDto.LoginRequest request) {
        try {
            System.out.println("[LOGIN] Intentando login con email: " + request.email());
            
            // 1. Autenticar al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            System.out.println("[LOGIN] Autenticación exitosa para: " + request.email());

            // 2. Obtener datos completos del usuario
            Usuario usuario = usuarioRepositoryPort.findByEmail(request.email())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            System.out.println("[LOGIN] Usuario encontrado - ID: " + usuario.getIdUsuario() + ", Rol: " + usuario.getRol());

            // 3. Generar token JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
            String token = jwtService.generateToken(userDetails);

            // 4. Crear respuesta con el token y datos del usuario
            AuthDto.UsuarioResponse usuarioResponse = AuthDto.UsuarioResponse.fromDomain(usuario);
            AuthDto.AuthResponse authResponse = new AuthDto.AuthResponse(token, usuarioResponse);

            System.out.println("[LOGIN] Token generado exitosamente");

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            System.err.println("[LOGIN ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDto.AuthResponse> register(@RequestBody AuthDto.RegisterRequest request) {
        try {
            System.out.println("[REGISTRO] Intentando registrar: " + request.email());
            
            // Verificar si el usuario ya existe
            Optional<Usuario> usuarioExistente = usuarioRepositoryPort.findByEmail(request.email());
            if (usuarioExistente.isPresent()) {
                System.out.println("[REGISTRO] Email ya existe: " + request.email());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            System.out.println("[REGISTRO] Email disponible, creando usuario...");
            
            // Crear nuevo usuario - SIEMPRE con rol CLIENTE
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(request.nombre());
            nuevoUsuario.setEmail(request.email());
            nuevoUsuario.setPassword(passwordEncoder.encode(request.password()));
            nuevoUsuario.setRol(Rol.CLIENTE); // SIEMPRE CLIENTE al registrarse
            
            // Guardar usuario
            Usuario usuarioGuardado = usuarioRepositoryPort.save(nuevoUsuario);
            System.out.println("[REGISTRO] Usuario guardado con ID: " + usuarioGuardado.getIdUsuario());

            // Generar token
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
            String token = jwtService.generateToken(userDetails);

            // Crear respuesta
            AuthDto.UsuarioResponse usuarioResponse = AuthDto.UsuarioResponse.fromDomain(usuarioGuardado);
            AuthDto.AuthResponse authResponse = new AuthDto.AuthResponse(token, usuarioResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (Exception e) {
            System.err.println("[REGISTRO ERROR] " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<AuthDto.AuthResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // 1. Validar header de autorización
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            final String jwt = authHeader.substring(7);

            // 2. Extraer el email del token
            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null) {
                // 3. Cargar UserDetails
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // 4. Validar el token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 5. Obtener usuario completo
                    Usuario usuario = usuarioRepositoryPort.findByEmail(userEmail)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    // 6. Generar nuevo token
                    String newToken = jwtService.generateToken(userDetails);

                    // 7. Crear respuesta
                    AuthDto.UsuarioResponse usuarioResponse = AuthDto.UsuarioResponse.fromDomain(usuario);
                    AuthDto.AuthResponse authResponse = new AuthDto.AuthResponse(newToken, usuarioResponse);

                    return ResponseEntity.ok(authResponse);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
