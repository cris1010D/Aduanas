package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.dto.MovimientoRequest;
import cl.triskeledu.aduanas.proceso.dto.MovimientoResponse;
import cl.triskeledu.aduanas.proceso.mapper.MovimientoMapper;
import cl.triskeledu.aduanas.proceso.model.Movimiento;
import cl.triskeledu.aduanas.proceso.repository.MovimientoRepository;
import cl.triskeledu.aduanas.proceso.repository.ViajeroRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ViajeroRepository viajeroRepository;
    private final MovimientoMapper movimientoMapper;

    public List<MovimientoResponse> listarTodos() {
        log.info("Listando todos los movimientos");
        return movimientoMapper.toResponseList(movimientoRepository.findAllByOrderByIdAsc());
    }

    public MovimientoResponse buscarPorId(Integer id) {
        log.info("Buscando movimiento por id: {}", id);
        return movimientoMapper.toResponse(getMovimientoById(id));
    }

    public List<MovimientoResponse> listarPorViajero(String rutViajero) {
        log.info("Listando movimientos del viajero: {}", rutViajero);
        return movimientoMapper.toResponseList(movimientoRepository.findByRutViajero(rutViajero));
    }

    @Transactional
    @SuppressWarnings("null")
    public MovimientoResponse crear(MovimientoRequest request) {
        log.info("Creando movimiento para viajero: {}", request.getRutViajero());
        if (!viajeroRepository.existsByRut(request.getRutViajero())) {
            throw new EntityNotFoundException("Viajero", "rut", request.getRutViajero());
        }
        Movimiento movimiento = movimientoMapper.toEntity(request);
        return movimientoMapper.toResponse(movimientoRepository.save(movimiento));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando movimiento id: {}", id);
        movimientoRepository.delete(getMovimientoById(id));
    }
    @SuppressWarnings("null")
    private Movimiento getMovimientoById(Integer id) {
        return movimientoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Movimiento", "id", id));
    }
}
