package cl.triskeledu.aduanas.proceso.client;

import cl.triskeledu.aduanas.proceso.dto.DeclaracionSagResponse;
import cl.triskeledu.aduanas.proceso.dto.TramiteSagRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign hacia ms-sag.
 * Delega el procesamiento del tramite SAG (evaluacion de riesgo,
 * persistencia de Declaracion + ItemDeclaracion y publicacion de evento Kafka)
 * al microservicio especializado ms-sag.
 */
@FeignClient(name = "ms-sag", path = "/api/v1/declaraciones")
public interface SagClient {

    @PostMapping("/tramite")
    DeclaracionSagResponse procesarTramite(@RequestBody TramiteSagRequest request);
}
