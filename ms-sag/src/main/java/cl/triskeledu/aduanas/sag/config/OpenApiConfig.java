package cl.triskeledu.aduanas.sag.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.providers.HateoasHalProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Optional;

@Configuration
public class OpenApiConfig {

    /**
     * Fix compatibilidad Spring Boot 3.5.0 + springdoc 2.8.8.
     * HateoasProperties.getUseHalAsDefaultJsonMediaType() fue eliminado en Spring Boot 3.5.0.
     * Proveemos un bean @Primary que sobreescribe isHalEnabled() evitando la llamada al método inexistente.
     */
    @Bean
    @Primary
    public HateoasHalProvider hateoasHalProvider(ObjectMapperProvider objectMapperProvider) {
        return new HateoasHalProvider(Optional.empty(), objectMapperProvider) {
            @Override
            public boolean isHalEnabled() {
                return false;
            }
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-SAG - Declaraciones Sanitarias y Agrícolas")
                        .version("1.0.0")
                        .description("API para gestión de declaraciones SAG. R.5: riesgo ALTO → CUARENTENA.")
                        .contact(new Contact()
                                .name("Sistema de Control Fronterizo - Aduanas")
                                .email("admin@aduanas.cl")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9000")
                                
                        .description("Via API Gateway")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el token JWT obtenido en /api/v1/auth/login")));
    }
}
