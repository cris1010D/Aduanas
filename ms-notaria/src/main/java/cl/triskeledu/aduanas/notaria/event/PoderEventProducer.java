package cl.triskeledu.aduanas.notaria.event;

import cl.triskeledu.aduanas.notaria.config.KafkaTopicConfig;
import cl.triskeledu.common.event.PoderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PoderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendPoderCreated(PoderCreatedEvent event) {
        log.info("Enviando PoderCreatedEvent para titular: {}", event.getRutTitular());
        kafkaTemplate.send(KafkaTopicConfig.PODER_CREATED_TOPIC, event.getRutTitular(), event);
        log.info("PoderCreatedEvent enviado correctamente");
    }
}
