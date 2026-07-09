// OficialCreateRequest.java
package cl.triskeledu.aduanas.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OficialCreateRequest {

    @NotBlank(message = "El rut es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    private String nombre;

    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 30, message = "El rol no puede superar 30 caracteres")
    private String rol;

    @NotNull(message = "El campo activo es obligatorio")
    private Boolean activo;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 6, max = 100, message = "El password debe tener entre 6 y 100 caracteres")
    private String password;
}