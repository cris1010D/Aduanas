package cl.triskeledu.common.event;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenorCreatedEvent implements DomainEvent {

    private String rut;
    private String nombre;
    private LocalDate fechaNac;
    private String rutTutor;

    @Override
    public String getAggregateId() {
        return rut;
    }
}
