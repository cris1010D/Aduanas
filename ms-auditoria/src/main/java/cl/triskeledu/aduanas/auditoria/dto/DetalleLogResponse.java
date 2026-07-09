package cl.triskeledu.aduanas.auditoria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleLogResponse {

    private Integer id;
    private Integer idLog;
    private String entidad;
    private String campo;
    private String valorNuevo;
}
