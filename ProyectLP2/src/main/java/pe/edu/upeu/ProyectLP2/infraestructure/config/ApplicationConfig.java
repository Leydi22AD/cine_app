package pe.edu.upeu.ProyectLP2.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upeu.ProyectLP2.domain.port.on.UsuarioRepositoryPort;

import java.util.Collections;
import java.util.List;

@Configuration
public class ApplicationConfig {
    
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ApplicationConfig(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepositoryPort.findByEmail(username)
                .map(usuario -> {
                    List<SimpleGrantedAuthority> authorities = 
                            Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_" + usuario.getRol())
                            );
                    return new org.springframework.security.core.userdetails.User(
                            usuario.getCorreo(),
                            usuario.getPassword(),
                            authorities
                    );
                }).orElseThrow(() -> 
                        new UsernameNotFoundException("Usuario no encontrado con el correo: " + username)
                );
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
