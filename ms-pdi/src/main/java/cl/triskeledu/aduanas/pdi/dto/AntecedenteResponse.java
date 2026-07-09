package cl.triskeledu.aduanas.pdi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AntecedenteResponse {

    private Integer id;
    private String rut;
    private String resultado;
    private LocalDate fechaConsulta;
    private String fuente;
}
