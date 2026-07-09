package cl.triskeledu.aduanas.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SesionRequest {

    @NotBlank(message = "El rut del oficial es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rutOficial;

    @NotBlank(message = "El token es obligatorio")
    @Size(max = 120, message = "El token no puede superar 120 caracteres")
    private String token;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate inicio;

    @NotNull(message = "La fecha de expiracion es obligatoria")
    private LocalDate expira;
}
