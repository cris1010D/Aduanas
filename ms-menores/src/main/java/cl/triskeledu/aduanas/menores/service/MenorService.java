package cl.triskeledu.aduanas.menores.service;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.event.MenorEventProducer;
import cl.triskeledu.aduanas.menores.mapper.MenorMapper;
import cl.triskeledu.aduanas.menores.model.Menor;
import cl.triskeledu.aduanas.menores.repository.MenorRepository;
import cl.triskeledu.common.event.MenorCreatedEvent;
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
public class MenorService {

    private final MenorRepository menorRepository;
    private final MenorMapper menorMapper;
    private final MenorEventProducer menorEventProducer;

    public List<MenorResponse> listarTodos() {
        log.info("Listando todos los menores");
        return menorMapper.toResponseList(menorRepository.findAllByOrderByIdAsc());
    }

    public MenorResponse buscarPorId(Integer id) {
        log.info("Buscando menor por id: {}", id);
        return menorMapper.toResponse(getMenorById(id));
    }

    public MenorResponse buscarPorRut(String rut) {
        log.info("Buscando menor por rut: {}", rut);
        return menorMapper.toResponse(
            menorRepository.findByRut(rut)
                .orElseThrow(() -> new EntityNotFoundException("Menor", "rut", rut))
        );
    }

    public List<MenorResponse> listarPorTutor(String rutTutor) {
        log.info("Listando menores del tutor: {}", rutTutor);
        return menorMapper.toResponseList(menorRepository.findByRutTutor(rutTutor));
    }

    @Transactional
    @SuppressWarnings("null")
    public MenorResponse crear(MenorRequest request) {
        log.info("Creando menor con rut: {}", request.getRut());
        menorRepository.findByRut(request.getRut()).ifPresent(existente -> {
            throw new DuplicateResourceException("Menor", "rut", request.getRut(), existente.getNombre());
        });
        Menor menor = menorMapper.toEntity(request);
        Menor guardado = menorRepository.save(menor);
        menorEventProducer.sendMenorCreated(
            MenorCreatedEvent.builder()
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .fechaNac(guardado.getFechaNac())
                .rutTutor(guardado.getRutTutor())
                .build()
        );
        return menorMapper.toResponse(guardado);
    }

    @Transactional
    public MenorResponse actualizar(Integer id, MenorRequest request) {
        log.info("Actualizando menor id: {}", id);
        Menor menor = getMenorById(id);
        if (!menor.getRut().equals(request.getRut()) && menorRepository.existsByRut(request.getRut())) {
            throw new DuplicateResourceException("Menor", "rut", request.getRut(), request.getRut());
        }
        menorMapper.updateEntity(request, menor);
        return menorMapper.toResponse(menorRepository.save(menor));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando menor id: {}", id);
        menorRepository.delete(getMenorById(id));
    }

    @SuppressWarnings("null")
    private Menor getMenorById(Integer id) {
        return menorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menor", "id", id));
    }
}
