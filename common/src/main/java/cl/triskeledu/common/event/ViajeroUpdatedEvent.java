package cl.triskeledu.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViajeroUpdatedEvent implements DomainEvent {

    private String rut;
    private String nombre;
    private String pasaporte;
    private String nacionalidad;

    @Override
    public String getAggregateId() {
        return rut;
    }
}
