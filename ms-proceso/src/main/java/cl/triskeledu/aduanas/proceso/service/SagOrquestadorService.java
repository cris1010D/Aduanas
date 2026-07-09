package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.client.SagClient;
import cl.triskeledu.aduanas.proceso.dto.DeclaracionSagResponse;
import cl.triskeledu.aduanas.proceso.dto.TramiteSagRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * R.5 — Orquestador del tramite SAG en ms-proceso.
 *
 * Responsabilidad: recibe la solicitud del cliente externo, delega a ms-sag
 * via Feign la aplicacion de la regla de negocio (ALTO → CUARENTENA),
 * la persistencia y la publicacion del evento Kafka.
 *
 * ms-proceso actua aqui como puerta de entrada al ecosistema SAG,
 * asegurando que la solicitud este autenticada (JWT) antes de
 * llegar a ms-sag.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SagOrquestadorService {

    private final SagClient sagClient;

    public DeclaracionSagResponse tramitarDeclaracionSag(TramiteSagRequest request) {
        log.info("Orquestando tramite SAG para viajero: {} | paso: {} | items: {}",
            request.getRutViajero(), request.getPasoFronterizo(), request.getItems().size());

        DeclaracionSagResponse response = sagClient.procesarTramite(request);

        log.info("Tramite SAG completado via ms-sag. DeclaracionId: {} | Estado: {}",
            response.getId(), response.getEstado());

        if ("CUARENTENA".equals(response.getEstado())) {
            log.warn("ALERTA SAG — Declaracion {} en CUARENTENA. Viajero: {} | Paso: {}",
                response.getId(), response.getRutViajero(), response.getPasoFronterizo());
        }

        return response;
    }
}
