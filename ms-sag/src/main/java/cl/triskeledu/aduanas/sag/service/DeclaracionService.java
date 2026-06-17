package cl.triskeledu.aduanas.sag.service;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.event.DeclaracionEventProducer;
import cl.triskeledu.aduanas.sag.mapper.DeclaracionMapper;
import cl.triskeledu.aduanas.sag.model.Declaracion;
import cl.triskeledu.aduanas.sag.repository.DeclaracionRepository;
import cl.triskeledu.common.event.DeclaracionCreatedEvent;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeclaracionService {

    private final DeclaracionRepository declaracionRepository;
    private final DeclaracionMapper declaracionMapper;
    private final DeclaracionEventProducer declaracionEventProducer;

    public List<DeclaracionResponse> listarTodas() {
        log.info("Listando todas las declaraciones");
        return declaracionMapper.toResponseList(declaracionRepository.findAllByOrderByIdAsc());
    }

    public DeclaracionResponse buscarPorId(Integer id) {
        log.info("Buscando declaracion por id: {}", id);
        return declaracionMapper.toResponse(getDeclaracionById(id));
    }

    public List<DeclaracionResponse> listarPorViajero(String rutViajero) {
        log.info("Listando declaraciones del viajero: {}", rutViajero);
        return declaracionMapper.toResponseList(declaracionRepository.findByRutViajero(rutViajero));
    }

    @Transactional
    @SuppressWarnings("null")
    public DeclaracionResponse crear(DeclaracionRequest request) {
        log.info("Creando declaracion para viajero: {}", request.getRutViajero());
        Declaracion declaracion = declaracionMapper.toEntity(request);
        Declaracion guardada = declaracionRepository.save(declaracion);
        declaracionEventProducer.sendDeclaracionCreated(
            DeclaracionCreatedEvent.builder()
                .rutViajero(guardada.getRutViajero())
                .fecha(guardada.getFecha())
                .estado(guardada.getEstado())
                .pasoFronterizo(guardada.getPasoFronterizo())
                .build()
        );
        return declaracionMapper.toResponse(guardada);
    }

    @Transactional
    @SuppressWarnings("null")
    public DeclaracionResponse actualizar(Integer id, DeclaracionRequest request) {
        log.info("Actualizando declaracion id: {}", id);
        Declaracion declaracion = getDeclaracionById(id);
        declaracionMapper.updateEntity(request, declaracion);
        return declaracionMapper.toResponse(declaracionRepository.save(declaracion));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando declaracion id: {}", id);
        declaracionRepository.delete(getDeclaracionById(id));
    }

    @SuppressWarnings("null")
    private Declaracion getDeclaracionById(Integer id) {
        return declaracionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Declaracion", "id", id));
    }
}
