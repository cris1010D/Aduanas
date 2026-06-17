package cl.triskeledu.aduanas.datos.event;

import cl.triskeledu.aduanas.datos.config.KafkaTopicConfig;
import cl.triskeledu.common.event.ConfiguracionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfiguracionEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("null")
    public void sendConfiguracionCreated(ConfiguracionCreatedEvent event) {
        log.info("Enviando ConfiguracionCreatedEvent para clave: {}", event.getClave());
        kafkaTemplate.send(KafkaTopicConfig.CONFIGURACION_CREATED_TOPIC, event.getClave(), event);
        log.info("ConfiguracionCreatedEvent enviado correctamente");
    }
}
