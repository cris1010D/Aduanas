package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehiculoResponse {

    private Integer id;
    private String placa;
    private String propietario;
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;

    private String rutPropietario;
    private Boolean esComercial;
    private String empresaTransportista;
    private String patenteRemolque;
    private String manifiestoCarga;

    private String marca;
    private String modelo;
    private Integer anio;
}