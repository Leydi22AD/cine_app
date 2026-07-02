package pe.edu.upeu.ProyectLP2.ddb;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketSteps {

    private ResponseEntity<String> response;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8082/api/v1/tickets";
    private int statusCodeResult;

    private Long createdFuncionId;
    private Long createdAsientoId;
    private Long createdSalaId;
    private Long createdPeliculaId;
    private Long createdClienteId;

    @Dado("que el sistema de ventas se encuentra limpio y preparado")
    public void prepararEntornoVentas() {
        try {
            restTemplate.delete(baseUrl);
        } catch (HttpClientErrorException e) {
            System.err.println("Error during cleanup: " + e.getStatusCode() + " : " + e.getResponseBodyAsString());
        }
    }

    @Dado("que la función {int} está disponible y el asiento {int} se encuentra {string}")
    public void configurarAsientoYFuncion(int funcionNum, int asientoNum, String estado) {
        try {
            // 1. Create a Pelicula
            Map<String, Object> peliculaBody = new HashMap<>();
            peliculaBody.put("titulo", "Test Movie " + System.currentTimeMillis());
            peliculaBody.put("descripcion", "Description for Test Movie");
            peliculaBody.put("duracion", 120);
            peliculaBody.put("genero", "Action");
            peliculaBody.put("idioma", "English");
            peliculaBody.put("formato", "2D");
            peliculaBody.put("director", "Test Director");
            peliculaBody.put("poster", "poster.jpg");
            peliculaBody.put("trailer", "trailer.mp4");
            ResponseEntity<Map> peliculaResponse = restTemplate.postForEntity("http://localhost:8082/api/v1/peliculas", peliculaBody, Map.class);
            createdPeliculaId = extractLongFromMap(peliculaResponse.getBody(), "id", "idPelicula");

            // 2. Create a Sala
            Map<String, Object> salaBody = new HashMap<>();
            salaBody.put("numero", 100 + (int) System.currentTimeMillis() % 100); // Unique room number
            salaBody.put("filas", 5);
            salaBody.put("columnas", 10);
            ResponseEntity<Map> salaResponse = restTemplate.postForEntity("http://localhost:8082/api/v1/salas", salaBody, Map.class);
            createdSalaId = extractLongFromMap(salaResponse.getBody(), "id", "idSala");

            // 3. Create a Funcion
            Map<String, Object> funcionBody = new HashMap<>();
            funcionBody.put("peliculaId", createdPeliculaId);
            funcionBody.put("salaId", createdSalaId);
            funcionBody.put("fecha", "2030-12-31");
            funcionBody.put("horario", "19:00:00");
            funcionBody.put("precio", 10.50);
            ResponseEntity<Map> funcionResponse = restTemplate.postForEntity("http://localhost:8082/api/v1/funciones", funcionBody, Map.class);
            createdFuncionId = extractLongFromMap(funcionResponse.getBody(), "id", "idFuncion");

            // 4. Create a Usuario
            Map<String, Object> usuarioBody = new HashMap<>();
            usuarioBody.put("nombre", "Test User");
            usuarioBody.put("email", "testuser" + System.currentTimeMillis() + "@example.com");
            usuarioBody.put("password", "password");
            ResponseEntity<Map> usuarioResponse = restTemplate.postForEntity("http://localhost:8082/api/v1/auth/register", usuarioBody, Map.class);
            Map responseBody = usuarioResponse.getBody();
            if (responseBody != null && responseBody.containsKey("usuario")) {
                Map userMap = (Map) responseBody.get("usuario");
                createdClienteId = extractLongFromMap(userMap, "id", "idUsuario");
            } else {
                createdClienteId = extractLongFromMap(responseBody, "id", "idUsuario");
            }

            // 5. Fetch seats for the created Sala and update the state of a specific one
            ResponseEntity<List> asientosSalaResponse = restTemplate.getForEntity("http://localhost:8082/api/v1/asientos/sala/" + createdSalaId, List.class);
            List<Map> asientos = asientosSalaResponse.getBody();

            if (asientos != null && !asientos.isEmpty()) {
                // Find the asiento corresponding to asientoNum (assuming asientoNum is 1-based index for simplicity)
                // If asientoNum is just a placeholder, we'll use the first seat.
                Map<String, Object> asientoToUpdate = asientos.get(0); // Pick the first seat
                createdAsientoId = extractLongFromMap(asientoToUpdate, "id", "idAsiento");

                // Update the state of the asiento
                asientoToUpdate.put("estado", estado);
                // Use PUT for update
                restTemplate.put("http://localhost:8082/api/v1/asientos/" + createdAsientoId, asientoToUpdate);
            } else {
                // Fallback: if no seats are auto-generated, create one.
                Map<String, Object> asientoBody = new HashMap<>();
                asientoBody.put("id_sala", createdSalaId);
                asientoBody.put("fila", 1);
                asientoBody.put("columna", 1);
                asientoBody.put("estado", estado);
                ResponseEntity<Map> asientoResponse = restTemplate.postForEntity("http://localhost:8082/api/v1/asientos", asientoBody, Map.class);
                createdAsientoId = extractLongFromMap(asientoResponse.getBody(), "id", "idAsiento");
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error during setup: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            statusCodeResult = e.getStatusCode().value(); // Set status code to reflect setup failure
        } catch (Exception e) {
            System.err.println("Unexpected Error during setup: " + e.getMessage());
            statusCodeResult = 500; // Set status code to reflect setup failure
        }
    }

    @Cuando("el cliente con ID {int} realiza la compra del ticket con precio {double}")
    public void comprarTicketExitoso(int clienteId, double precio) {
        try {
            // Use the IDs created in the @Dado step
            if (createdFuncionId == null || createdAsientoId == null || createdClienteId == null) {
                statusCodeResult = 500; // Indicate setup failure
                return;
            }

            Map<String, Object> body = new HashMap<>();
            body.put("funcionId", createdFuncionId);
            body.put("asientoId", createdAsientoId);
            body.put("clienteId", createdClienteId);

            response = restTemplate.postForEntity(baseUrl + "/crear", body, String.class);
            statusCodeResult = response.getStatusCode().value();
        } catch (HttpClientErrorException e) {
            statusCodeResult = e.getStatusCode().value();
        } catch (Exception e) {
            statusCodeResult = 500;
        }
    }

    @Entonces("el ticket se genera correctamente y el código de estado es {int}")
    public void validarTicketGenerado(int codigo) {
        assertEquals(codigo, statusCodeResult);
    }
    @Cuando("el cliente con ID {int} intenta realizar la compra del ticket con precio {double}")
    public void intentarCompraAsientoOcupado(int clienteId, double precio) {
        try {
            // Ensure the seat is OCCUPIED for this scenario
            // The 'configurarAsientoYFuncion' should have set it to "OCUPADO"
            if (createdFuncionId == null || createdAsientoId == null || createdClienteId == null) {
                statusCodeResult = 500; // Indicate setup failure
                return;
            }

            Map<String, Object> body = new HashMap<>();
            body.put("funcionId", createdFuncionId);
            body.put("asientoId", createdAsientoId);
            body.put("clienteId", createdClienteId);

            response = restTemplate.postForEntity(baseUrl + "/crear", body, String.class);
            statusCodeResult = response.getStatusCode().value();
        } catch (HttpClientErrorException e) {
            statusCodeResult = e.getStatusCode().value();
        } catch (Exception e) {
            statusCodeResult = 500;
        }
    }

    @Entonces("el sistema debe denegar la venta respondiendo con un código de error {int}")
    public void validarErrorAsientoOcupado(int codigo) {
        assertEquals(codigo, statusCodeResult);
    }

    // --- Helpers para extracción segura de campos en responses dinámicos ---
    private Long extractLongFromMap(Map m, String... keys) {
        if (m == null) return null;
        for (String k : keys) {
            Object v = m.get(k);
            if (v instanceof Number) return ((Number) v).longValue();
            if (v instanceof String) {
                try { return Long.parseLong((String) v); } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    private Long extractSalaIdFromFuncion(Map f) {
        if (f == null) return null;
        Object s1 = f.get("salaId");
        if (s1 instanceof Number) return ((Number) s1).longValue();
        if (s1 instanceof String) {
            try { return Long.parseLong((String) s1); } catch (NumberFormatException ignored) {}
        }
        Object salaObj = f.get("sala");
        if (salaObj instanceof Map) {
            Map salaMap = (Map) salaObj;
            Long v = extractLongFromMap(salaMap, "idSala", "id");
            if (v != null) return v;
        }
        return null;
    }

    private Long extractSalaIdFromAsiento(Map a) {
        if (a == null) return null;
        Object s1 = a.get("idSala");
        if (s1 instanceof Number) return ((Number) s1).longValue();
        if (s1 instanceof String) {
            try { return Long.parseLong((String) s1); } catch (NumberFormatException ignored) {}
        }
        Object s2 = a.get("salaId");
        if (s2 instanceof Number) return ((Number) s2).longValue();
        if (s2 instanceof String) {
            try { return Long.parseLong((String) s2); } catch (NumberFormatException ignored) {}
        }
        Object salaObj = a.get("sala");
        if (salaObj instanceof Map) {
            Map salaMap = (Map) salaObj;
            Long v = extractLongFromMap(salaMap, "idSala", "id");
            if (v != null) return v;
        }
        return null;
    }

}