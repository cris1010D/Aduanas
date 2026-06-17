package cl.triskeledu.aduanas.datos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CacheEntryRequest {

    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 60)
    private String clave;

    @NotBlank(message = "El valor de cache es obligatorio")
    @Size(max = 120)
    private String valorCache;

    @NotNull(message = "La fecha de expiracion es obligatoria")
    private LocalDate expira;

    @NotNull(message = "Los hits son obligatorios")
    private Integer hits;
}
