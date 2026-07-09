package cl.triskeledu.aduanas.pdi.service;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.mapper.ConsultaMapper;
import cl.triskeledu.aduanas.pdi.model.Consulta;
import cl.triskeledu.aduanas.pdi.repository.AntecedenteRepository;
import cl.triskeledu.aduanas.pdi.repository.ConsultaRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final AntecedenteRepository antecedenteRepository;
    private final ConsultaMapper consultaMapper;

    public List<ConsultaResponse> listarTodas() {
        log.info("Listando todas las consultas");
        return consultaMapper.toResponseList(consultaRepository.findAllByOrderByIdAsc());
    }

    public ConsultaResponse buscarPorId(Integer id) {
        log.info("Buscando consulta por id: {}", id);
        return consultaMapper.toResponse(getConsultaById(id));
    }

    public List<ConsultaResponse> listarPorRut(String rut) {
        log.info("Listando consultas para rut: {}", rut);
        return consultaMapper.toResponseList(consultaRepository.findByRut(rut));
    }

    @Transactional
    @SuppressWarnings("null")
    public ConsultaResponse crear(ConsultaRequest request) {
        log.info("Creando consulta para rut: {}", request.getRut());
        if (!antecedenteRepository.existsByRut(request.getRut())) {
            throw new EntityNotFoundException("Antecedente", "rut", request.getRut());
        }
        Consulta consulta = consultaMapper.toEntity(request);
        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando consulta id: {}", id);
        consultaRepository.delete(getConsultaById(id));
    }

    @SuppressWarnings("null")
    private Consulta getConsultaById(Integer id) {
        return consultaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Consulta", "id", id));
    }
}
