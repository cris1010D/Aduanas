package cl.triskeledu.aduanas.pdi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ANTECEDENTE_CREATED_TOPIC = "pdi.antecedente.created";

    @Bean
    public NewTopic antecedenteCreatedTopic() {
        return TopicBuilder.name(ANTECEDENTE_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
}
