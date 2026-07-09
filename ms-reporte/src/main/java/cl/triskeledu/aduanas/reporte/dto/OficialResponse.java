package cl.triskeledu.aduanas.reporte.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OficialResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private String rol;
    private Boolean activo;
}
