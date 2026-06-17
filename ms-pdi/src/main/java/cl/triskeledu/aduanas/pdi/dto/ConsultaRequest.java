package cl.triskeledu.aduanas.pdi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConsultaRequest {

    @NotBlank(message = "El rut es obligatorio")
    @Size(max = 12)
    private String rut;

    @NotBlank(message = "El rut del oficial es obligatorio")
    @Size(max = 12)
    private String rutOficial;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 60)
    private String motivo;
}
