package cl.triskeledu.aduanas.proceso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeclaracionAduanaRequest {

    @NotBlank(message = "El rut del viajero es obligatorio")
    private String rutViajero;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El paso fronterizo es obligatorio")
    private String pasoFronterizo;

    @NotNull(message = "Debe indicar si porta dinero en efectivo")
    private Boolean portaDineroEfectivo;

    private BigDecimal montoDinero;

    private String monedaDinero;

    @NotNull(message = "Debe indicar si porta mercancias afectas")
    private Boolean mercanciasAfectas;

    private String descripcionMercancias;

    private Double litrosAlcohol;

    private Integer cantidadCigarrillos;
}