package cl.triskeledu.aduanas.reporte.export;

import cl.triskeledu.aduanas.reporte.dto.LogEventoResponse;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementacion Strategy para exportacion en formato PDF.
 *
 * Usa OpenPDF (fork open-source de iText 2.x) para generar un documento
 * estructurado con encabezado institucional, metadatos del reporte y tabla
 * de eventos de trazabilidad obtenidos desde ms-auditoria.
 *
 * Registro Spring: @Component → Spring inyecta esta instancia en
 * List<ReporteExportador> dentro de ReporteExportService.
 */
@Slf4j
@Component
public class ExportadorPDF implements ReporteExportador {

    private static final String FORMATO = "PDF";
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    // Colores institucionales Aduanas Chile
    private static final Color COLOR_HEADER     = new Color(0, 51, 102);   // azul institucional
    private static final Color COLOR_SUBHEADER  = new Color(0, 102, 153);  // azul claro
    private static final Color COLOR_FILA_PAR   = new Color(235, 241, 250);
    private static final Color COLOR_BLANCO     = Color.WHITE;

    // Fuentes
    private static final Font FUENTE_TITULO    = new Font(Font.HELVETICA, 16, Font.BOLD,   Color.WHITE);
    private static final Font FUENTE_SUBTITULO = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(80, 80, 80));
    private static final Font FUENTE_HEADER_COL= new Font(Font.HELVETICA,  9, Font.BOLD,   Color.WHITE);
    private static final Font FUENTE_CELDA     = new Font(Font.HELVETICA,  8, Font.NORMAL, Color.BLACK);
    private static final Font FUENTE_FOOTER    = new Font(Font.HELVETICA,  7, Font.ITALIC, new Color(120, 120, 120));

    @Override
    public byte[] exportar(List<LogEventoResponse> logs, String rutOficial, LocalDate fechaGeneracion) {
        log.info("[ExportadorPDF] Generando PDF con {} registros para oficial {}", logs.size(), rutOficial);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 50, 40);
            PdfWriter.getInstance(document, baos);
            document.open();

            // ── Encabezado ─────────────────────────────────────────────────────────
            agregarEncabezado(document, rutOficial, fechaGeneracion, logs.size());

            // ── Tabla de datos ──────────────────────────────────────────────────────
            agregarTablaDatos(document, logs);

            // ── Pie de pagina ───────────────────────────────────────────────────────
            agregarPie(document, fechaGeneracion);

            document.close();
            log.info("[ExportadorPDF] PDF generado: {} bytes", baos.size());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("[ExportadorPDF] Error al generar PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar reporte PDF: " + e.getMessage(), e);
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Helpers privados
    // ────────────────────────────────────────────────────────────────────────────

    private void agregarEncabezado(Document doc, String rutOficial,
                                   LocalDate fechaGeneracion, int totalRegistros)
            throws DocumentException {

        // Banda azul con titulo
        PdfPTable banner = new PdfPTable(1);
        banner.setWidthPercentage(100);
        PdfPCell celdaBanner = new PdfPCell(new Phrase(
                "SISTEMA DE CONTROL FRONTERIZO - ADUANAS CHILE", FUENTE_TITULO));
        celdaBanner.setBackgroundColor(COLOR_HEADER);
        celdaBanner.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaBanner.setPadding(10);
        celdaBanner.setBorder(Rectangle.NO_BORDER);
        banner.addCell(celdaBanner);
        doc.add(banner);

        doc.add(new Paragraph(" "));

        // Subtitulo con metadatos
        doc.add(new Paragraph("Reporte de Trazabilidad de Movimientos Fronterizos",
                new Font(Font.HELVETICA, 12, Font.BOLD, COLOR_SUBHEADER)));
        doc.add(new Paragraph(
                "Oficial solicitante: " + rutOficial +
                "     |     Fecha de generacion: " + fechaGeneracion.format(FMT_FECHA) +
                "     |     Total de registros: " + totalRegistros,
                FUENTE_SUBTITULO));
        doc.add(new Paragraph(" "));
    }

    private void agregarTablaDatos(Document doc, List<LogEventoResponse> logs)
            throws DocumentException {

        // 5 columnas: ID, RUT Oficial, Accion, Fecha, MS Origen
        float[] anchos = {0.08f, 0.20f, 0.35f, 0.15f, 0.22f};
        PdfPTable tabla = new PdfPTable(anchos);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(4);

        // Cabecera
        String[] cabeceras = {"ID", "RUT Oficial", "Accion", "Fecha", "MS Origen"};
        for (String cab : cabeceras) {
            PdfPCell cell = new PdfPCell(new Phrase(cab, FUENTE_HEADER_COL));
            cell.setBackgroundColor(COLOR_HEADER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            cell.setBorderColor(Color.WHITE);
            tabla.addCell(cell);
        }

        // Filas de datos
        for (int i = 0; i < logs.size(); i++) {
            LogEventoResponse log = logs.get(i);
            Color fondo = (i % 2 == 0) ? COLOR_BLANCO : COLOR_FILA_PAR;

            agregarCelda(tabla, String.valueOf(log.getId()), fondo, Element.ALIGN_CENTER);
            agregarCelda(tabla, nullSafe(log.getRutOficial()), fondo, Element.ALIGN_LEFT);
            agregarCelda(tabla, nullSafe(log.getAccion()),     fondo, Element.ALIGN_LEFT);
            agregarCelda(tabla,
                    log.getFecha() != null ? log.getFecha().format(FMT_FECHA) : "-",
                    fondo, Element.ALIGN_CENTER);
            agregarCelda(tabla, nullSafe(log.getMsOrigen()), fondo, Element.ALIGN_CENTER);
        }

        doc.add(tabla);
    }

    private void agregarCelda(PdfPTable tabla, String valor, Color fondo, int alineacion) {
        PdfPCell cell = new PdfPCell(new Phrase(valor, FUENTE_CELDA));
        cell.setBackgroundColor(fondo);
        cell.setHorizontalAlignment(alineacion);
        cell.setPadding(4);
        cell.setBorderColor(new Color(200, 200, 200));
        tabla.addCell(cell);
    }

    private void agregarPie(Document doc, LocalDate fechaGeneracion) throws DocumentException {
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(
                "Documento generado automaticamente por ms-reporte el " +
                fechaGeneracion.format(FMT_FECHA) +
                ". Uso interno exclusivo del Servicio Nacional de Aduanas de Chile.",
                FUENTE_FOOTER));
    }

    private String nullSafe(String valor) {
        return valor != null ? valor : "-";
    }

    @Override
    public String getFormato() {
        return FORMATO;
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
