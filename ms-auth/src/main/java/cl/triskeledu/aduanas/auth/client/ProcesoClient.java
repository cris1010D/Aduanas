package cl.triskeledu.aduanas.auth.client;

import cl.triskeledu.aduanas.auth.dto.ViajeroResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-proceso", path = "/api/v1")
public interface ProcesoClient {

    @GetMapping("/viajeros/rut/{rut}")
    ViajeroResponse findByRut(@PathVariable("rut") String rut);
}
