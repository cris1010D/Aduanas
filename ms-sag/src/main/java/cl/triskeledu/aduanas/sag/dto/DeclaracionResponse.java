package cl.triskeledu.aduanas.sag.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclaracionResponse {

    private Integer id;
    private String rutViajero;
    private LocalDate fecha;
    private String estado;
    private String pasoFronterizo;
}
