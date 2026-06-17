package cl.triskeledu.aduanas.auditoria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEventoResponse {

    private Integer id;
    private String rutOficial;
    private String accion;
    private LocalDate fecha;
    private String msOrigen;
}
