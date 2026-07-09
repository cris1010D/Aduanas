package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutorizacionMenorResponse {

    private Integer id;
    private String rutMenor;
    private String rutTutor;
    private String tipo;
    private LocalDate fechaVencimiento;
}
