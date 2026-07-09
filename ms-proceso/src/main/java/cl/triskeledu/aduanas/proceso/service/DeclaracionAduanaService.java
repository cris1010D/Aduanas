package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.dto.DeclaracionAduanaRequest;
import cl.triskeledu.aduanas.proceso.dto.DeclaracionAduanaResponse;
import cl.triskeledu.aduanas.proceso.mapper.DeclaracionAduanaMapper;
import cl.triskeledu.aduanas.proceso.model.DeclaracionAduana;
import cl.triskeledu.aduanas.proceso.repository.DeclaracionAduanaRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * R.6 — Declaracion aduanera de equipaje, valores y moneda.
 *
 * Evalua automaticamente si el viajero excede la franquicia legal:
 *   - Dinero en efectivo/instrumentos negociables >= USD 10.000
 *   - Alcohol > 2.5 litros
 *   - Cigarrillos > 400 unidades
 *   - Cualquier mercancia afecta a impuestos declarada
 *
 * Si excede alguna, la declaracion queda en estado OBSERVADA (requiere
 * revision de un oficial); en caso contrario queda APROBADA automaticamente.
 * El mismo patron de evaluacion de riesgo que usa SagOrquestadorService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeclaracionAduanaService {

    private static final BigDecimal MAX_EFECTIVO_USD = BigDecimal.valueOf(10_000);
    private static final double MAX_LITROS_ALCOHOL = 2.5;
    private static final int MAX_CIGARRILLOS = 400;

    private final DeclaracionAduanaRepository declaracionAduanaRepository;
    private final DeclaracionAduanaMapper declaracionAduanaMapper;

    public List<DeclaracionAduanaResponse> listarTodas() {
        log.info("Listando todas las declaraciones de aduana");
        return declaracionAduanaMapper.toResponseList(declaracionAduanaRepository.findAllByOrderByIdAsc());
    }

    public List<DeclaracionAduanaResponse> listarPorViajero(String rutViajero) {
        log.info("Listando declaraciones de aduana para rut: {}", rutViajero);
        return declaracionAduanaMapper.toResponseList(declaracionAduanaRepository.findByRutViajero(rutViajero));
    }

    public DeclaracionAduanaResponse buscarPorId(Integer id) {
        return declaracionAduanaMapper.toResponse(
                declaracionAduanaRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("DeclaracionAduana", "id", id))
        );
    }

    @Transactional
    public DeclaracionAduanaResponse tramitar(DeclaracionAduanaRequest request) {
        log.info("Tramitando declaracion de aduana. Viajero: {} | Paso: {}",
                request.getRutViajero(), request.getPasoFronterizo());

        boolean excedeEfectivo = Boolean.TRUE.equals(request.getPortaDineroEfectivo())
                && request.getMontoDinero() != null
                && "USD".equalsIgnoreCase(String.valueOf(request.getMonedaDinero()))
                && request.getMontoDinero().compareTo(MAX_EFECTIVO_USD) >= 0;

        boolean excedeAlcohol = request.getLitrosAlcohol() != null
                && request.getLitrosAlcohol() > MAX_LITROS_ALCOHOL;

        boolean excedeTabaco = request.getCantidadCigarrillos() != null
                && request.getCantidadCigarrillos() > MAX_CIGARRILLOS;

        boolean tieneMercanciasAfectas = Boolean.TRUE.equals(request.getMercanciasAfectas());

        boolean excedeFranquicia = excedeEfectivo || excedeAlcohol || excedeTabaco || tieneMercanciasAfectas;
        String estado = excedeFranquicia ? "OBSERVADA" : "APROBADA";

        DeclaracionAduana declaracion = DeclaracionAduana.builder()
                .rutViajero(request.getRutViajero())
                .fecha(request.getFecha())
                .pasoFronterizo(request.getPasoFronterizo())
                .portaDineroEfectivo(request.getPortaDineroEfectivo())
                .montoDinero(request.getMontoDinero())
                .monedaDinero(request.getMonedaDinero())
                .mercanciasAfectas(request.getMercanciasAfectas())
                .descripcionMercancias(request.getDescripcionMercancias())
                .litrosAlcohol(request.getLitrosAlcohol())
                .cantidadCigarrillos(request.getCantidadCigarrillos())
                .excedeFranquicia(excedeFranquicia)
                .estado(estado)
                .build();

        DeclaracionAduana guardada = declaracionAduanaRepository.save(declaracion);

        if (excedeFranquicia) {
            log.warn("ALERTA ADUANA — Declaracion {} OBSERVADA. Viajero: {} | Paso: {}",
                    guardada.getId(), guardada.getRutViajero(), guardada.getPasoFronterizo());
        }

        return declaracionAduanaMapper.toResponse(guardada);
    }
}