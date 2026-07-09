package cl.triskeledu.aduanas.monitoreo.client;

import cl.triskeledu.aduanas.monitoreo.dto.HealthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Cliente Feign para consultar la salud del servidor Eureka.
 *
 * Usa url fija (no descubrimiento) porque Eureka no puede registrarse
 * a si mismo ni ser descubierto por si mismo cuando esta caido.
 */
@FeignClient(name = "eureka-server", url = "http://localhost:8761", path = "/actuator")
public interface EurekaHealthClient {

    @GetMapping("/health")
    HealthResponse health();
}
