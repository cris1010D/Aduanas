package cl.triskeledu.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Evento publicado por ms-proceso en Kafka cuando se consulta
 * el historial de antecedentes de un viajero via ms-pdi.
 * Consumidor principal: ms-auditoria.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecedenteConsultadoEvent implements DomainEvent {

    /** RUT del viajero cuyo antecedente fue consultado. */
    private String rut;

    /** RUT del oficial que realizo la consulta. */
    private String rutOficial;

    /** Resultado devuelto por ms-pdi: SIN_REGISTROS | CON_REGISTROS */
    private String resultado;

    /** Fecha de la consulta en formato ISO (YYYY-MM-DD). */
    private String fecha;

    /** Fuente del antecedente (ej. INTERPOL-CHL, REGISTRO_CIVIL). */
    private String fuente;

    @Override
    public String getAggregateId() {
        return rut;
    }
}
