package cl.triskeledu.aduanas.sag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeclaracionRequest {

    @NotBlank(message = "El rut del viajero es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rutViajero;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede superar 20 caracteres")
    private String estado;

    @NotBlank(message = "El paso fronterizo es obligatorio")
    @Size(max = 40, message = "El paso fronterizo no puede superar 40 caracteres")
    private String pasoFronterizo;
}
