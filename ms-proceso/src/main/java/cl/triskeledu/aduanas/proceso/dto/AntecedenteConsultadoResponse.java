package cl.triskeledu.aduanas.proceso.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

/**
 * Respuesta que ms-proceso recibe desde ms-pdi via Feign
 * tras consultar antecedentes de un viajero.
 * Espeja ConsultaAntecedenteResponse de ms-pdi.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AntecedenteConsultadoResponse {

    // --- Consulta registrada en ms-pdi ---
    private Integer idConsulta;
    private String rut;
    private String rutOficial;
    private LocalDate fecha;
    private String motivo;

    // --- Resultado del Antecedente ---
    private String resultado;       // SIN_REGISTROS | CON_REGISTROS
    private String fuente;
    private LocalDate fechaConsulta;
}
