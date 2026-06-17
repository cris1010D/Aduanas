package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.dto.ViajeroRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroResponse;
import cl.triskeledu.aduanas.proceso.event.ViajeroEventProducer;
import cl.triskeledu.aduanas.proceso.mapper.ViajeroMapper;
import cl.triskeledu.aduanas.proceso.model.Viajero;
import cl.triskeledu.aduanas.proceso.repository.ViajeroRepository;
import cl.triskeledu.common.event.ViajeroCreatedEvent;
import cl.triskeledu.common.event.ViajeroDeletedEvent;
import cl.triskeledu.common.event.ViajeroUpdatedEvent;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViajeroService {

    private final ViajeroRepository viajeroRepository;
    private final ViajeroMapper viajeroMapper;
    private final ViajeroEventProducer viajeroEventProducer;

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
        if (viajeroRepository.existsByRut(rut)) {
            throw new DuplicateResourceException("Viajero", "rut", rut, rut);
        }
    }

    private void validarPasaporteUnico(String pasaporte) {
        if (viajeroRepository.existsByPasaporte(pasaporte)) {
            throw new DuplicateResourceException("Viajero", "pasaporte", pasaporte, pasaporte);
        }
    }
}
