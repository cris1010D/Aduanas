package cl.triskeledu.aduanas.monitoreo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Dashboard de salud del ecosistema Aduanas.
 *
 * Consolida el estado de todos los microservicios en un unico objeto
 * para que el Supervisor visualice la disponibilidad del sistema de un vistazo.
 *
 * Campos:
 *   totalServicios          : cantidad de MS monitoreados
 *   serviciosUp             : cuantos responden con estado UP
 *   serviciosDown           : cuantos no responden o retornan DOWN
 *   porcentajeDisponibilidad: (serviciosUp / totalServicios) * 100, redondeado a 1 decimal
 *   estadoGeneral           : "OPERATIVO" si todos UP, "DEGRADADO" si algunos DOWN, "CRITICO" si todos DOWN
 *   estados                 : detalle por microservicio
 *   fechaConsulta           : timestamp ISO de cuando se realizo la consulta
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SistemaSaludResponse {

    private Integer totalServicios;
    private Integer serviciosUp;
    private Integer serviciosDown;
    private Double  porcentajeDisponibilidad;
    private String  estadoGeneral;
    private List<EstadoMsResponse> estados;
    private LocalDateTime fechaConsulta;
}
