package cl.triskeledu.aduanas.datos.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String CONFIGURACION_CREATED_TOPIC = "datos.configuracion.created";

    /**
     * Topico publicado cuando una configuracion global es ACTUALIZADA.
     * Los MS suscritos pueden refrescar sus parametros sin reiniciar.
     */
    public static final String CONFIGURACION_UPDATED_TOPIC = "datos.configuracion.updated";

    @Bean
    public NewTopic configuracionCreatedTopic() {
        return TopicBuilder.name(CONFIGURACION_CREATED_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic configuracionUpdatedTopic() {
        return TopicBuilder.name(CONFIGURACION_UPDATED_TOPIC).partitions(1).replicas(1).build();
    }
}
