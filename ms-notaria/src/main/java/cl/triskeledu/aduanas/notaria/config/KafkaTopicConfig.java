package cl.triskeledu.aduanas.notaria.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String PODER_CREATED_TOPIC = "notaria.poder.created";

    @Bean
    public NewTopic poderCreatedTopic() {
        return TopicBuilder.name(PODER_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
}
