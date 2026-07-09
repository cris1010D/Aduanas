package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO de respuesta que ms-proceso recibe desde ms-sag via Feign
 * tras procesar un tramite SAG. Espeja DeclaracionResponse de ms-sag.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclaracionSagResponse {

    private Integer id;
    private String rutViajero;
    private LocalDate fecha;
    private String estado;
    private String pasoFronterizo;
}
