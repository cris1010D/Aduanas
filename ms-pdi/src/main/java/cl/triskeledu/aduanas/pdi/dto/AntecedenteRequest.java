package cl.triskeledu.aduanas.pdi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AntecedenteRequest {

    @NotBlank(message = "El rut es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El resultado es obligatorio")
    @Pattern(regexp = "^(SIN_REGISTROS|CON_REGISTROS)$",
             message = "El resultado debe ser SIN_REGISTROS o CON_REGISTROS")
    @Size(max = 20, message = "El resultado no puede superar 20 caracteres")
    private String resultado;

    @NotNull(message = "La fecha de consulta es obligatoria")
    private LocalDate fechaConsulta;

    @NotBlank(message = "La fuente es obligatoria")
    @Size(max = 40, message = "La fuente no puede superar 40 caracteres")
    private String fuente;
}
