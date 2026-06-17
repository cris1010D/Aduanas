package cl.triskeledu.aduanas.menores.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenorResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private LocalDate fechaNac;
    private String rutTutor;
}
