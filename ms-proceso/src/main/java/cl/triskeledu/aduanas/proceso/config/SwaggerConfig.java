package cl.triskeledu.aduanas.proceso.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String schemeName = "bearerAuth";

        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:9000").description("Via API Gateway")
                ))
                .info(new Info()
                        .title("MS-Proceso - Viajeros, Vehiculos, SAG y PDI")
                        .version("1.0.0")
                        .description("API principal de procesamiento fronterizo: viajeros, vehiculos, consultas SAG y PDI.")
                        .contact(new Contact()
                                .name("Sistema de Control Fronterizo - Aduanas")
                                .email("admin@aduanas.cl")))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el token JWT obtenido en /api/v1/auth/login")));
    }
}
