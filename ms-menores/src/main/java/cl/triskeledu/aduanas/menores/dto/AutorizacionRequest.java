package cl.triskeledu.aduanas.menores.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AutorizacionRequest {

    @NotBlank(message = "El rut del menor es obligatorio")
    @Size(max = 12)
    private String rutMenor;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 30)
    private String tipo;

    @NotNull(message = "La vigencia es obligatoria")
    private LocalDate vigencia;

    @NotBlank(message = "La notaria de origen es obligatoria")
    @Size(max = 60)
    private String notariaOrigen;
}
