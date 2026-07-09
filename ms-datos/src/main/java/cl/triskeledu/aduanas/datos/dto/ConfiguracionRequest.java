package cl.triskeledu.aduanas.datos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConfiguracionRequest {

    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 60)
    private String clave;

    @NotBlank(message = "El valor es obligatorio")
    @Size(max = 120)
    private String valor;

    @NotBlank(message = "El MS duenio es obligatorio")
    @Size(max = 30)
    private String msDuenio;

    @NotNull(message = "El campo activo es obligatorio")
    private Boolean activo;
}
