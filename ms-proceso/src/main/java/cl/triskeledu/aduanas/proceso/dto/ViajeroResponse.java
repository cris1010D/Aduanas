package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViajeroResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private String pasaporte;
    private String nacionalidad;

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