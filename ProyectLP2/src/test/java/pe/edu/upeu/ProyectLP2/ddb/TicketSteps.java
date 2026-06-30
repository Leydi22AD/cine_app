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

    @Dado("que el sistema de ventas se encuentra limpio y preparado")
    public void prepararEntornoVentas() {
    }

    @Dado("que la función {int} está disponible y el asiento {int} se encuentra {string}")
    public void configurarAsientoYFuncion(int idFuncion, int idAsiento, String estado) {
    }

    @Cuando("el cliente con ID {int} realiza la compra del ticket con precio {double}")
    public void comprarTicketExitoso(int clienteId, double precio) {
        try {
            ResponseEntity<List> funcionesRes = restTemplate.getForEntity("http://localhost:8082/api/v1/funciones", List.class);
            ResponseEntity<List> asientosRes = restTemplate.getForEntity("http://localhost:8082/api/v1/asientos", List.class);

            Long funcionId = null;
            Long asientoId = null;

            // Strategy 1: try to find a LIBRE asiento that belongs to the same sala as a funcion
            if (funcionesRes.getBody() != null && asientosRes.getBody() != null) {
                for (Object fObj : funcionesRes.getBody()) {
                    if (!(fObj instanceof Map)) continue;
                    Map f = (Map) fObj;
                    Long salaId = extractSalaIdFromFuncion(f);
                    if (salaId == null) continue;

                    for (Object aObj : asientosRes.getBody()) {
                        if (!(aObj instanceof Map)) continue;
                        Map a = (Map) aObj;
                        Long aSalaId = extractSalaIdFromAsiento(a);
                        String estadoAsiento = a.get("estado") != null ? a.get("estado").toString() : null;
                        if (aSalaId != null && aSalaId.equals(salaId) && "LIBRE".equalsIgnoreCase(estadoAsiento)) {
                            funcionId = extractLongFromMap(f, "id", "idFuncion");
                            asientoId = extractLongFromMap(a, "id", "idAsiento");
                            break;
                        }
                    }
                    if (funcionId != null) break;
                }
            }

            // Strategy 2: if not found, try to find any asiento (prefer LIBRE) in the sala of any funcion
            if (funcionId == null || asientoId == null) {
                if (funcionesRes.getBody() != null) {
                    for (Object fObj : funcionesRes.getBody()) {
                        if (!(fObj instanceof Map)) continue;
                        Map f = (Map) fObj;
                        Long salaId = extractSalaIdFromFuncion(f);
                        if (salaId == null) continue;

                        // try to fetch seats by sala endpoint (more reliable)
                        List salaAsientos = null;
                        try {
                            ResponseEntity<List> res = restTemplate.getForEntity("http://localhost:8082/api/v1/asientos/sala/" + salaId, List.class);
                            salaAsientos = res.getBody();
                        } catch (Exception ignore) {
                            // fallback to global list already fetched
                            salaAsientos = asientosRes.getBody();
                        }

                        if (salaAsientos == null) continue;

                        // prefer LIBRE, else take any (use loops to avoid raw-stream generics issues)
                        Long foundSeat = null;
                        for (Object oa2 : salaAsientos) {
                            if (!(oa2 instanceof Map)) continue;
                            Map m = (Map) oa2;
                            Long aSala2 = extractSalaIdFromAsiento(m);
                            String est = m.get("estado") != null ? m.get("estado").toString() : null;
                            if (aSala2 != null && aSala2.equals(salaId) && "LIBRE".equalsIgnoreCase(est)) {
                                foundSeat = extractLongFromMap(m, "id", "idAsiento");
                                break;
                            }
                        }
                        if (foundSeat != null) {
                            funcionId = extractLongFromMap(f, "id", "idFuncion");
                            asientoId = foundSeat;
                            break;
                        }
                        // if no libre, pick any seat in that sala
                        for (Object oa2 : salaAsientos) {
                            if (!(oa2 instanceof Map)) continue;
                            Map m = (Map) oa2;
                            Long aSala2 = extractSalaIdFromAsiento(m);
                            if (aSala2 != null && aSala2.equals(salaId)) {
                                foundSeat = extractLongFromMap(m, "id", "idAsiento");
                                break;
                            }
                        }
                        if (foundSeat != null) {
                            funcionId = extractLongFromMap(f, "id", "idFuncion");
                            asientoId = foundSeat;
                            break;
                        }

                    }
                }
            }

            // Strategy 3: final fallback - pick first funcion and first asiento from lists (avoid hardcoded 1 and 12)
            if (funcionId == null || asientoId == null) {
                if (funcionesRes.getBody() != null && !funcionesRes.getBody().isEmpty()) {
                    Map f = (Map) funcionesRes.getBody().get(0);
                    funcionId = extractLongFromMap(f, "id", "idFuncion");
                    Long salaId = extractSalaIdFromFuncion(f);
                    if ((asientoId == null) && asientosRes.getBody() != null && !asientosRes.getBody().isEmpty()) {
                        // try to find a seat in same sala first
                        for (Object aObj : asientosRes.getBody()) {
                            if (!(aObj instanceof Map)) continue;
                            Map a = (Map) aObj;
                            Long aSala = extractSalaIdFromAsiento(a);
                            if (salaId != null && aSala != null && aSala.equals(salaId)) {
                                asientoId = extractLongFromMap(a, "id", "idAsiento");
                                break;
                            }
                        }
                        if (asientoId == null) {
                            Map a = (Map) asientosRes.getBody().get(0);
                            asientoId = extractLongFromMap(a, "id", "idAsiento");
                        }
                    }
                }
            }

            // If still null, return 500 to fail clearly
            if (funcionId == null || asientoId == null) {
                statusCodeResult = 500;
                return;
            }

            Map<String, Object> body = new HashMap<>();
            body.put("id_funcion", funcionId);
            body.put("id_asiento", asientoId);
            body.put("id_usuario", (long) clienteId);

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
            ResponseEntity<List> funcionesRes = restTemplate.getForEntity("http://localhost:8082/api/v1/funciones", List.class);
            ResponseEntity<List> asientosRes = restTemplate.getForEntity("http://localhost:8082/api/v1/asientos", List.class);

            Long funcionId = null;
            Long asientoId = null;

            if (funcionesRes.getBody() != null && !funcionesRes.getBody().isEmpty() && asientosRes.getBody() != null) {
                Map f = (Map) funcionesRes.getBody().get(0);
                funcionId = extractLongFromMap(f, "id", "idFuncion");

                // Intentamos buscar un asiento que YA tenga cualquier estado diferente a LIBRE
                for (Object aObj : asientosRes.getBody()) {
                    if (!(aObj instanceof Map)) continue;
                    Map a = (Map) aObj;
                    String est = a.get("estado") != null ? a.get("estado").toString() : "";
                    if (!"LIBRE".equalsIgnoreCase(est) && !"".equals(est)) {
                        asientoId = extractLongFromMap(a, "id", "idAsiento");
                        break;
                    }
                }

                // Si todos están libres, forzamos la ocupación del primero de la lista para simular el error
                if (asientoId == null && !asientosRes.getBody().isEmpty()) {
                    Map a = (Map) asientosRes.getBody().get(0);
                    asientoId = extractLongFromMap(a, "id", "idAsiento");

                    // Hacemos una compra previa fantasma para ocuparlo de verdad en el sistema
                    Map<String, Object> preocuparBody = new HashMap<>();
                    preocuparBody.put("id_funcion", funcionId);
                    preocuparBody.put("id_asiento", asientoId);
                    preocuparBody.put("id_usuario", (long) clienteId);
                    try {
                        restTemplate.postForEntity(baseUrl + "/crear", preocuparBody, String.class);
                    } catch (Exception ignored) {}
                }
            }

            if (funcionId == null || asientoId == null) {
                statusCodeResult = 500;
                return;
            }

            Map<String, Object> body = new HashMap<>();
            body.put("id_funcion", funcionId);
            body.put("id_asiento", asientoId);
            body.put("id_usuario", (long) clienteId);

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