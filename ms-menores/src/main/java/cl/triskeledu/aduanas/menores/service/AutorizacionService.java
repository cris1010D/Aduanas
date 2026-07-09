package cl.triskeledu.aduanas.menores.service;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.mapper.AutorizacionMapper;
import cl.triskeledu.aduanas.menores.model.Autorizacion;
import cl.triskeledu.aduanas.menores.repository.AutorizacionRepository;
import cl.triskeledu.aduanas.menores.repository.MenorRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutorizacionService {

    private final AutorizacionRepository autorizacionRepository;
    private final MenorRepository menorRepository;
    private final AutorizacionMapper autorizacionMapper;

    public List<AutorizacionResponse> listarTodas() {
        log.info("Listando todas las autorizaciones");
        return autorizacionMapper.toResponseList(autorizacionRepository.findAllByOrderByIdAsc());
    }

    public AutorizacionResponse buscarPorId(Integer id) {
        log.info("Buscando autorizacion por id: {}", id);
        return autorizacionMapper.toResponse(getAutorizacionById(id));
    }

    public List<AutorizacionResponse> listarPorMenor(String rutMenor) {
        log.info("Listando autorizaciones del menor: {}", rutMenor);
        return autorizacionMapper.toResponseList(autorizacionRepository.findByRutMenor(rutMenor));
    }

    @Transactional
    @SuppressWarnings("null")
    public AutorizacionResponse crear(AutorizacionRequest request) {
        log.info("Creando autorizacion para menor: {}", request.getRutMenor());
        if (!menorRepository.existsByRut(request.getRutMenor())) {
            throw new EntityNotFoundException("Menor", "rut", request.getRutMenor());
        }
        Autorizacion autorizacion = autorizacionMapper.toEntity(request);
        return autorizacionMapper.toResponse(autorizacionRepository.save(autorizacion));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando autorizacion id: {}", id);
        autorizacionRepository.delete(getAutorizacionById(id));
    }

    @SuppressWarnings("null")
    private Autorizacion getAutorizacionById(Integer id) {
        return autorizacionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Autorizacion", "id", id));
    }
}
