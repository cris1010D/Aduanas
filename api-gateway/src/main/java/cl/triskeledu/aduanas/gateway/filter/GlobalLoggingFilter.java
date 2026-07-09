package cl.triskeledu.aduanas.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Filtro global de trazabilidad del API Gateway.
 *
 * Registra en consola:
 *   ENTRADA  → metodo HTTP + URI completa + ID de solicitud
 *   SALIDA   ← metodo HTTP + URI + codigo de respuesta + tiempo de respuesta en ms
 *
 * El tiempo de respuesta incluye el tiempo total de red + procesamiento del MS.
 *
 * Orden: -1 (ejecuta antes que los filtros de Spring Cloud Gateway,
 * despues de JwtHeaderRelayFilter que corre en -2).
 *
 * Ejemplo de salida en consola:
 *   [GATEWAY →] POST http://localhost:9000/api/v1/auth/login  [req:abc123]
 *   [GATEWAY ←] POST /api/v1/auth/login | 200 | 87ms         [req:abc123]
 */
@Slf4j
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    private static final int ORDER = -1;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest  request = exchange.getRequest();
        HttpMethod         method  = request.getMethod();
        URI                uri     = request.getURI();
        String             reqId   = request.getId();
        long               start   = System.currentTimeMillis();

        log.info("[GATEWAY ->] {} {} [req:{}]", method, uri, reqId);

        return chain.filter(exchange)
            .then(Mono.fromRunnable(() -> logResponse(exchange, method, uri, reqId, start)));
    }
    @SuppressWarnings("null")
    private void logResponse(ServerWebExchange exchange,
                             HttpMethod method,
                             URI uri,
                             String reqId,
                             long start) {
        ServerHttpResponse response = exchange.getResponse();
        int statusCode = response.getStatusCode() != null
            ? response.getStatusCode().value()
            : 0;
        long elapsed = System.currentTimeMillis() - start;
        String path   = uri.getPath();

        log.info("[GATEWAY <-] {} {} | {} | {}ms [req:{}]",
            method, path, statusCode, elapsed, reqId);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
