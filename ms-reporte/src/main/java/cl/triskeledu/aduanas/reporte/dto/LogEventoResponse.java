package cl.triskeledu.aduanas.reporte.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;

/**
 * DTO local que espeja la respuesta de ms-auditoria GET /api/v1/logs.
 * Se mantiene en ms-reporte para no acoplar el modulo a la estructura
 * interna de ms-auditoria.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEventoResponse {

    private Integer   id;
    private String    rutOficial;
    private String    accion;
    private LocalDate fecha;
    private String    msOrigen;
}
