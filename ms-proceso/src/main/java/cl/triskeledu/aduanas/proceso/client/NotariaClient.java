package cl.triskeledu.aduanas.proceso.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Cliente Feign hacia ms-notaria.
 * Verifica que el tutor identificado por RUT tiene un poder notarial o judicial
 * vigente registrado en el sistema de notaria.
 * En caso de que ms-notaria no este disponible, MenorService maneja la excepcion
 * y simula la validacion para entorno de desarrollo.
 */
@FeignClient(name = "ms-notaria", path = "/api/v1/poderes")
public interface NotariaClient {

    @GetMapping("/tutor/{rutTutor}")
    Map<String, Object> validarPoder(@PathVariable("rutTutor") String rutTutor);
}
