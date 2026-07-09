package cl.triskeledu.aduanas.proceso.event;

import cl.triskeledu.aduanas.proceso.config.KafkaTopicConfig;
import cl.triskeledu.common.event.ViajeroCreatedEvent;
import cl.triskeledu.common.event.ViajeroDeletedEvent;
import cl.triskeledu.common.event.ViajeroUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViajeroEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @SuppressWarnings("null")
    public void sendViajeroCreated(ViajeroCreatedEvent event) {
        log.info("Enviando ViajeroCreatedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.VIAJERO_CREATED_TOPIC, event.getRut(), event);
        log.info("ViajeroCreatedEvent enviado correctamente");
    }
    @SuppressWarnings("null")
    public void sendViajeroUpdated(ViajeroUpdatedEvent event) {
        log.info("Enviando ViajeroUpdatedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.VIAJERO_UPDATED_TOPIC, event.getRut(), event);
        log.info("ViajeroUpdatedEvent enviado correctamente");
    }
    @SuppressWarnings("null")
    public void sendViajeroDeleted(ViajeroDeletedEvent event) {
        log.info("Enviando ViajeroDeletedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.VIAJERO_DELETED_TOPIC, event.getRut(), event);
        log.info("ViajeroDeletedEvent enviado correctamente");
    }
}
