package cl.triskeledu.aduanas.auditoria.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String LOG_EVENTO_CREATED_TOPIC = "auditoria.log.created";

    @Bean
    public NewTopic logEventoCreatedTopic() {
        return TopicBuilder.name(LOG_EVENTO_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
}
