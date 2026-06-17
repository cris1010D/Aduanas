package cl.triskeledu.common.event;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeclaracionCreatedEvent implements DomainEvent {

    private String rutViajero;
    private LocalDate fecha;
    private String estado;
    private String pasoFronterizo;

    @Override
    public String getAggregateId() {
        return rutViajero;
    }
}
