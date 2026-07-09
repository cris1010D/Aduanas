package cl.triskeledu.aduanas.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistroViajeroRequest {

    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 12, message = "El RUT no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    private String nombre;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmPassword;
}
