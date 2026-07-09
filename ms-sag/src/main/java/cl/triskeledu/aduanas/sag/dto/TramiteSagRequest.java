package cl.triskeledu.aduanas.sag.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TramiteSagRequest {

    @NotBlank(message = "El rut del viajero es obligatorio")
    @Size(max = 12, message = "El rut no puede superar 12 caracteres")
    private String rutViajero;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El paso fronterizo es obligatorio")
    @Size(max = 40, message = "El paso fronterizo no puede superar 40 caracteres")
    private String pasoFronterizo;

    @NotEmpty(message = "Debe incluir al menos un item en la declaracion")
    @Valid
    private List<ItemDeclaracionRequest> items;
}
