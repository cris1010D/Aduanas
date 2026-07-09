package cl.triskeledu.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenorAutorizadoEvent implements DomainEvent {

    private String rutMenor;
    private String rutTutor;
    private String tipo;
    private String fechaVencimiento;

    @Override
    public String getAggregateId() {
        return rutMenor;
    }
}
