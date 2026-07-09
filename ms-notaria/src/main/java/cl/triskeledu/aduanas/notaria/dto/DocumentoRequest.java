package cl.triskeledu.aduanas.notaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentoRequest {

    @NotNull(message = "El id del poder es obligatorio")
    private Integer idPoder;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 30)
    private String tipo;

    @NotBlank(message = "El folio es obligatorio")
    @Size(max = 20)
    private String folio;

    @NotNull(message = "La fecha de emision es obligatoria")
    private LocalDate fechaEmision;
}
