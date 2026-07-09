package cl.triskeledu.aduanas.notaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PoderRequest {

    @NotBlank(message = "El rut del titular es obligatorio")
    @Size(max = 12)
    private String rutTitular;

    @NotBlank(message = "El rut del apoderado es obligatorio")
    @Size(max = 12)
    private String rutApoderado;

    @NotBlank(message = "La notaria de origen es obligatoria")
    @Size(max = 60)
    private String notariaOrigen;

    @NotNull(message = "La vigencia es obligatoria")
    private LocalDate vigencia;
}
