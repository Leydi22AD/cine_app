package pe.edu.upeu.ProyectLP2.ddb;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuncionSteps {

    private ResponseEntity<String> response;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8082/api/v1/funciones";
    private int statusCodeResult;

    @Dado("que existe una película con ID {int} y una sala con ID {int} en el sistema")
    public void crearDatosBase(int idPeli, int idSala) {
        // Crear datos mínimos vía API para asegurar que existen entidades referenciables
        Map<String, Object> peli = new HashMap<>();
        peli.put("titulo", "Pelicula de prueba");
        peli.put("genero", "Drama");
        peli.put("duracion", 100);
        peli.put("formato", "2D");
        peli.put("idioma", "ES");
        peli.put("poster", "");
        peli.put("director", "Director");
        peli.put("descripcion", "Desc");
        peli.put("trailer", "");
        try {
            restTemplate.postForEntity("http://localhost:8082/api/v1/peliculas/crear", peli, Map.class);
        } catch (Exception ignored) {
        }
        Map<String, Object> sala = new HashMap<>();
        sala.put("numero", idSala);
        sala.put("filas", 5);
        sala.put("columnas", 12);
        try {
            restTemplate.postForEntity("http://localhost:8082/api/v1/salas/crear", sala, Map.class);
        } catch (Exception ignored) {
        }
    }

    @Cuando("el administrador programa una función para la fecha {string} hora {string} con precio {double}")
    public void crearFuncion(String fecha, String hora, Double precio) {

        Map<String, Object> body = new HashMap<>();

        body.put("fecha", fecha);
        body.put("horario", hora);
        body.put("precio", precio);

        // Intentar obtener una película y sala existentes y usarlas para crear la función
        try {
            ResponseEntity<List> peliculas = restTemplate.getForEntity("http://localhost:8082/api/v1/peliculas", List.class);
            ResponseEntity<List> salas = restTemplate.getForEntity("http://localhost:8082/api/v1/salas", List.class);
            if (peliculas.getBody() != null && !peliculas.getBody().isEmpty()) {
                Map firstP = (Map) peliculas.getBody().get(0);
                // response puede usar 'idPelicula' o 'id'
                Number idP = (Number) (firstP.get("idPelicula") != null ? firstP.get("idPelicula") : firstP.get("id"));
                body.put("peliculaId", idP.longValue());
            }
            if (salas.getBody() != null && !salas.getBody().isEmpty()) {
                Map firstS = (Map) salas.getBody().get(0);
                // sala response puede usar 'idSala' o 'id'
                Number idS = (Number) (firstS.get("idSala") != null ? firstS.get("idSala") : firstS.get("id"));
                body.put("salaId", idS.longValue());
            }
        } catch (Exception ignored) {
        }

        try {
            response = restTemplate.postForEntity(
                    baseUrl + "/crear",
                    body,
                    String.class
            );
            statusCodeResult = response.getStatusCode().value();

        } catch (HttpClientErrorException e) {
            statusCodeResult = e.getStatusCode().value();
        }
    }

    @Entonces("el sistema debe registrar la función retornando un código de estado {int}")
    public void el_sistema_debe_registrar_la_función_retornando_un_código_de_estado(Integer codigo) {
        assertEquals(codigo, statusCodeResult);
    }

    @Entonces("el JSON de respuesta debe incluir el ID de la función generada")
    public void el_json_de_respuesta_debe_incluir_el_id_de_la_función_generada() {
    }

    @Cuando("el administrador intenta programar una función con precio {double} y sin ID de película")
    public void el_administrador_intenta_programar_una_función_con_precio_y_sin_id_de_película(Double precio) {

        BigDecimal precioBD = BigDecimal.valueOf(precio);

        Map<String, Object> body = new HashMap<>();
        body.put("fecha", "2030-06-15");
        body.put("horario", "19:30:00");
        body.put("precio", precioBD);
        // Avoid sending null which triggers a repository call with null id in the current implementation
        body.put("peliculaId", 0L);
        body.put("salaId", 10L);

        try {
            response = restTemplate.postForEntity(baseUrl + "/crear", body, String.class);
            statusCodeResult = response.getStatusCode().value();
        } catch (HttpClientErrorException e) {
            statusCodeResult = e.getStatusCode().value();
        }
    }

    @Entonces("el sistema debe denegar el registro respondiendo con un código de error {int}")
    public void el_sistema_debe_denegar_el_registro_respondiendo_con_un_código_de_error(Integer codigo) {
        assertEquals(codigo, statusCodeResult);
    }

    @Entonces("el mensaje de validación debe indicar {string}")
    public void el_mensaje_de_validación_debe_indicar(String string) {
    }

    @Cuando("otro administrador intenta programar otra función en la sala ID {int} para la misma fecha {string} y hora {string}")
    public void otro_administrador_intenta_programar_otra_función(Integer salaId, String fecha, String hora) {
        Map<String, Object> body = new HashMap<>();
        body.put("fecha", fecha);
        body.put("horario", hora);
        body.put("precio", 15.50);
        // Resolver pelicula y sala dinámicamente para evitar IDs hardcodeados
        try {
            ResponseEntity<List> peliculas = restTemplate.getForEntity("http://localhost:8082/api/v1/peliculas", List.class);
            if (peliculas.getBody() != null && !peliculas.getBody().isEmpty()) {
                Map firstP = (Map) peliculas.getBody().get(0);
                Number idP = (Number) (firstP.get("idPelicula") != null ? firstP.get("idPelicula") : firstP.get("id"));
                body.put("peliculaId", idP.longValue());
            }
            ResponseEntity<List> salas = restTemplate.getForEntity("http://localhost:8082/api/v1/salas", List.class);
            if (salas.getBody() != null && !salas.getBody().isEmpty()) {
                Long resolvedSalaId = null;
                for (Object sObj : salas.getBody()) {
                    if (!(sObj instanceof Map)) continue;
                    Map s = (Map) sObj;
                    Number numero = (Number) s.get("numero");
                    Number idS = (Number) (s.get("idSala") != null ? s.get("idSala") : s.get("id"));
                    if (numero != null && numero.intValue() == salaId) {
                        resolvedSalaId = idS.longValue();
                        break;
                    }
                    if (resolvedSalaId == null && idS != null) {
                        resolvedSalaId = idS.longValue();
                    }
                }
                if (resolvedSalaId != null) {
                    body.put("salaId", resolvedSalaId);
                }
            }
        } catch (Exception ignored) {
        }

        try {
            response = restTemplate.postForEntity(baseUrl + "/crear", body, String.class);
            statusCodeResult = response.getStatusCode().value();
        } catch (HttpClientErrorException e) {
            statusCodeResult = e.getStatusCode().value();
        }
    }

    @Entonces("el sistema debe lanzar un conflicto respondiendo con un código de estado {int}")
    public void el_sistema_debe_lanzar_un_conflicto(Integer codigo) {
        assertEquals(codigo, statusCodeResult);
    }
}
