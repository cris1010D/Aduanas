package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehiculoRequest {

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 20, message = "La placa no puede superar 20 caracteres")
    private String placa;

    @NotBlank(message = "El propietario es obligatorio")
    @Size(max = 80, message = "El propietario no puede superar 80 caracteres")
    private String propietario;

<<<<<<< HEAD
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;

    // ── Nuevos, todos opcionales (solo aplican a vehículos comerciales) ────
    @Size(max = 12, message = "El rut del propietario no puede superar 12 caracteres")
    private String rutPropietario;

    private Boolean esComercial;

    @Size(max = 100, message = "La empresa no puede superar 100 caracteres")
    private String empresaTransportista;

    @Size(max = 20, message = "La patente del remolque no puede superar 20 caracteres")
    private String patenteRemolque;

    @Size(max = 500, message = "El manifiesto no puede superar 500 caracteres")
    private String manifiestoCarga;

    @Size(max = 60, message = "La marca no puede superar 60 caracteres")
    private String marca;

    @Size(max = 60, message = "El modelo no puede superar 60 caracteres")
    private String modelo;

    private Integer anio;
}
=======
    // El sistema calcula estas fechas automáticamente (hoy + 180 días)
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;
}
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
