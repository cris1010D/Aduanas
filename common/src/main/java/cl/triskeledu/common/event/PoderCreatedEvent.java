package cl.triskeledu.common.event;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoderCreatedEvent implements DomainEvent {

    private String rutTitular;
    private String rutApoderado;
    private String notariaOrigen;
    private LocalDate vigencia;

    @Override
    public String getAggregateId() {
        return rutTitular;
    }
}
