package cl.triskeledu.aduanas.reporte.client;

import cl.triskeledu.aduanas.reporte.dto.LogEventoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign Client para consumir los logs de trazabilidad desde ms-auditoria.
 *
 * ms-auditoria expone solo endpoints GET (API de solo lectura / R.14).
 * ms-reporte los consume para construir reportes de estadisticas de movimientos.
 */
@FeignClient(name = "ms-auditoria", path = "/api/v1/logs")
public interface AuditoriaClient {

    /** Todos los logs del sistema ordenados por id ascendente. */
    @GetMapping
    List<LogEventoResponse> listarTodos();

    /** Logs filtrados por RUT del oficial responsable. */
    @GetMapping("/oficial/{rut}")
    List<LogEventoResponse> listarPorOficial(@PathVariable("rut") String rut);

    /** Logs filtrados por microservicio de origen (ej: ms-proceso, ms-sag). */
    @GetMapping("/ms/{msOrigen}")
    List<LogEventoResponse> listarPorMs(@PathVariable("msOrigen") String msOrigen);
}
