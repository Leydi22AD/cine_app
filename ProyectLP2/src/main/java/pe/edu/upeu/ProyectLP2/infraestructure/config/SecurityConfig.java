package pe.edu.upeu.ProyectLP2.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import pe.edu.upeu.ProyectLP2.infraestructure.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(java.util.List.of("*"));
                    corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(java.util.List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos - autenticación y registro
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/usuarios/register").permitAll()

                        // 👇 NUEVAS LÍNEAS PARA LIBERAR TUS ENDPOINTS EN DESARROLLO 👇
                        // Esto permite GET, POST, PUT, DELETE en todas las rutas de salas
                        .requestMatchers("/api/v1/salas/**").permitAll()
                        .requestMatchers("/api/v1/asientos/**").permitAll()
                        .requestMatchers("/api/v1/peliculas/**").permitAll()
                        .requestMatchers("/api/v1/funciones/**").permitAll()
                        .requestMatchers("/api/v1/tickets/**").permitAll()

                        // Si quieres liberar de una vez todos tus otros controladores para probarlos en Postman,
                        // puedes descomentar o agregar las siguientes líneas:
                        // .requestMatchers("/api/v1/peliculas/**").permitAll()
                        // .requestMatchers("/api/v1/funciones/**").permitAll()
                        // .requestMatchers("/api/v1/tickets/**").permitAll()

                        // Permitir acceso a páginas de error y recursos estáticos
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()

                        // El resto de endpoints requieren autenticación
                        .anyRequest().authenticated()

                );


        return http.build();
    }
}
