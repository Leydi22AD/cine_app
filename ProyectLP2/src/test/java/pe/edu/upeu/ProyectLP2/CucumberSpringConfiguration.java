package pe.edu.upeu.ProyectLP2;
// 💡 Paquete raíz unificado de tus pruebas
import io.cucumber.java.Before; // ⚠️ Asegúrate de que sea el Before de Cucumber, NO de JUnit
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @Before
    public void setupCucumberSpringContext() {
        // Al poner este método vacío con @Before, Cucumber se ve obligado
        // a fusionar esta clase con el "Glue" de tus Steps.
    }
}