package cl.triskeledu.aduanas.menores.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String MENOR_CREATED_TOPIC = "menores.menor.created";

    @Bean
    public NewTopic menorCreatedTopic() {
        return TopicBuilder.name(MENOR_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
}
