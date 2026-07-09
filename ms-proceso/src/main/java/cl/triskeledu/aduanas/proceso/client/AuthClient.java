package cl.triskeledu.aduanas.proceso.client;

import cl.triskeledu.aduanas.proceso.dto.OficialResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-auth", path = "/api/v1")
public interface AuthClient {

    @GetMapping("/oficiales/rut/{rut}")
    OficialResponse findByRut(@PathVariable("rut") String rut);
}
