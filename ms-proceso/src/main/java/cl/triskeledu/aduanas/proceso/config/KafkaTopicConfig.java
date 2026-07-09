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

    // --- Salida Diplomatica ---
    public static final String SALIDA_DIPLOMATICA_TOPIC = "proceso.salida_diplomatica.created";

    @Bean
    public NewTopic salidaDiplomaticaTopic() {
        return TopicBuilder.name(SALIDA_DIPLOMATICA_TOPIC).partitions(1).replicas(1).build();
    }

    // --- Vehiculo ---
    public static final String VEHICULO_ADMITIDO_TOPIC = "proceso.vehiculo.admitido";

    @Bean
    public NewTopic vehiculoAdmitidoTopic() {
        return TopicBuilder.name(VEHICULO_ADMITIDO_TOPIC).partitions(1).replicas(1).build();
    }

    // --- Menor ---
    public static final String MENOR_AUTORIZADO_TOPIC = "proceso.menor.autorizado";

    @Bean
    public NewTopic menorAutorizadoTopic() {
        return TopicBuilder.name(MENOR_AUTORIZADO_TOPIC).partitions(1).replicas(1).build();
    }

    // --- PDI ---
    public static final String ANTECEDENTE_CONSULTADO_TOPIC = "proceso.antecedente.consultado";

    @Bean
    public NewTopic antecedenteConsultadoTopic() {
        return TopicBuilder.name(ANTECEDENTE_CONSULTADO_TOPIC).partitions(1).replicas(1).build();
    }
}
