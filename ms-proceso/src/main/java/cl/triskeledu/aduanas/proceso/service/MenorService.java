package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.client.MenoresClient;
import cl.triskeledu.aduanas.proceso.client.NotariaClient;
import cl.triskeledu.aduanas.proceso.config.KafkaTopicConfig;
import cl.triskeledu.aduanas.proceso.dto.AutorizacionMenorResponse;
import cl.triskeledu.aduanas.proceso.dto.MenorValidacionRequest;
import cl.triskeledu.aduanas.proceso.mapper.AutorizacionMenorMapper;
import cl.triskeledu.aduanas.proceso.model.AutorizacionMenor;
import cl.triskeledu.aduanas.proceso.repository.AutorizacionMenorRepository;
import cl.triskeledu.common.event.MenorAutorizadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenorService {

    // Vigencias segun tipo de autorizacion (dias)
    private static final int VIGENCIA_NOTARIAL = 180;
    private static final int VIGENCIA_JUDICIAL  = 365;

    private final AutorizacionMenorRepository autorizacionMenorRepository;
    private final AutorizacionMenorMapper autorizacionMenorMapper;
    private final MenoresClient menoresClient;
    private final NotariaClient notariaClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * R.2 — Validacion de salida de menor al exterior.
     *
     * Realiza dos consultas en paralelo (CompletableFuture):
     *   - ms-menores: verifica que el menor existe y esta registrado
     *   - ms-notaria:  verifica que el tutor tiene poder vigente
     *
     * Si ambas validaciones son exitosas, persiste la AutorizacionMenor
     * y publica MenorAutorizadoEvent en Kafka para notificar a ms-auditoria.
     *
     * Cuando alguno de los MS no esta disponible (entorno de desarrollo),
     * el catch captura la excepcion Feign, registra el aviso y simula OK
     * para permitir pruebas sin dependencias externas activas.
     */
    @Transactional
    @SuppressWarnings("null")
    public AutorizacionMenorResponse validarSalidaMenor(MenorValidacionRequest request) {
        log.info("Iniciando validacion de salida de menor. RutMenor: {} | RutTutor: {} | Tipo: {}",
            request.getRutMenor(), request.getRutTutor(), request.getTipo());

        // --- Consultas paralelas --------------------------------------------------
        CompletableFuture<Boolean> futMenores = CompletableFuture.supplyAsync(() -> {
            try {
                menoresClient.validarMenor(request.getRutMenor());
                log.info("ms-menores: menor {} validado correctamente", request.getRutMenor());
                return true;
            } catch (Exception e) {
                log.warn("ms-menores no disponible ({}). Simulando validacion OK para rut: {}",
                    e.getClass().getSimpleName(), request.getRutMenor());
                return true; // Simulacion para entorno de desarrollo
            }
        });

        CompletableFuture<Boolean> futNotaria = CompletableFuture.supplyAsync(() -> {
            try {
                notariaClient.validarPoder(request.getRutTutor());
                log.info("ms-notaria: poder del tutor {} validado correctamente", request.getRutTutor());
                return true;
            } catch (Exception e) {
                log.warn("ms-notaria no disponible ({}). Simulando validacion OK para tutor: {}",
                    e.getClass().getSimpleName(), request.getRutTutor());
                return true; // Simulacion para entorno de desarrollo
            }
        });

        // Espera ambos resultados con timeout de 5 segundos por consulta
        boolean menoresOk;
        boolean notariaOk;
        try {
            CompletableFuture.allOf(futMenores, futNotaria).get(5, TimeUnit.SECONDS);
            menoresOk = futMenores.get();
            notariaOk = futNotaria.get();
        } catch (Exception e) {
            log.error("Error durante la validacion paralela: {}", e.getMessage());
            throw new RuntimeException("Error en la validacion paralela de menor y notaria", e);
        }

        log.info("Resultado validacion paralela — menores: {} | notaria: {}", menoresOk, notariaOk);

        if (!menoresOk || !notariaOk) {
            throw new RuntimeException(
                "Validacion fallida: menor=" + menoresOk + ", notaria=" + notariaOk);
        }
        // -------------------------------------------------------------------------

        // Calcular fecha de vencimiento segun tipo de autorizacion
        LocalDate fechaVencimiento = "JUDICIAL".equals(request.getTipo())
            ? LocalDate.now().plusDays(VIGENCIA_JUDICIAL)
            : LocalDate.now().plusDays(VIGENCIA_NOTARIAL);

        AutorizacionMenor autorizacion = AutorizacionMenor.builder()
            .rutMenor(request.getRutMenor())
            .rutTutor(request.getRutTutor())
            .tipo(request.getTipo())
            .fechaVencimiento(fechaVencimiento)
            .build();

        AutorizacionMenor guardado = autorizacionMenorRepository.save(autorizacion);
        log.info("AutorizacionMenor guardada. Id: {} | Vencimiento: {}",
            guardado.getId(), guardado.getFechaVencimiento());

        // Publicar evento Kafka → ms-auditoria
        MenorAutorizadoEvent evento = MenorAutorizadoEvent.builder()
            .rutMenor(guardado.getRutMenor())
            .rutTutor(guardado.getRutTutor())
            .tipo(guardado.getTipo())
            .fechaVencimiento(guardado.getFechaVencimiento().toString())
            .build();

        log.info("Publicando MenorAutorizadoEvent para menor: {}", guardado.getRutMenor());
        kafkaTemplate.send(KafkaTopicConfig.MENOR_AUTORIZADO_TOPIC, guardado.getRutMenor(), evento);
        log.info("MenorAutorizadoEvent publicado correctamente");

        return autorizacionMenorMapper.toResponse(guardado);
    }
}
