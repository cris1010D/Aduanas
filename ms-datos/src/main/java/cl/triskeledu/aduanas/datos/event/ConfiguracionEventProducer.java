package cl.triskeledu.aduanas.datos.event;

import cl.triskeledu.aduanas.datos.config.KafkaTopicConfig;
import cl.triskeledu.common.event.ConfiguracionActualizadaEvent;
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

    /**
     * Notifica a todos los MS interesados que un parametro global cambio.
     * Los consumidores pueden refrescar su configuracion en caliente.
     *
     * @param event contiene clave, valorAnterior, valorNuevo, msDuenio
     */
    @SuppressWarnings("null")
    public void sendConfiguracionActualizada(ConfiguracionActualizadaEvent event) {
        log.info("Enviando ConfiguracionActualizadaEvent: clave={} | {} -> {}",
            event.getClave(), event.getValorAnterior(), event.getValorNuevo());
        kafkaTemplate.send(KafkaTopicConfig.CONFIGURACION_UPDATED_TOPIC, event.getClave(), event);
        log.info("ConfiguracionActualizadaEvent enviado correctamente");
    }
}
