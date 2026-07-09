package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.client.PdiClient;
import cl.triskeledu.aduanas.proceso.config.KafkaTopicConfig;
import cl.triskeledu.aduanas.proceso.dto.AntecedenteConsultadoResponse;
import cl.triskeledu.aduanas.proceso.dto.ConsultaPdiRequest;
import cl.triskeledu.common.event.AntecedenteConsultadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Servicio que orquesta la consulta de antecedentes PDI desde ms-proceso.
 *
 * Flujo:
 *   1. Llama a ms-pdi via PdiClient (Feign) dentro de un CompletableFuture.
 *   2. Aplica timeout estricto de 2000 ms (ISO 25010 — caracteristica de rendimiento).
 *      Si ms-pdi no responde en ese plazo, lanza RuntimeException con mensaje claro.
 *   3. Si la consulta es exitosa, publica AntecedenteConsultadoEvent en Kafka
 *      para que ms-auditoria guarde el registro de la operacion.
 *   4. Registra metricas de tiempo de respuesta en el log para trazabilidad.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PdiConsultaService {

    /** Limite de tiempo de respuesta total — ISO 25010 caracteristica de rendimiento. */
    private static final long SLA_TIMEOUT_MS = 2000L;

    private final PdiClient pdiClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public AntecedenteConsultadoResponse consultarAntecedentes(ConsultaPdiRequest request) {
        log.info("Iniciando consulta PDI para rut: {} | oficial: {} | motivo: {}",
            request.getRut(), request.getRutOficial(), request.getMotivo());

        long inicio = System.currentTimeMillis();

        // --- Llamada a ms-pdi con timeout ISO 25010 (2000 ms) -------------------
        CompletableFuture<AntecedenteConsultadoResponse> future =
            CompletableFuture.supplyAsync(() -> pdiClient.consultarAntecedente(request));

        AntecedenteConsultadoResponse response;
        try {
            response = future.get(SLA_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            long elapsed = System.currentTimeMillis() - inicio;
            log.error("[ISO-25010] SLA EXCEDIDO — ms-pdi no respondio en {}ms (limite: {}ms) para rut: {}",
                elapsed, SLA_TIMEOUT_MS, request.getRut());
            future.cancel(true);
            throw new RuntimeException(
                "Servicio PDI excedio el tiempo maximo de respuesta (" + SLA_TIMEOUT_MS + " ms). "
                + "Intente nuevamente o contacte al administrador del sistema."
            );
        } catch (Exception e) {
            log.error("[PDI] Error al consultar antecedentes para rut {}: {}", request.getRut(), e.getMessage(), e);
            throw new RuntimeException("Error al consultar antecedentes en ms-pdi", e);
        }

        long elapsed = System.currentTimeMillis() - inicio;
        log.info("[ISO-25010] Consulta PDI completada en {}ms (SLA: {}ms) — rut: {} | resultado: {}",
            elapsed, SLA_TIMEOUT_MS, response.getRut(), response.getResultado());

        // --- Publicar AntecedenteConsultadoEvent → ms-auditoria ------------------
        
        AntecedenteConsultadoEvent evento = AntecedenteConsultadoEvent.builder()
            .rut(response.getRut())
            .rutOficial(response.getRutOficial())
            .resultado(response.getResultado())
            .fecha(response.getFecha() != null ? response.getFecha().toString() : null)
            .fuente(response.getFuente())
            .build();

        log.info("Publicando AntecedenteConsultadoEvent para rut: {}", evento.getRut());
        kafkaTemplate.send(KafkaTopicConfig.ANTECEDENTE_CONSULTADO_TOPIC, evento.getRut(), evento);
        log.info("AntecedenteConsultadoEvent publicado correctamente");

        return response;
    }
}
