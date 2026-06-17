package cl.triskeledu.aduanas.reporte.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteResponse {

    private Integer id;
    private String tipo;
    private LocalDate fecha;
    private String rutOficial;
    private String formato;
}
