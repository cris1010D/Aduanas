package cl.triskeledu.aduanas.auditoria.event;

import cl.triskeledu.aduanas.auditoria.config.KafkaTopicConfig;
import cl.triskeledu.common.event.LogEventoCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogEventoEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendLogEventoCreated(LogEventoCreatedEvent event) {
        log.info("Enviando LogEventoCreatedEvent para oficial: {}", event.getRutOficial());
        kafkaTemplate.send(KafkaTopicConfig.LOG_EVENTO_CREATED_TOPIC, event.getRutOficial(), event);
        log.info("LogEventoCreatedEvent enviado correctamente");
    }
}
