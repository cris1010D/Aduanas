package cl.triskeledu.aduanas.reporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReporteRequest {

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 30)
    private String tipo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El rut del oficial es obligatorio")
    @Size(max = 12)
    private String rutOficial;

    @NotBlank(message = "El formato es obligatorio")
    @Size(max = 10)
    private String formato;
}
