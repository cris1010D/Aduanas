package cl.triskeledu.aduanas.reporte.service;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.event.ReporteEventProducer;
import cl.triskeledu.aduanas.reporte.mapper.ReporteMapper;
import cl.triskeledu.aduanas.reporte.model.Reporte;
import cl.triskeledu.aduanas.reporte.repository.ReporteRepository;
import cl.triskeledu.common.event.ReporteCreatedEvent;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;
    private final ReporteEventProducer reporteEventProducer;

    public List<ReporteResponse> listarTodos() {
        log.info("Listando todos los reportes");
        return reporteMapper.toResponseList(reporteRepository.findAllByOrderByIdAsc());
    }

    public ReporteResponse buscarPorId(Integer id) {
        log.info("Buscando reporte por id: {}", id);
        return reporteMapper.toResponse(getReporteById(id));
    }

    public List<ReporteResponse> listarPorOficial(String rutOficial) {
        log.info("Listando reportes del oficial: {}", rutOficial);
        return reporteMapper.toResponseList(reporteRepository.findByRutOficial(rutOficial));
    }

    @Transactional
    public ReporteResponse crear(ReporteRequest request) {
        log.info("Creando reporte tipo: {}", request.getTipo());
        Reporte reporte = reporteMapper.toEntity(request);
        Reporte guardado = reporteRepository.save(reporte);
        reporteEventProducer.sendReporteCreated(
            ReporteCreatedEvent.builder()
                .tipo(guardado.getTipo())
                .fecha(guardado.getFecha())
                .rutOficial(guardado.getRutOficial())
                .formato(guardado.getFormato())
                .build()
        );
        return reporteMapper.toResponse(guardado);
    }

    @Transactional
    public ReporteResponse actualizar(Integer id, ReporteRequest request) {
        log.info("Actualizando reporte id: {}", id);
        Reporte reporte = getReporteById(id);
        reporteMapper.updateEntity(request, reporte);
        return reporteMapper.toResponse(reporteRepository.save(reporte));
    }

    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando reporte id: {}", id);
        reporteRepository.delete(getReporteById(id));
    }

    private Reporte getReporteById(Integer id) {
        return reporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Reporte", "id", id));
    }
}
