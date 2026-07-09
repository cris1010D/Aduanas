package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenorValidacionRequest {

    @NotBlank(message = "El rut del menor es obligatorio")
    @Size(max = 12, message = "El rut del menor no puede superar 12 caracteres")
    private String rutMenor;

    @NotBlank(message = "El rut del tutor es obligatorio")
    @Size(max = 12, message = "El rut del tutor no puede superar 12 caracteres")
    private String rutTutor;

    @NotBlank(message = "El tipo de autorizacion es obligatorio")
    @Pattern(regexp = "^(NOTARIAL|JUDICIAL)$",
             message = "El tipo debe ser NOTARIAL o JUDICIAL")
    private String tipo;

    @NotBlank(message = "El paso fronterizo es obligatorio")
    @Size(max = 40, message = "El paso fronterizo no puede superar 40 caracteres")
    private String pasoFronterizo;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;
}
