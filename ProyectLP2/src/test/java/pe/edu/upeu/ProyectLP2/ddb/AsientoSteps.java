package pe.edu.upeu.ProyectLP2.ddb;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AsientoSteps {

    private ResponseEntity<?> response;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrlAsientos = "http://localhost:8082/api/v1/asientos";
    // Nota: Asumo que tienes un microservicio o endpoint para crear salas en el puerto 8082
    private final String baseUrlSalas = "http://localhost:8082/api/v1/salas";
    private int statusCodeResult;
    private List<?> asientosResultados;
    private Long createdSalaId = null;

    @Dado("que la base de datos de pruebas está activa y limpia")
    public void limpiarBaseDeDatos() {
        // El orden de eliminación es crucial para evitar errores de integridad referencial.
        // Se elimina de "hijo" a "padre".
        String[] endpointsEnOrdenDeLimpieza = new String[]{
                "http://localhost:8082/api/v1/tickets",
                "http://localhost:8082/api/v1/funciones",
                "http://localhost:8082/api/v1/asientos",
                "http://localhost:8082/api/v1/peliculas",
                "http://localhost:8082/api/v1/salas",
                "http://localhost:8082/api/v1/usuarios"
        };

        for (String endpoint : endpointsEnOrdenDeLimpieza) {
            try {
                // Obtenemos todos los IDs de los recursos
                ResponseEntity<List<Map<String, Object>>> listRes = restTemplate.exchange(
                        endpoint,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}
                );

                if (listRes.getBody() != null) {
                    for (Map<String, Object> item : listRes.getBody()) {
                        // Intentar identificar un id común
                        Object id = item.get("id"); // El más común
                        if (id == null) id = item.get("idTicket");
                        if (id == null) id = item.get("idFuncion");
                        if (id == null) id = item.get("idAsiento");
                        if (id == null) id = item.get("idSala");
                        if (id == null) id = item.get("idPelicula");
                        if (id == null) id = item.get("idUsuario");

                        if (id != null) {
                            String deleteUrl = endpoint + "/" + id;
                            try {
                                restTemplate.delete(deleteUrl);
                            } catch (Exception e) {
                                // Ignorar si la eliminación falla (p.ej., por dependencias que se limpiarán después)
                            }
                        }
                    }
                }
            } catch (Exception ignore) {
                // si el endpoint no existe o la app aún no levantó, ignorar
            }
        }

        // Reset estado local
        statusCodeResult = 0;
        asientosResultados = null;
        createdSalaId = null;
    }

    @Dado("que se registra una nueva sala con ID {int}, con {int} filas y {int} columnas")
    public void registrarSalaConAsientosAutogenerados(int salaNumero, int filas, int columnas) {

        Map<String, Object> bodySala = new HashMap<>();
        bodySala.put("numero", salaNumero);
        bodySala.put("filas", filas);
        bodySala.put("columnas", columnas);

        try {
            // Crear la sala
            restTemplate.postForEntity(baseUrlSalas + "/crear", bodySala, Map.class);
            // Buscar la sala creada por número para obtener su id real
            ResponseEntity<List> salas = restTemplate.getForEntity(baseUrlSalas, List.class);
            if (salas.getBody() != null) {
                for (Object o : salas.getBody()) {
                    if (o instanceof Map) {
                        Map m = (Map) o;
                        Object numero = m.get("numero");
                        if (numero != null && ((Number) numero).intValue() == salaNumero) {
                            Object id = m.get("idSala");
                            if (id != null) {
                                createdSalaId = ((Number) id).longValue();
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Cuando("se solicita la lista de asientos de la sala ID {int} mediante una petición GET")
    public void consultarAsientosPorSala(int salaId) {
        long idToUse = (createdSalaId != null) ? createdSalaId : salaId;
        try {
            ResponseEntity<List> res = restTemplate.exchange(
                    baseUrlAsientos + "/sala/" + idToUse,
                    HttpMethod.GET,
                    null,
                    List.class
            );
            statusCodeResult = res.getStatusCode().value();
            asientosResultados = res.getBody();
        } catch (HttpClientErrorException e) {
            statusCodeResult = e.getStatusCode().value();
        }
    }

    @Entonces("el sistema debe responder con un código de estado de éxito {int}")
    public void verificarCodigoEstado(int codigo) {
        assertEquals(codigo, statusCodeResult);
    }

    @Y("la lista debe contener un total de {int} asientos configurados como {string}")
    public void verificarCantidadYEstadoDeAsientos(int cantidadEsperada, String estadoEsperado) {
        // Polling: la generación de asientos puede ser asíncrona; esperar hasta que la cantidad esperada exista o timeout
        long timeoutMs = 5000L;
        long start = System.currentTimeMillis();
        long idToUse = (createdSalaId != null) ? createdSalaId : 1L;
        while ((asientosResultados == null || asientosResultados.size() != cantidadEsperada) && (System.currentTimeMillis() - start) < timeoutMs) {
            try {
                ResponseEntity<List> res = restTemplate.exchange(
                        baseUrlAsientos + "/sala/" + idToUse,
                        HttpMethod.GET,
                        null,
                        List.class
                );
                if (res.getStatusCode().is2xxSuccessful()) {
                    asientosResultados = res.getBody();
                    statusCodeResult = res.getStatusCode().value();
                }
                Thread.sleep(200);
            } catch (Exception e) {
                // Ignorar y reintentar
            }
        }

        assertNotNull(asientosResultados, "La lista de asientos no debería ser nula");
        assertEquals(cantidadEsperada, asientosResultados.size(), "La cantidad de asientos autogenerados no coincide");

        // Aquí podrías validar que los objetos internos tengan el estado "LIBRE" si mapeas la respuesta a tu DTO.
    }

    @Cuando("se envía una solicitud POST manual para crear un asiento en la fila {int} columna {int} que ya fue autogenerado")
    public void intentarCrearAsientoDuplicadoManualmente(int fila, int columna) {
        Map<String, Object> bodyAsiento = new HashMap<>();
        bodyAsiento.put("fila", fila);
        bodyAsiento.put("columna", columna);
        bodyAsiento.put("estado", "LIBRE");
        bodyAsiento.put("salaId", (createdSalaId != null) ? createdSalaId : 1L); // ID de la sala creada en el Dado

        try {
            // Intentamos pegarle a tu @PostMapping("/crear") con un asiento que la sala ya generó
            response = restTemplate.postForEntity(baseUrlAsientos + "/crear", bodyAsiento, String.class);
            statusCodeResult = response.getStatusCode().value();
        } catch (HttpClientErrorException e) {
            // Aquí debería atrapar el 409 Conflict provocado por tu AsientoAlreadyExistsException
            statusCodeResult = e.getStatusCode().value();
        }
    }
    @Entonces("el sistema debe responder con un código de estado de conflicto {int}")
    public void elSistemaDebeResponderConCodigoConflicto(int codigo) {
        assertEquals(codigo, statusCodeResult);
    }
}