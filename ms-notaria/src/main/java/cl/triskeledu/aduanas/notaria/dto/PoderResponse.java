package cl.triskeledu.aduanas.notaria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoderResponse {

    private Integer id;
    private String rutTitular;
    private String rutApoderado;
    private String notariaOrigen;
    private LocalDate vigencia;
}
