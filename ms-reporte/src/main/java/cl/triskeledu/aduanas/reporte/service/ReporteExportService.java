package cl.triskeledu.aduanas.reporte.service;

import cl.triskeledu.aduanas.reporte.client.AuditoriaClient;
import cl.triskeledu.aduanas.reporte.dto.LogEventoResponse;
import cl.triskeledu.aduanas.reporte.event.ReporteEventProducer;
import cl.triskeledu.aduanas.reporte.export.ReporteExportador;
import cl.triskeledu.aduanas.reporte.model.Reporte;
import cl.triskeledu.aduanas.reporte.repository.ReporteRepository;
import cl.triskeledu.common.event.ReporteCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio orquestador de exportacion de reportes (R.7 / R.8).
 *
 * Flujo por peticion de exportacion:
 *   1. Obtiene los logs de trazabilidad desde ms-auditoria via Feign.
 *      Filtra por rutOficial si se proporcionó el parametro.
 *   2. Resuelve la implementacion Strategy correspondiente al formato solicitado
 *      (PDF → ExportadorPDF, EXCEL → ExportadorExcel).
 *   3. Invoca exportar() → bytes del archivo.
 *   4. Persiste una entrada Reporte en PostgreSQL (tipo, fecha, rutOficial, formato).
 *   5. Publica ReporteCreatedEvent en Kafka (topic: reporte.reporte.created)
 *      para trazabilidad de auditoria.
 *   6. Retorna ResponseEntity<byte[]> con headers Content-Type y Content-Disposition.
 *
 * RBAC: este servicio es invocado exclusivamente desde endpoints protegidos con
 * hasRole("SUPERVISOR") definidos en SecurityConfig y ReporteController.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteExportService {

    /** Todas las implementaciones Strategy inyectadas automaticamente por Spring. */
    private final List<ReporteExportador> exportadores;

    private final AuditoriaClient      auditoriaClient;
    private final ReporteRepository    reporteRepository;
    private final ReporteEventProducer reporteEventProducer;

    /**
     * Genera y retorna un reporte en el formato indicado.
     *
     * @param formato     "PDF" o "EXCEL" (case-insensitive)
     * @param rutOficial  RUT del supervisor solicitante; si es null devuelve todos los logs
     * @return ResponseEntity con bytes del archivo y headers HTTP correctos
     */
    @Transactional
    public ResponseEntity<byte[]> exportar(String formato, String rutOficial) {
        log.info("[ReporteExportService] Solicitando exportacion formato={} oficial={}", formato, rutOficial);

        // 1. Obtener logs desde ms-auditoria
        List<LogEventoResponse> logs = obtenerLogs(rutOficial);
        log.info("[ReporteExportService] {} registros obtenidos de ms-auditoria", logs.size());

        // 2. Resolver estrategia
        ReporteExportador exportador = resolverExportador(formato);

        // 3. Generar bytes
        LocalDate hoy = LocalDate.now();
        byte[] contenido = exportador.exportar(logs,
                rutOficial != null ? rutOficial : "TODOS", hoy);

        // 4. Persistir registro del reporte generado
        Reporte reporte = Reporte.builder()
                .tipo("TRAZABILIDAD")
                .fecha(hoy)
                .rutOficial(rutOficial != null ? rutOficial : "TODOS")
                .formato(exportador.getFormato())
                .build();
        Reporte guardado = reporteRepository.save(reporte);
        log.info("[ReporteExportService] Reporte persistido con id={}", guardado.getId());

        // 5. Publicar evento Kafka
        reporteEventProducer.sendReporteCreated(
            ReporteCreatedEvent.builder()
                .tipo(guardado.getTipo())
                .fecha(guardado.getFecha())
                .rutOficial(guardado.getRutOficial())
                .formato(guardado.getFormato())
                .build()
        );
        log.info("[ReporteExportService] ReporteCreatedEvent publicado en Kafka");

        // 6. Construir respuesta HTTP con archivo adjunto
        String nombreArchivo = construirNombreArchivo(formato, rutOficial, hoy);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(exportador.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nombreArchivo + "\"")
                .body(contenido);
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Helpers privados
    // ────────────────────────────────────────────────────────────────────────────

    /**
     * Obtiene logs de ms-auditoria filtrando por oficial si se indico.
     * Ante cualquier error de comunicacion Feign lanza RuntimeException
     * con mensaje claro para el llamador.
     */
    private List<LogEventoResponse> obtenerLogs(String rutOficial) {
        try {
            if (rutOficial != null && !rutOficial.isBlank()) {
                log.info("[ReporteExportService] Filtrando logs por oficial: {}", rutOficial);
                return auditoriaClient.listarPorOficial(rutOficial);
            }
            return auditoriaClient.listarTodos();
        } catch (Exception e) {
            log.error("[ReporteExportService] Error al obtener logs de ms-auditoria: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los registros de auditoria: " + e.getMessage(), e);
        }
    }

    /**
     * Busca en la lista de estrategias inyectadas la que coincide con el formato.
     * La comparacion es case-insensitive para mayor robustez.
     */
    private ReporteExportador resolverExportador(String formato) {
        return exportadores.stream()
                .filter(e -> e.getFormato().equalsIgnoreCase(formato))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("[ReporteExportService] Formato no soportado: {}", formato);
                    return new IllegalArgumentException(
                            "Formato de exportacion no soportado: " + formato +
                            ". Formatos disponibles: PDF, EXCEL");
                });
    }

    /**
     * Construye el nombre del archivo descargable incluyendo fecha y oficial.
     * Ejemplo: reporte_TRAZABILIDAD_12345678-9_2024-11-15.pdf
     */
    private String construirNombreArchivo(String formato, String rutOficial, LocalDate fecha) {
        String extension = resolverExportador(formato).getFileExtension();
        String rut = (rutOficial != null && !rutOficial.isBlank()) ? rutOficial : "TODOS";
        // Sanitizar: reemplazar caracteres no validos en nombres de archivo
        rut = rut.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        return "reporte_TRAZABILIDAD_" + rut + "_" + fecha + "." + extension;
    }
}
