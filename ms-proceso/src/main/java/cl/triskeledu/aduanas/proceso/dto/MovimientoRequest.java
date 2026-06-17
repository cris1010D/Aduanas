package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MovimientoRequest {

    @NotBlank(message = "El rut del viajero es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rutViajero;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo no puede superar 20 caracteres")
    private String tipo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El paso fronterizo es obligatorio")
    @Size(max = 40, message = "El paso fronterizo no puede superar 40 caracteres")
    private String pasoFronterizo;
}
