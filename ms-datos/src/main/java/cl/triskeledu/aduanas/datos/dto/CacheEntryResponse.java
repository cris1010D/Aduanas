package cl.triskeledu.aduanas.datos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheEntryResponse {

    private Integer id;
    private String clave;
    private String valorCache;
    private LocalDate expira;
    private Integer hits;
}
