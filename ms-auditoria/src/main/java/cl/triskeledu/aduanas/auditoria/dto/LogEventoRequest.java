package cl.triskeledu.aduanas.auditoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogEventoRequest {

    @NotBlank(message = "El rut del oficial es obligatorio")
    @Size(max = 12)
    private String rutOficial;

    @NotBlank(message = "La accion es obligatoria")
    @Size(max = 60)
    private String accion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El MS de origen es obligatorio")
    @Size(max = 30)
    private String msOrigen;
}
