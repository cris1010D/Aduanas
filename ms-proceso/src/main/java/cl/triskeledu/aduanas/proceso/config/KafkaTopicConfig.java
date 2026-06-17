package cl.triskeledu.aduanas.proceso.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String VIAJERO_CREATED_TOPIC = "proceso.viajero.created";
    public static final String VIAJERO_UPDATED_TOPIC = "proceso.viajero.updated";
    public static final String VIAJERO_DELETED_TOPIC = "proceso.viajero.deleted";

    @Bean
    public NewTopic viajeroCreatedTopic() {
        return TopicBuilder.name(VIAJERO_CREATED_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic viajeroUpdatedTopic() {
        return TopicBuilder.name(VIAJERO_UPDATED_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic viajeroDeletedTopic() {
        return TopicBuilder.name(VIAJERO_DELETED_TOPIC).partitions(1).replicas(1).build();
    }
}
