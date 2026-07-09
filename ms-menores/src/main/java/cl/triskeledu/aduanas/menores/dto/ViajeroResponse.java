package cl.triskeledu.aduanas.menores.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViajeroResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private String pasaporte;
    private String nacionalidad;
}
