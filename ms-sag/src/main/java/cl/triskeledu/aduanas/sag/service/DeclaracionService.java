package cl.triskeledu.aduanas.sag.service;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.dto.TramiteSagRequest;
import cl.triskeledu.aduanas.sag.event.DeclaracionEventProducer;
import cl.triskeledu.aduanas.sag.mapper.DeclaracionMapper;
import cl.triskeledu.aduanas.sag.model.Declaracion;
import cl.triskeledu.aduanas.sag.model.ItemDeclaracion;
import cl.triskeledu.aduanas.sag.repository.DeclaracionRepository;
import cl.triskeledu.aduanas.sag.repository.ItemDeclaracionRepository;
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
    private final ItemDeclaracionRepository itemDeclaracionRepository;
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
        if (declaracionRepository.existsByRutViajeroAndFechaAndPasoFronterizo(
                request.getRutViajero(), request.getFecha(), request.getPasoFronterizo())) {
            throw new cl.triskeledu.common.exception.DuplicateResourceException(
                "Declaracion SAG", "rutViajero+fecha+pasoFronterizo",
                request.getRutViajero() + " / " + request.getFecha(),
                "ya existe una declaración para este viajero en esa fecha y paso fronterizo");
        }
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

    // -------------------------------------------------------
    // R.5 — TRAMITE SAG (declaracion con items y regla CUARENTENA)
    // -------------------------------------------------------

    /**
     * Procesa una declaracion SAG con sus items sanitarios/agricolas.
     *
     * Regla de negocio R.5:
     *   Si al menos un item tiene riesgo = "ALTO"  → estado = "CUARENTENA"
     *   Si todos los items son MEDIO o BAJO         → estado = "APROBADA"
     *
     * Persiste la Declaracion, luego persiste cada ItemDeclaracion vinculado,
     * y finalmente publica DeclaracionCreatedEvent en Kafka para ms-auditoria.
     */
    @Transactional
    @SuppressWarnings("null")
    public DeclaracionResponse procesarTramite(TramiteSagRequest request) {
        log.info("Iniciando tramite SAG para viajero: {} | items: {}",
            request.getRutViajero(), request.getItems().size());

        // R.5 — Evaluar nivel de riesgo de todos los items
        boolean tieneItemAlto = request.getItems().stream()
            .anyMatch(item -> "ALTO".equalsIgnoreCase(item.getRiesgo()));
        String estadoFinal = tieneItemAlto ? "CUARENTENA" : "APROBADA";
        log.info("Evaluacion de riesgo completada. Estado resultante: {}", estadoFinal);

        // Persistir la declaracion con el estado calculado
        Declaracion declaracion = Declaracion.builder()
            .rutViajero(request.getRutViajero())
            .fecha(request.getFecha())
            .estado(estadoFinal)
            .pasoFronterizo(request.getPasoFronterizo())
            .build();
        Declaracion guardada = declaracionRepository.save(declaracion);
        log.info("Declaracion SAG persistida. Id: {} | Estado: {}", guardada.getId(), guardada.getEstado());

        // Persistir cada item vinculado a la declaracion
        request.getItems().forEach(itemReq -> {
            ItemDeclaracion item = ItemDeclaracion.builder()
                .idDeclaracion(guardada.getId())
                .descripcion(itemReq.getDescripcion())
                .cantidad(itemReq.getCantidad())
                .riesgo(itemReq.getRiesgo())
                .build();
            itemDeclaracionRepository.save(item);
            log.debug("Item SAG guardado: {} | riesgo: {}", itemReq.getDescripcion(), itemReq.getRiesgo());
        });

        // Publicar evento Kafka → ms-auditoria
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
}
