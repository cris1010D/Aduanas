package cl.triskeledu.aduanas.proceso.client;

import cl.triskeledu.aduanas.proceso.dto.AntecedenteConsultadoResponse;
import cl.triskeledu.aduanas.proceso.dto.ConsultaPdiRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign hacia ms-pdi.
 *
 * Invoca el endpoint R.6 de ms-pdi para registrar la consulta de antecedentes
 * de un viajero y obtener su resultado (SIN_REGISTROS | CON_REGISTROS).
 *
 * El timeout de conexion y lectura se configura en application.yml bajo
 * feign.client.config.ms-pdi para cumplir el requisito ISO 25010 de
 * respuesta total <= 2000 ms.
 */
@FeignClient(name = "ms-pdi", path = "/api/v1/antecedentes")
public interface PdiClient {

    @PostMapping("/consultar")
    AntecedenteConsultadoResponse consultarAntecedente(@RequestBody ConsultaPdiRequest request);
}
