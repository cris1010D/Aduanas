package cl.triskeledu.aduanas.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro de propagacion del header JWT hacia los microservicios de destino.
 *
 * Spring Cloud Gateway propaga todos los headers del request de origen por defecto.
 * Este filtro hace explicita y auditable esa propagacion:
 *
 *   1. Detecta si la peticion trae el header Authorization (Bearer <token>)
 *   2. Lo loguea a nivel DEBUG (nunca imprime el token completo en produccion)
 *   3. Agrega headers de trazabilidad del gateway al request mutado:
 *      - X-Gateway-Source  : identifica que paso por este gateway
 *      - X-Forwarded-By    : nombre de la instancia del gateway
 *      - X-Request-ID      : ID unico del request para correlacion de logs
 *
 * Diseño:
 *   - La VALIDACION del JWT la hace cada MS en su propio SecurityConfig.
 *   - El gateway NO valida el token — solo lo transporta. Esto permite que
 *     ms-auth pueda rechazar tokens expirados con un 401 limpio, mientras
 *     otros MS de solo lectura (ej: ms-monitoreo) pueden ser mas permisivos.
 *
 * Orden: -2 (corre antes que GlobalLoggingFilter en -1).
 */
@Slf4j
@Component
public class JwtHeaderRelayFilter implements GlobalFilter, Ordered {

    private static final int    ORDER              = -2;
    private static final String GATEWAY_SOURCE     = "X-Gateway-Source";
    private static final String FORWARDED_BY       = "X-Forwarded-By";
    private static final String REQUEST_ID_HEADER  = "X-Request-ID";
    private static final String GATEWAY_NAME       = "aduanas-api-gateway";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authHeader = exchange.getRequest()
            .getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Loguear solo los primeros 20 chars del token para diagnostico
            String tokenPreview = authHeader.length() > 27
                ? authHeader.substring(0, 27) + "..."
                : authHeader;
            log.debug("[JWT-RELAY] Authorization presente: {} — propagando a MS destino",
                tokenPreview);
        } else {
            log.debug("[JWT-RELAY] Sin Authorization header — request anonimo o ruta publica");
        }

        // Mutar el request para agregar headers de trazabilidad del gateway
        ServerHttpRequest mutatedRequest = exchange.getRequest()
            .mutate()
            .header(GATEWAY_SOURCE,    GATEWAY_NAME)
            .header(FORWARDED_BY,      GATEWAY_NAME)
            .header(REQUEST_ID_HEADER, exchange.getRequest().getId())
            .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
