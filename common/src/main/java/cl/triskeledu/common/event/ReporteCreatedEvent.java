package cl.triskeledu.common.event;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteCreatedEvent implements DomainEvent {

    private String tipo;
    private LocalDate fecha;
    private String rutOficial;
    private String formato;

    @Override
    public String getAggregateId() {
        return tipo;
    }
}
