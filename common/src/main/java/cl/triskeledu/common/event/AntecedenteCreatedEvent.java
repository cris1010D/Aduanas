package cl.triskeledu.common.event;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedenteCreatedEvent implements DomainEvent {

    private String rut;
    private String resultado;
    private LocalDate fechaConsulta;
    private String fuente;

    @Override
    public String getAggregateId() {
        return rut;
    }
}
