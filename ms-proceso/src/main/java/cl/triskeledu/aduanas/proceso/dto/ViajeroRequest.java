package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ViajeroRequest {

    @NotBlank(message = "El rut es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    private String nombre;

    @Size(max = 20, message = "El pasaporte no puede superar 20 caracteres")
    private String pasaporte;

    @NotBlank(message = "La nacionalidad es obligatoria")
    @Size(max = 40, message = "La nacionalidad no puede superar 40 caracteres")
    private String nacionalidad;
}
