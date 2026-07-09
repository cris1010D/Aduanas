package cl.triskeledu.aduanas.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SesionResponse {

    private Integer id;
    private String rutOficial;
    private String token;
    private LocalDate inicio;
    private LocalDate expira;
}
