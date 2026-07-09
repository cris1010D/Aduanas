package cl.triskeledu.aduanas.menores.event;

import cl.triskeledu.aduanas.menores.config.KafkaTopicConfig;
import cl.triskeledu.common.event.MenorCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenorEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendMenorCreated(MenorCreatedEvent event) {
        log.info("Enviando MenorCreatedEvent para rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.MENOR_CREATED_TOPIC, event.getRut(), event);
        log.info("MenorCreatedEvent enviado correctamente");
    }
}
