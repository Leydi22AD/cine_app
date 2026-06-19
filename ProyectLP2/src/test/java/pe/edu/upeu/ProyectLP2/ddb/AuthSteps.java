package pe.edu.upeu.ProyectLP2.ddb;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto.AuthDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthSteps {

    private ResponseEntity<AuthDto.AuthResponse> response;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8082/api/v1/auth";

    @Dado("que el usuario {string} ya esta registrado con la clave {string}")
    public void registrarUsuario(String email, String password) {
        // Intentar registrar vía API; si ya existe, ignorar el conflicto
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("Test User", email, password);
        try {
            restTemplate.postForEntity(baseUrl + "/register", registerRequest, AuthDto.AuthResponse.class);
        } catch (org.springframework.web.client.HttpClientErrorException ignored) {
            // Si ya existe, no es fatal para el flujo de pruebas
        }
    }

    @Cuando("intenta iniciar sesion con el correo {string} y clave {string}")
    public void iniciarSesion(String email, String password) {
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(email, password);
        try {
            response = restTemplate.postForEntity(baseUrl + "/login", loginRequest, AuthDto.AuthResponse.class);
        } catch (HttpClientErrorException e) {
            // Captura errores 4xx si las credenciales fallan
        }
    }

    @Entonces("el sistema debe permitir el ingreso y generar un token JWT")
    public void validarToken() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().token());
    }
}