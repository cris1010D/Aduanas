package cl.triskeledu.aduanas.notaria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoResponse {

    private Integer id;
    private Integer idPoder;
    private String tipo;
    private String folio;
    private LocalDate fechaEmision;
}
