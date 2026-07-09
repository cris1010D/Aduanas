package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclaracionAduanaResponse {

    private Integer id;
    private String rutViajero;
    private LocalDate fecha;
    private String pasoFronterizo;

    private Boolean portaDineroEfectivo;
    private BigDecimal montoDinero;
    private String monedaDinero;

    private Boolean mercanciasAfectas;
    private String descripcionMercancias;

    private Double litrosAlcohol;
    private Integer cantidadCigarrillos;

    private Boolean excedeFranquicia;
    private String estado;
}