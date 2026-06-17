package cl.triskeledu.aduanas.notaria.service;

import cl.triskeledu.aduanas.notaria.dto.PoderRequest;
import cl.triskeledu.aduanas.notaria.dto.PoderResponse;
import cl.triskeledu.aduanas.notaria.event.PoderEventProducer;
import cl.triskeledu.aduanas.notaria.mapper.PoderMapper;
import cl.triskeledu.aduanas.notaria.model.Poder;
import cl.triskeledu.aduanas.notaria.repository.PoderRepository;
import cl.triskeledu.common.event.PoderCreatedEvent;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoderService {

    private final PoderRepository poderRepository;
    private final PoderMapper poderMapper;
    private final PoderEventProducer poderEventProducer;

    public List<PoderResponse> listarTodos() {
        log.info("Listando todos los poderes");
        return poderMapper.toResponseList(poderRepository.findAllByOrderByIdAsc());
    }

    public PoderResponse buscarPorId(Integer id) {
        log.info("Buscando poder por id: {}", id);
        return poderMapper.toResponse(getPoderById(id));
    }

    public List<PoderResponse> listarPorTitular(String rutTitular) {
        log.info("Listando poderes del titular: {}", rutTitular);
        return poderMapper.toResponseList(poderRepository.findByRutTitular(rutTitular));
    }

    @Transactional
    @SuppressWarnings("null")
    public PoderResponse crear(PoderRequest request) {
        log.info("Creando poder para titular: {}", request.getRutTitular());
        Poder poder = poderMapper.toEntity(request);
        Poder guardado = poderRepository.save(poder);
        poderEventProducer.sendPoderCreated(
            PoderCreatedEvent.builder()
                .rutTitular(guardado.getRutTitular())
                .rutApoderado(guardado.getRutApoderado())
                .notariaOrigen(guardado.getNotariaOrigen())
                .vigencia(guardado.getVigencia())
                .build()
        );
        return poderMapper.toResponse(guardado);
    }

    @Transactional
    @SuppressWarnings("null")
    public PoderResponse actualizar(Integer id, PoderRequest request) {
        log.info("Actualizando poder id: {}", id);
        Poder poder = getPoderById(id);
        poderMapper.updateEntity(request, poder);
        return poderMapper.toResponse(poderRepository.save(poder));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando poder id: {}", id);
        poderRepository.delete(getPoderById(id));
    }

    @SuppressWarnings("null")
    private Poder getPoderById(Integer id) {
        return poderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Poder", "id", id));
    }
}
