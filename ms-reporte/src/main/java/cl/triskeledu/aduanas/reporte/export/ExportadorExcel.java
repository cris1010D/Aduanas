package cl.triskeledu.aduanas.reporte.export;

import cl.triskeledu.aduanas.reporte.dto.LogEventoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementacion Strategy para exportacion en formato Excel (.xlsx).
 *
 * Usa Apache POI XSSFWorkbook para generar un libro de calculo con:
 *  - Hoja "Resumen"  : datos globales del reporte (oficial, fecha, total)
 *  - Hoja "Detalle"  : tabla completa de eventos con cabeceras resaltadas
 *
 * Registro Spring: @Component → Spring inyecta esta instancia en
 * List<ReporteExportador> dentro de ReporteExportService.
 */
@Slf4j
@Component
public class ExportadorExcel implements ReporteExportador {

    private static final String FORMATO = "EXCEL";
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    // Columnas de la hoja Detalle
    private static final String[] CABECERAS = {"ID", "RUT Oficial", "Accion", "Fecha", "MS Origen"};

    @Override
    public byte[] exportar(List<LogEventoResponse> logs, String rutOficial, LocalDate fechaGeneracion) {
        log.info("[ExportadorExcel] Generando XLSX con {} registros para oficial {}", logs.size(), rutOficial);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // ── Estilos ─────────────────────────────────────────────────────────
            XSSFCellStyle estiloTitulo    = crearEstiloTitulo(workbook);
            XSSFCellStyle estiloHeader    = crearEstiloHeader(workbook);
            XSSFCellStyle estiloCeldaPar  = crearEstiloCelda(workbook, false);
            XSSFCellStyle estiloCeldaImpar= crearEstiloCelda(workbook, true);
            XSSFCellStyle estiloMeta      = crearEstiloMeta(workbook);

            // ── Hoja 1: Resumen ──────────────────────────────────────────────────
            construirHojaResumen(workbook, estiloTitulo, estiloMeta,
                    rutOficial, fechaGeneracion, logs.size());

            // ── Hoja 2: Detalle ──────────────────────────────────────────────────
            construirHojaDetalle(workbook, estiloTitulo, estiloHeader,
                    estiloCeldaPar, estiloCeldaImpar, logs, fechaGeneracion);

            workbook.write(baos);
            log.info("[ExportadorExcel] XLSX generado: {} bytes", baos.size());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("[ExportadorExcel] Error al generar Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar reporte Excel: " + e.getMessage(), e);
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Hoja Resumen
    // ────────────────────────────────────────────────────────────────────────────

    private void construirHojaResumen(XSSFWorkbook wb, XSSFCellStyle titulo,
                                      XSSFCellStyle meta,
                                      String rutOficial, LocalDate fechaGeneracion, int total) {
        XSSFSheet sheet = wb.createSheet("Resumen");
        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 7000);

        // Fila 0: titulo del sistema
        XSSFRow fTitulo = sheet.createRow(0);
        XSSFCell cTitulo = fTitulo.createCell(0);
        cTitulo.setCellValue("SISTEMA DE CONTROL FRONTERIZO - ADUANAS CHILE");
        cTitulo.setCellStyle(titulo);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        fTitulo.setHeightInPoints(22);

        // Fila 1: subtitulo
        XSSFRow fSub = sheet.createRow(1);
        XSSFCell cSub = fSub.createCell(0);
        cSub.setCellValue("Reporte de Trazabilidad de Movimientos Fronterizos");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));

        sheet.createRow(2); // separador

        // Filas 3-5: metadatos clave-valor
        agregarFilaMeta(sheet, 3, "Oficial solicitante",  rutOficial,          meta);
        agregarFilaMeta(sheet, 4, "Fecha de generacion",  fechaGeneracion.format(FMT_FECHA), meta);
        agregarFilaMeta(sheet, 5, "Total de registros",   String.valueOf(total), meta);
        agregarFilaMeta(sheet, 6, "Estado del reporte",   "GENERADO", meta);
    }

    private void agregarFilaMeta(XSSFSheet sheet, int rowNum,
                                  String clave, String valor, XSSFCellStyle style) {
        XSSFRow row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(clave + ":");
        XSSFCell cell = row.createCell(1);
        cell.setCellValue(valor);
        cell.setCellStyle(style);
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Hoja Detalle
    // ────────────────────────────────────────────────────────────────────────────

    private void construirHojaDetalle(XSSFWorkbook wb, XSSFCellStyle titulo,
                                      XSSFCellStyle header,
                                      XSSFCellStyle par, XSSFCellStyle impar,
                                      List<LogEventoResponse> logs,
                                      LocalDate fechaGeneracion) {
        XSSFSheet sheet = wb.createSheet("Detalle");

        // Anchos de columna (en unidades de 1/256 de caracter)
        int[] anchos = {2000, 4500, 10000, 3500, 4500};
        for (int i = 0; i < anchos.length; i++) {
            sheet.setColumnWidth(i, anchos[i]);
        }

        // Fila 0: titulo
        XSSFRow fTitulo = sheet.createRow(0);
        XSSFCell cTitulo = fTitulo.createCell(0);
        cTitulo.setCellValue("Eventos de Trazabilidad - " + fechaGeneracion.format(FMT_FECHA));
        cTitulo.setCellStyle(titulo);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, CABECERAS.length - 1));
        fTitulo.setHeightInPoints(18);

        sheet.createRow(1); // separador

        // Fila 2: cabeceras
        XSSFRow fHeader = sheet.createRow(2);
        fHeader.setHeightInPoints(16);
        for (int i = 0; i < CABECERAS.length; i++) {
            XSSFCell cell = fHeader.createCell(i);
            cell.setCellValue(CABECERAS[i]);
            cell.setCellStyle(header);
        }

        // Auto-filter en cabecera
        sheet.setAutoFilter(new CellRangeAddress(2, 2, 0, CABECERAS.length - 1));

        // Filas de datos
        for (int i = 0; i < logs.size(); i++) {
            LogEventoResponse log = logs.get(i);
            XSSFRow row = sheet.createRow(i + 3);
            XSSFCellStyle estilo = (i % 2 == 0) ? par : impar;

            XSSFCell cId = row.createCell(0);
            cId.setCellValue(log.getId() != null ? log.getId() : 0);
            cId.setCellStyle(estilo);

            crearCeldaString(row, 1, log.getRutOficial(), estilo);
            crearCeldaString(row, 2, log.getAccion(),     estilo);
            crearCeldaString(row, 3,
                    log.getFecha() != null ? log.getFecha().format(FMT_FECHA) : "-", estilo);
            crearCeldaString(row, 4, log.getMsOrigen(), estilo);
        }

        // Fila de totales
        int totalRow = logs.size() + 3;
        XSSFRow fTotal = sheet.createRow(totalRow);
        XSSFCellStyle estiloTotal = (XSSFCellStyle) header.copy();
        XSSFCell cTotal = fTotal.createCell(0);
        cTotal.setCellValue("TOTAL: " + logs.size() + " registros");
        cTotal.setCellStyle(estiloTotal);
        sheet.addMergedRegion(new CellRangeAddress(totalRow, totalRow, 0, CABECERAS.length - 1));
    }

    private void crearCeldaString(XSSFRow row, int col, String valor, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(col);
        cell.setCellValue(valor != null ? valor : "-");
        cell.setCellStyle(style);
    }

    // ────────────────────────────────────────────────────────────────────────────
    // Fabrica de estilos
    // ────────────────────────────────────────────────────────────────────────────

    private XSSFCellStyle crearEstiloTitulo(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 0, (byte) 51, (byte) 102}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private XSSFCellStyle crearEstiloHeader(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 0, (byte) 102, (byte) 153}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private XSSFCellStyle crearEstiloCelda(XSSFWorkbook wb, boolean sombreado) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 9);
        style.setFont(font);
        if (sombreado) {
            style.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 235, (byte) 241, (byte) 250}, null));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return style;
    }

    private XSSFCellStyle crearEstiloMeta(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }

    @Override
    public String getFormato() {
        return FORMATO;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String getFileExtension() {
        return "xlsx";
    }
}
