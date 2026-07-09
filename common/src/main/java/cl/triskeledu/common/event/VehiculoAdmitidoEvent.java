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
public class VehiculoAdmitidoEvent implements DomainEvent {

    private Integer id;
    private String placa;
    private String propietario;
    private String fechaIngreso;
    private String fechaVencimiento;

    @Override
    public String getAggregateId() {
        return placa;
    }
}
