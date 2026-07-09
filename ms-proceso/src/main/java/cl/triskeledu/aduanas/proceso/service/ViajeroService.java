package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.config.KafkaTopicConfig;
import cl.triskeledu.aduanas.proceso.dto.TramiteRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroResponse;
import cl.triskeledu.aduanas.proceso.event.ViajeroEventProducer;
import cl.triskeledu.aduanas.proceso.mapper.ViajeroMapper;
import cl.triskeledu.aduanas.proceso.model.Movimiento;
import cl.triskeledu.aduanas.proceso.model.Viajero;
import cl.triskeledu.aduanas.proceso.repository.MovimientoRepository;
import cl.triskeledu.aduanas.proceso.repository.ViajeroRepository;
import cl.triskeledu.common.event.SalidaDiplomaticaCreatedEvent;
import cl.triskeledu.common.event.ViajeroCreatedEvent;
import cl.triskeledu.common.event.ViajeroDeletedEvent;
import cl.triskeledu.common.event.ViajeroUpdatedEvent;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViajeroService {

    private final ViajeroRepository viajeroRepository;
    private final ViajeroMapper viajeroMapper;
    private final ViajeroEventProducer viajeroEventProducer;
    private final MovimientoRepository movimientoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<ViajeroResponse> listarTodos() {
        log.info("Listando todos los viajeros");
        return viajeroMapper.toResponseList(viajeroRepository.findAllByOrderByIdAsc());
    }

    public ViajeroResponse buscarPorId(Integer id) {
        log.info("Buscando viajero por id: {}", id);
        return viajeroMapper.toResponse(getViajeroById(id));
    }

    public ViajeroResponse buscarPorRut(String rut) {
        log.info("Buscando viajero por rut: {}", rut);
        return viajeroMapper.toResponse(
            viajeroRepository.findByRut(rut)
                .orElseThrow(() -> new EntityNotFoundException("Viajero", "rut", rut))
        );
    }

    @Transactional
    @SuppressWarnings("null")
    public ViajeroResponse crear(ViajeroRequest request) {
        log.info("Creando viajero con rut: {}", request.getRut());
        validarRutUnico(request.getRut());
        if (request.getPasaporte() != null) {
            validarPasaporteUnico(request.getPasaporte());
        }
        Viajero viajero = viajeroMapper.toEntity(request);
        Viajero guardado = viajeroRepository.save(viajero);
        viajeroEventProducer.sendViajeroCreated(
            ViajeroCreatedEvent.builder()
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .pasaporte(guardado.getPasaporte())
                .nacionalidad(guardado.getNacionalidad())
                .build()
        );
        return viajeroMapper.toResponse(guardado);
    }

    @Transactional
    public ViajeroResponse actualizar(Integer id, ViajeroRequest request) {
        log.info("Actualizando viajero id: {}", id);
        Viajero viajero = getViajeroById(id);
        if (!viajero.getRut().equals(request.getRut())) {
            validarRutUnico(request.getRut());
        }
        viajeroMapper.updateEntity(request, viajero);
        Viajero actualizado = viajeroRepository.save(viajero);
        viajeroEventProducer.sendViajeroUpdated(
            ViajeroUpdatedEvent.builder()
                .rut(actualizado.getRut())
                .nombre(actualizado.getNombre())
                .pasaporte(actualizado.getPasaporte())
                .nacionalidad(actualizado.getNacionalidad())
                .build()
        );
        return viajeroMapper.toResponse(actualizado);
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando viajero id: {}", id);
        Viajero viajero = getViajeroById(id);
        viajeroRepository.delete(viajero);
        viajeroEventProducer.sendViajeroDeleted(
            ViajeroDeletedEvent.builder()
                .rut(viajero.getRut())
                .build()
        );
    }
    @SuppressWarnings("null")
    private Viajero getViajeroById(Integer id) {
        return viajeroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Viajero", "id", id));
    }
    //@SuppressWarnings("null")
    private void validarRutUnico(String rut) {
        viajeroRepository.findByRut(rut).ifPresent(existente -> {
            throw new DuplicateResourceException("Viajero", "rut", rut, existente.getNombre());
        });
    }

    private void validarPasaporteUnico(String pasaporte) {
        if (viajeroRepository.existsByPasaporte(pasaporte)) {
            throw new DuplicateResourceException("Viajero", "pasaporte", pasaporte, pasaporte);
        }
    }

    // -------------------------------------------------------
    // SALIDA DIPLOMATICA
    // -------------------------------------------------------

    private static final Map<String, String> EXENCIONES = new HashMap<>();

    static {
        EXENCIONES.put("C.D.", "Cuerpo Diplomatico - exencion total de control aduanero");
        EXENCIONES.put("CC",   "Consulado - exencion parcial de control aduanero");
        EXENCIONES.put("O.I.", "Organizacion Internacional - exencion por convenio multilateral");
        EXENCIONES.put("P.A.T.", "Privilegio Acuerdo Tratado - exencion por tratado bilateral");
    }

    private void validarInmunidad(String tipoAcreditacion) {
        if (tipoAcreditacion == null || !EXENCIONES.containsKey(tipoAcreditacion)) {
            throw new EntityNotFoundException(
                "TipoAcreditacion", "codigo", tipoAcreditacion != null ? tipoAcreditacion : "null"
            );
        }
        log.info("Inmunidad validada para tipo: {} — {}", tipoAcreditacion, EXENCIONES.get(tipoAcreditacion));
    }

    @Transactional
    @SuppressWarnings("null")
    public ViajeroResponse procesarSalidaDiplomatica(TramiteRequest request) {
        log.info("Procesando salida diplomatica para rut: {}", request.getRutViajero());

        // 1. Busca el viajero por rut
        Viajero viajero = viajeroRepository.findByRut(request.getRutViajero())
            .orElseThrow(() -> new EntityNotFoundException("Viajero", "rut", request.getRutViajero()));

        // 2. Valida inmunidad diplomatica
        validarInmunidad(request.getTipoAcreditacion());

        // 3. Registra el movimiento EGRESO_DIPLOMATICO
        Movimiento movimiento = Movimiento.builder()
            .rutViajero(viajero.getRut())
            .tipo("EGRESO_DIPLOMATICO")
            .fecha(LocalDate.parse(request.getFecha()))
            .pasoFronterizo(request.getPasoFronterizo())
            .build();
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        // 4. Publica evento Kafka
        SalidaDiplomaticaCreatedEvent evento = SalidaDiplomaticaCreatedEvent.builder()
            .id(movimientoGuardado.getId())
            .rutViajero(viajero.getRut())
            .tipoAcreditacion(request.getTipoAcreditacion())
            .pasoFronterizo(request.getPasoFronterizo())
            .fecha(request.getFecha())
            .build();
        log.info("Publicando SalidaDiplomaticaCreatedEvent para rut: {}", viajero.getRut());
        kafkaTemplate.send(KafkaTopicConfig.SALIDA_DIPLOMATICA_TOPIC, viajero.getRut(), evento);
        log.info("SalidaDiplomaticaCreatedEvent publicado correctamente");

        // 5. Retorna ViajeroResponse
        return viajeroMapper.toResponse(viajero);
    }
}
