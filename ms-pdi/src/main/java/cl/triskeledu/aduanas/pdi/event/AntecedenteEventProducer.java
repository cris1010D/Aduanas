package cl.triskeledu.aduanas.pdi.event;

import cl.triskeledu.aduanas.pdi.config.KafkaTopicConfig;
import cl.triskeledu.common.event.AntecedenteCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AntecedenteEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendAntecedenteCreated(AntecedenteCreatedEvent event) {
        log.info("Enviando AntecedenteCreatedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.ANTECEDENTE_CREATED_TOPIC, event.getRut(), event);
        log.info("AntecedenteCreatedEvent enviado correctamente");
    }
}
