package cl.triskeledu.aduanas.menores.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutorizacionResponse {

    private Integer id;
    private String rutMenor;
    private String tipo;
    private LocalDate vigencia;
    private String notariaOrigen;
}
