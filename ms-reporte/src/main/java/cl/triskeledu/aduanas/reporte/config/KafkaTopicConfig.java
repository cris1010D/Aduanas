package cl.triskeledu.aduanas.reporte.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String REPORTE_CREATED_TOPIC = "reporte.reporte.created";

    @Bean
    public NewTopic reporteCreatedTopic() {
        return TopicBuilder.name(REPORTE_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
}
