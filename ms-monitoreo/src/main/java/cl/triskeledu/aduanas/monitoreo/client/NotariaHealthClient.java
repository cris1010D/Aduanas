package cl.triskeledu.aduanas.monitoreo.client;

import cl.triskeledu.aduanas.monitoreo.dto.HealthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ms-notaria", path = "/actuator")
public interface NotariaHealthClient {

    @GetMapping("/health")
    HealthResponse health();
}
