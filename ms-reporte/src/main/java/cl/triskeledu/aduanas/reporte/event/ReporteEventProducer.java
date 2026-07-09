package cl.triskeledu.aduanas.reporte.event;

import cl.triskeledu.aduanas.reporte.config.KafkaTopicConfig;
import cl.triskeledu.common.event.ReporteCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReporteEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendReporteCreated(ReporteCreatedEvent event) {
        log.info("Enviando ReporteCreatedEvent para tipo: {}", event.getTipo());
        kafkaTemplate.send(KafkaTopicConfig.REPORTE_CREATED_TOPIC, event.getTipo(), event);
        log.info("ReporteCreatedEvent enviado correctamente");
    }
}
