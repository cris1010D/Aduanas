package cl.triskeledu.aduanas.pdi.service;

import cl.triskeledu.aduanas.pdi.dto.AntecedenteRequest;
import cl.triskeledu.aduanas.pdi.dto.AntecedenteResponse;
import cl.triskeledu.aduanas.pdi.event.AntecedenteEventProducer;
import cl.triskeledu.aduanas.pdi.mapper.AntecedenteMapper;
import cl.triskeledu.aduanas.pdi.model.Antecedente;
import cl.triskeledu.aduanas.pdi.repository.AntecedenteRepository;
import cl.triskeledu.common.event.AntecedenteCreatedEvent;
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
public class AntecedenteService {

    private final AntecedenteRepository antecedenteRepository;
    private final AntecedenteMapper antecedenteMapper;
    private final AntecedenteEventProducer antecedenteEventProducer;

    public List<AntecedenteResponse> listarTodos() {
        log.info("Listando todos los antecedentes");
        return antecedenteMapper.toResponseList(antecedenteRepository.findAllByOrderByIdAsc());
    }

    public AntecedenteResponse buscarPorId(Integer id) {
        log.info("Buscando antecedente por id: {}", id);
        return antecedenteMapper.toResponse(getAntecedenteById(id));
    }

    public AntecedenteResponse buscarPorRut(String rut) {
        log.info("Buscando antecedente por rut: {}", rut);
        return antecedenteMapper.toResponse(
            antecedenteRepository.findByRut(rut)
                .orElseThrow(() -> new EntityNotFoundException("Antecedente", "rut", rut))
        );
    }

    @Transactional
    @SuppressWarnings("null")
    public AntecedenteResponse crear(AntecedenteRequest request) {
        log.info("Creando antecedente para rut: {}", request.getRut());
        if (antecedenteRepository.existsByRut(request.getRut())) {
            throw new DuplicateResourceException("Antecedente", "rut", request.getRut(), request.getRut());
        }
        Antecedente antecedente = antecedenteMapper.toEntity(request);
        Antecedente guardado = antecedenteRepository.save(antecedente);
        antecedenteEventProducer.sendAntecedenteCreated(
            AntecedenteCreatedEvent.builder()
                .rut(guardado.getRut())
                .resultado(guardado.getResultado())
                .fechaConsulta(guardado.getFechaConsulta())
                .fuente(guardado.getFuente())
                .build()
        );
        return antecedenteMapper.toResponse(guardado);
    }

    @Transactional
    @SuppressWarnings("null")
    public AntecedenteResponse actualizar(Integer id, AntecedenteRequest request) {
        log.info("Actualizando antecedente id: {}", id);
        Antecedente antecedente = getAntecedenteById(id);
        antecedenteMapper.updateEntity(request, antecedente);
        return antecedenteMapper.toResponse(antecedenteRepository.save(antecedente));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando antecedente id: {}", id);
        antecedenteRepository.delete(getAntecedenteById(id));
    }

    @SuppressWarnings("null")
    private Antecedente getAntecedenteById(Integer id) {
        return antecedenteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Antecedente", "id", id));
    }
}
