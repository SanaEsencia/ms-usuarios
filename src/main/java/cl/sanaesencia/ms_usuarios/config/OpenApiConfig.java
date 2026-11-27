package cl.sanaesencia.ms_usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Documentación de los endpoints. Se ve en http://localhost:8081/swagger-ui.html
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Usuarios - Sanaesencia")
                        .version("1.0")
                        .description("Microservicio de registro, inicio de sesión y perfil de usuarios del Centro Integrativo Sanaesencia"));
    }
}
