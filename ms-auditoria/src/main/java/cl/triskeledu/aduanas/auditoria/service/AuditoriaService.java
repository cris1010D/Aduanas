package cl.triskeledu.aduanas.auditoria.service;

import cl.triskeledu.aduanas.auditoria.model.DetalleLog;
import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import cl.triskeledu.aduanas.auditoria.repository.DetalleLogRepository;
import cl.triskeledu.aduanas.auditoria.repository.LogEventoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio interno de ms-auditoria para persistir eventos recibidos via Kafka.
 *
 * Escribe directamente a los repositorios (no llama a LogEventoService.crear())
 * para evitar que se publique un nuevo evento Kafka y se genere un ciclo infinito:
 *   Kafka → AuditoriaService → LogEventoService.crear() → Kafka → AuditoriaService → ...
 *
 * Garantia de inmutabilidad: este servicio es llamado EXCLUSIVAMENTE por
 * AuditoriaEventListener. Ningun endpoint REST escribe en log_evento ni detalle_log.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final LogEventoRepository logEventoRepository;
    private final DetalleLogRepository detalleLogRepository;

    /**
     * Persiste un LogEvento y todos sus DetalleLog asociados en una sola transaccion.
     *
     * @param rutOficial RUT del oficial que origino la accion, o "SISTEMA" si no aplica
     * @param accion     Descripcion de la accion auditada (max 60 chars)
     * @param msOrigen   Nombre del microservicio que publico el evento (max 30 chars)
     * @param detalles   Lista de cambios: entidad + campo + valorNuevo (idLog se asigna aqui)
     */
    @Transactional
    @SuppressWarnings("null")
    public void registrarEvento(String rutOficial, String accion, String msOrigen,
                                List<DetalleLog> detalles) {
        LogEvento logEvento = LogEvento.builder()
            .rutOficial(truncarRut(rutOficial))
            .accion(accion)
            .fecha(LocalDate.now())
            .msOrigen(msOrigen)
            .build();

        LogEvento guardado = logEventoRepository.save(logEvento);

        detalles.forEach(detalle -> {
            detalle.setIdLog(guardado.getId());
            detalleLogRepository.save(detalle);
        });

        log.info("Auditoria registrada — id: {} | accion: {} | ms: {} | detalles: {}",
            guardado.getId(), accion, msOrigen, detalles.size());
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------

    /** Construye un DetalleLog sin idLog (se asigna en registrarEvento). */
    public static DetalleLog detalle(String entidad, String campo, String valor) {
        return DetalleLog.builder()
            .entidad(entidad)
            .campo(campo)
            .valorNuevo(truncarValor(valor))
            .build();
    }

    /** Trunca a 80 chars (limite de la columna valor_nuevo). */
    private static String truncarValor(String valor) {
        if (valor == null) return "null";
        return valor.length() > 80 ? valor.substring(0, 77) + "..." : valor;
    }

    /** Trunca a 12 chars (limite de la columna rut_oficial) y fallback a "SISTEMA". */
    private static String truncarRut(String rut) {
        if (rut == null || rut.isBlank()) return "SISTEMA";
        return rut.length() > 12 ? rut.substring(0, 12) : rut;
    }
}
