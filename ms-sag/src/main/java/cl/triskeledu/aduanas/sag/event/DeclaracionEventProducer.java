package cl.triskeledu.aduanas.sag.event;

import cl.triskeledu.aduanas.sag.config.KafkaTopicConfig;
import cl.triskeledu.common.event.DeclaracionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeclaracionEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendDeclaracionCreated(DeclaracionCreatedEvent event) {
        log.info("Enviando DeclaracionCreatedEvent para rutViajero: {}", event.getRutViajero());
        kafkaTemplate.send(KafkaTopicConfig.DECLARACION_CREATED_TOPIC, event.getRutViajero(), event);
        log.info("DeclaracionCreatedEvent enviado correctamente");
    }
}
