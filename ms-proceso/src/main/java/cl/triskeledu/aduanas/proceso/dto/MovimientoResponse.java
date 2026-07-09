package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovimientoResponse {

    private Integer id;
    private String rutViajero;
    private String tipo;
    private LocalDate fecha;
    private String pasoFronterizo;
}
