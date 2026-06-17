package cl.triskeledu.aduanas.monitoreo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthResponse {

    private String status;
}
