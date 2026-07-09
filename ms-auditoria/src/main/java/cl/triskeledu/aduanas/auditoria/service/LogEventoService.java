package cl.triskeledu.aduanas.auditoria.service;

import cl.triskeledu.aduanas.auditoria.dto.DetalleLogResponse;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoRequest;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.event.LogEventoEventProducer;
import cl.triskeledu.aduanas.auditoria.mapper.LogEventoMapper;
import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import cl.triskeledu.aduanas.auditoria.repository.DetalleLogRepository;
import cl.triskeledu.aduanas.auditoria.repository.LogEventoRepository;
import cl.triskeledu.common.event.LogEventoCreatedEvent;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogEventoService {

    private final LogEventoRepository logEventoRepository;
    private final DetalleLogRepository detalleLogRepository;
    private final LogEventoMapper logEventoMapper;
    private final LogEventoEventProducer logEventoEventProducer;

    public List<LogEventoResponse> listarTodos() {
        log.info("Listando todos los logs");
        return logEventoMapper.toResponseList(logEventoRepository.findAllByOrderByIdAsc());
    }

    public LogEventoResponse buscarPorId(Integer id) {
        log.info("Buscando log por id: {}", id);
        return logEventoMapper.toResponse(getLogById(id));
    }

    public List<LogEventoResponse> listarPorOficial(String rutOficial) {
        log.info("Listando logs del oficial: {}", rutOficial);
        return logEventoMapper.toResponseList(logEventoRepository.findByRutOficial(rutOficial));
    }

    public List<LogEventoResponse> listarPorMs(String msOrigen) {
        log.info("Listando logs del MS: {}", msOrigen);
        return logEventoMapper.toResponseList(logEventoRepository.findByMsOrigen(msOrigen));
    }

    @Transactional
    @SuppressWarnings("null")
    public LogEventoResponse crear(LogEventoRequest request) {
        log.info("Registrando log de accion: {} por oficial: {}", request.getAccion(), request.getRutOficial());
        LogEvento logEvento = logEventoMapper.toEntity(request);
        LogEvento guardado = logEventoRepository.save(logEvento);
        logEventoEventProducer.sendLogEventoCreated(
            LogEventoCreatedEvent.builder()
                .rutOficial(guardado.getRutOficial())
                .accion(guardado.getAccion())
                .fecha(guardado.getFecha())
                .msOrigen(guardado.getMsOrigen())
                .build()
        );
        return logEventoMapper.toResponse(guardado);
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando log id: {}", id);
        logEventoRepository.delete(getLogById(id));
    }

    @SuppressWarnings("null")
    private LogEvento getLogById(Integer id) {
        return logEventoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LogEvento", "id", id));
    }

    // -------------------------------------------------------
    // R.14 — Consulta de detalles de un log (inmutables)
    // -------------------------------------------------------

    /**
     * Retorna los registros detalle_log asociados a un log_evento.
     * Verifica primero que el log exista (lanza 404 si no).
     */
    public List<DetalleLogResponse> listarDetallesPorLog(Integer idLog) {
        log.info("Consultando detalles del log id: {}", idLog);
        getLogById(idLog); // valida existencia del log padre
        return detalleLogRepository.findByIdLog(idLog)
            .stream()
            .map(d -> DetalleLogResponse.builder()
                .id(d.getId())
                .idLog(d.getIdLog())
                .entidad(d.getEntidad())
                .campo(d.getCampo())
                .valorNuevo(d.getValorNuevo())
                .build())
            .toList();
    }
}
