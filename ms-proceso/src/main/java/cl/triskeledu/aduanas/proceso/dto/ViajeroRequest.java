package cl.triskeledu.aduanas.proceso.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.Email;
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

<<<<<<< HEAD
import java.time.LocalDate;

=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
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
<<<<<<< HEAD

    // ── Documento de viaje ───────────────────────────────────────────────
    @Size(max = 20, message = "Tipo de documento invalido")
    private String tipoDocumento;

    @Size(max = 40)
    private String paisEmisorDocumento;

    private LocalDate fechaEmisionDocumento;

    private LocalDate fechaVencimientoDocumento;

    // ── Contacto ─────────────────────────────────────────────────────────
    @Email(message = "Email invalido")
    @Size(max = 100)
    private String email;

    @Size(max = 25)
    private String telefono;

    // ── Logística ────────────────────────────────────────────────────────
    @Size(max = 160)
    private String direccionEstadia;

    @Size(max = 20)
    private String propositoViaje;

    // ── Contacto de emergencia ──────────────────────────────────────────
    @Size(max = 80)
    private String emergenciaNombre;

    @Size(max = 40)
    private String emergenciaParentesco;

    @Size(max = 25)
    private String emergenciaTelefono;

    // ── Seguro ───────────────────────────────────────────────────────────
    private Boolean seguroVigente;

    @Size(max = 80)
    private String seguroProveedor;
}
=======
}
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
