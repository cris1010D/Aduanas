package cl.triskeledu.common.event;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEventoCreatedEvent implements DomainEvent {

    private String rutOficial;
    private String accion;
    private LocalDate fecha;
    private String msOrigen;

    @Override
    public String getAggregateId() {
        return rutOficial;
    }
}
