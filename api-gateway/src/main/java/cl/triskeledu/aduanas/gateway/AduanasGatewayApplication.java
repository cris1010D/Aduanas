package cl.triskeledu.aduanas.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway centralizado del Sistema de Control Fronterizo - Aduanas.
 *
 * Puerto: 9000
 *
 * Responsabilidades:
 *   1. Punto de entrada unico para todos los clientes externos
 *   2. Enrutamiento dinamico hacia los MS via Eureka (lb://ms-xxx)
 *   3. Propagacion del header Authorization (JWT) a los MS de destino
 *   4. Logging centralizado de cada request/response con tiempo de respuesta
 *   5. CORS global para permitir acceso desde navegadores
 *
 * Orden de arranque recomendado:
 *   1. eureka (8761) → 2. api-gateway (9000) → 3. ms-auth (9001) → resto de MS
 *
 * Tecnologia: Spring Cloud Gateway (WebFlux/reactivo).
 *   - NO usa Spring MVC / spring-boot-starter-web (son incompatibles)
 *   - Los filtros implementan GlobalFilter + retornan Mono<Void>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AduanasGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AduanasGatewayApplication.class, args);
    }
}
