package cl.triskeledu.aduanas.sag.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String DECLARACION_CREATED_TOPIC = "sag.declaracion.created";

    @Bean
    public NewTopic declaracionCreatedTopic() {
        return TopicBuilder.name(DECLARACION_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
}
