package cl.triskeledu.aduanas.auth.event;

import cl.triskeledu.aduanas.auth.config.KafkaTopicConfig;
import cl.triskeledu.common.event.OficialCreatedEvent;
import cl.triskeledu.common.event.OficialDeletedEvent;
import cl.triskeledu.common.event.OficialUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OficialEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @SuppressWarnings("null")
    public void sendOficialCreated(OficialCreatedEvent event) {
        log.info("Enviando OficialCreatedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.OFICIAL_CREATED_TOPIC, event.getRut(), event);
        log.info("OficialCreatedEvent enviado correctamente");
    }
    @SuppressWarnings("null")
    public void sendOficialUpdated(OficialUpdatedEvent event) {
        log.info("Enviando OficialUpdatedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.OFICIAL_UPDATED_TOPIC, event.getRut(), event);
        log.info("OficialUpdatedEvent enviado correctamente");
    }
    @SuppressWarnings("null")
    public void sendOficialDeleted(OficialDeletedEvent event) {
        log.info("Enviando OficialDeletedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.OFICIAL_DELETED_TOPIC, event.getRut(), event);
        log.info("OficialDeletedEvent enviado correctamente");
    }
}
