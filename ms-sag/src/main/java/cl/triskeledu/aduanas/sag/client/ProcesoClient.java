package cl.triskeledu.aduanas.sag.client;

import cl.triskeledu.aduanas.sag.dto.ViajeroResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-proceso", path = "/api/v1")
public interface ProcesoClient {

    @GetMapping("/viajeros/rut/{rut}")
    ViajeroResponse findViajeroByRut(@PathVariable("rut") String rut);
}
