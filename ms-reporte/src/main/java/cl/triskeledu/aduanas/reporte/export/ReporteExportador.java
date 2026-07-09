package cl.triskeledu.aduanas.reporte.export;

import cl.triskeledu.aduanas.reporte.dto.LogEventoResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz Strategy para la exportacion de reportes de trazabilidad.
 *
 * Cada implementacion encapsula el algoritmo de generacion de un formato
 * especifico (PDF, Excel) sin que el servicio orquestador conozca los detalles.
 *
 * Implementaciones registradas como beans Spring:
 *   - ExportadorPDF   → formato "PDF",   Content-Type: application/pdf
 *   - ExportadorExcel → formato "EXCEL", Content-Type: application/vnd.openxmlformats-...
 *
 * Patron de uso en ReporteExportService:
 *   List<ReporteExportador> exportadores; // inyectados por Spring
 *   exportadores.stream()
 *       .filter(e -> e.getFormato().equalsIgnoreCase(formato))
 *       .findFirst()
 *       .orElseThrow(...)
 *       .exportar(logs, rutOficial, fecha);
 */
public interface ReporteExportador {

    /**
     * Genera el archivo binario a partir de los logs de trazabilidad.
     *
     * @param logs           lista de eventos de auditoria obtenidos de ms-auditoria
     * @param rutOficial     RUT del supervisor que solicita el reporte
     * @param fechaGeneracion fecha ISO de generacion del documento
     * @return contenido binario del archivo (PDF o XLSX)
     */
    byte[] exportar(List<LogEventoResponse> logs, String rutOficial, LocalDate fechaGeneracion);

    /** Identificador del formato: "PDF" o "EXCEL". */
    String getFormato();

    /** MIME type para el header Content-Type de la respuesta HTTP. */
    String getContentType();

    /** Extension del archivo para el header Content-Disposition. */
    String getFileExtension();
}
