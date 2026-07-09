package cl.triskeledu.aduanas.pdi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

/**
 * R.6 — Respuesta combinada del flujo "consultar antecedentes".
 *
 * Agrupa en un solo objeto:
 *   - Los datos de la Consulta registrada (quién preguntó, cuándo, por qué)
 *   - El resultado del Antecedente del viajero (SIN_REGISTROS | CON_REGISTROS)
 *
 * Esto permite que el oficial de PDI obtenga en una sola llamada
 * tanto el registro de auditoría de la consulta como el resultado
 * de los antecedentes del viajero consultado.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaAntecedenteResponse {

    // --- Consulta registrada ---
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
