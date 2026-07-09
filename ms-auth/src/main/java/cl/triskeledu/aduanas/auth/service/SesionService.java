package cl.triskeledu.aduanas.auth.service;

import cl.triskeledu.aduanas.auth.dto.SesionRequest;
import cl.triskeledu.aduanas.auth.dto.SesionResponse;
import cl.triskeledu.aduanas.auth.mapper.SesionMapper;
import cl.triskeledu.aduanas.auth.model.Sesion;
import cl.triskeledu.aduanas.auth.repository.OficialRepository;
import cl.triskeledu.aduanas.auth.repository.SesionRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SesionService {

    private final SesionRepository sesionRepository;
    private final OficialRepository oficialRepository;
    private final SesionMapper sesionMapper;

    public List<SesionResponse> listarTodas() {
        log.info("Listando todas las sesiones");
        return sesionMapper.toResponseList(sesionRepository.findAllByOrderByIdAsc());
    }

    public SesionResponse buscarPorId(Integer id) {
        log.info("Buscando sesion por id: {}", id);
        return sesionMapper.toResponse(getSesionById(id));
    }

    public List<SesionResponse> listarPorOficial(String rutOficial) {
        log.info("Listando sesiones del oficial: {}", rutOficial);
        return sesionMapper.toResponseList(sesionRepository.findByRutOficial(rutOficial));
    }

    @Transactional
    @SuppressWarnings("null")
    public SesionResponse crear(SesionRequest request) {
        log.info("Creando sesion para oficial: {}", request.getRutOficial());
        if (!oficialRepository.existsByRut(request.getRutOficial())) {
            throw new EntityNotFoundException("Oficial", "rut", request.getRutOficial());
        }
        Sesion sesion = sesionMapper.toEntity(request);
        return sesionMapper.toResponse(sesionRepository.save(sesion));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando sesion id: {}", id);
        sesionRepository.delete(getSesionById(id));
    }
    @SuppressWarnings("null")
    private Sesion getSesionById(Integer id) {
        return sesionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sesion", "id", id));
    }
}
