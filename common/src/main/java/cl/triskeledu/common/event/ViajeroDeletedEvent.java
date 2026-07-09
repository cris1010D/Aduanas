package cl.triskeledu.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViajeroDeletedEvent implements DomainEvent {

    private String rut;

    @Override
    public String getAggregateId() {
        return rut;
    }
}
