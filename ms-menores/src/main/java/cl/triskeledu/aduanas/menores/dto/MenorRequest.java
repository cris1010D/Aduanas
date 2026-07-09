package cl.triskeledu.aduanas.menores.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenorRequest {

    @NotBlank(message = "El rut es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    private String nombre;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNac;

    @NotBlank(message = "El rut del tutor es obligatorio")
    @Size(max = 12, message = "El rut del tutor no puede superar 12 caracteres")
    private String rutTutor;
}
