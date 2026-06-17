package cl.triskeledu.aduanas.datos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfiguracionResponse {

    private Integer id;
    private String clave;
    private String valor;
    private String msDuenio;
    private Boolean activo;
}
