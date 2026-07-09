package cl.triskeledu.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OficialUpdatedEvent implements DomainEvent {

    private String rut;
    private String nombre;
    private String rol;
    private Boolean activo;

    @Override
    public String getAggregateId() {
        return rut;
    }
}
