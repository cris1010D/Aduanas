package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * Request para consultar antecedentes PDI de un viajero.
 * ms-proceso lo reenvía a ms-pdi via Feign (PdiClient).
 * Espeja ConsultaRequest de ms-pdi.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConsultaPdiRequest {

    @NotBlank(message = "El rut del viajero es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El rut del oficial es obligatorio")
    @Size(max = 12, message = "El rut del oficial no puede superar 12 caracteres")
    private String rutOficial;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 60, message = "El motivo no puede superar 60 caracteres")
    private String motivo;
}
