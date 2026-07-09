package cl.triskeledu.aduanas.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String OFICIAL_CREATED_TOPIC = "auth.oficial.created";
    public static final String OFICIAL_UPDATED_TOPIC = "auth.oficial.updated";
    public static final String OFICIAL_DELETED_TOPIC = "auth.oficial.deleted";

    @Bean
    public NewTopic oficialCreatedTopic() {
        return TopicBuilder.name(OFICIAL_CREATED_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic oficialUpdatedTopic() {
        return TopicBuilder.name(OFICIAL_UPDATED_TOPIC).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic oficialDeletedTopic() {
        return TopicBuilder.name(OFICIAL_DELETED_TOPIC).partitions(1).replicas(1).build();
    }
}
