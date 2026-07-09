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
public class SalidaDiplomaticaCreatedEvent implements DomainEvent {

    private Integer id;
    private String rutViajero;
    private String tipoAcreditacion;
    private String pasoFronterizo;
    private String fecha;

    @Override
    public String getAggregateId() {
        return id != null ? String.valueOf(id) : null;
    }
}
