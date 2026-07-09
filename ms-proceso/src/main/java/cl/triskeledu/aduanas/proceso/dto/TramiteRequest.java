package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramiteRequest {

    @NotBlank(message = "El rut del viajero es obligatorio")
    private String rutViajero;

    @NotBlank(message = "El tipo de tramite es obligatorio")
    private String tipo;

    @NotBlank(message = "El paso fronterizo es obligatorio")
    @Size(max = 40, message = "El paso fronterizo no puede superar 40 caracteres")
    private String pasoFronterizo;

    @NotNull(message = "La fecha es obligatoria")
    private String fecha;

    @Pattern(
        regexp = "^(C\\.D\\.|CC|O\\.I\\.|P\\.A\\.T\\.)$",
        message = "Tipo de acreditacion invalido. Valores permitidos: C.D., CC, O.I., P.A.T."
    )
    private String tipoAcreditacion;
}
