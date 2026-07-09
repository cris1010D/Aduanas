package cl.triskeledu.aduanas.sag.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemDeclaracionRequest {

    @NotBlank(message = "La descripcion del item es obligatoria")
    @Size(max = 80, message = "La descripcion no puede superar 80 caracteres")
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    @NotBlank(message = "El nivel de riesgo es obligatorio")
    @Pattern(regexp = "^(ALTO|MEDIO|BAJO)$",
             message = "El riesgo debe ser ALTO, MEDIO o BAJO")
    private String riesgo;
}
