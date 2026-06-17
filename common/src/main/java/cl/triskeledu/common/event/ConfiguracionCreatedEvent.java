package cl.triskeledu.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionCreatedEvent implements DomainEvent {

    private String clave;
    private String valor;
    private String msDuenio;
    private Boolean activo;

    @Override
    public String getAggregateId() {
        return clave;
    }
}
