package cl.triskeledu.aduanas.notaria.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.providers.HateoasHalProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Optional;

@Configuration
public class SwaggerConfig {

    /**
     * Fix compatibilidad Spring Boot 3.5.0 + springdoc 2.8.8 + HATEOAS.
     * HateoasProperties.getUseHalAsDefaultJsonMediaType() fue eliminado en SB 3.5.0.
     * Constructor de 2 argumentos: (Optional.empty(), objectMapperProvider).
     * isHalEnabled() retorna false para que springdoc no intente serializar HAL.
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
        final String schemeName = "bearerAuth";
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:9000").description("Via API Gateway")))
                .info(new Info()
                        .title("MS-Notaria - Poderes Notariales")
                        .version("1.0.0")
                        .description("API para gestion de poderes notariales y documentos legales.")
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
