package cl.triskeledu.aduanas.proceso.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Cliente Feign hacia ms-menores.
 * Consulta si el menor identificado por RUT existe y esta activo en el sistema.
 * En caso de que ms-menores no este disponible, MenorService maneja la excepcion
 * y simula la validacion para entorno de desarrollo.
 */
@FeignClient(name = "ms-menores", path = "/api/v1/menores")
public interface MenoresClient {

    @GetMapping("/rut/{rut}")
    Map<String, Object> validarMenor(@PathVariable("rut") String rut);
}
