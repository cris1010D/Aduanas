package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

<<<<<<< HEAD
import java.time.LocalDate;

=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViajeroResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private String pasaporte;
    private String nacionalidad;
<<<<<<< HEAD

    private String tipoDocumento;
    private String paisEmisorDocumento;
    private LocalDate fechaEmisionDocumento;
    private LocalDate fechaVencimientoDocumento;

    private String email;
    private String telefono;

    private String direccionEstadia;
    private String propositoViaje;

    private String emergenciaNombre;
    private String emergenciaParentesco;
    private String emergenciaTelefono;

    private Boolean seguroVigente;
    private String seguroProveedor;
}
=======
}
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
