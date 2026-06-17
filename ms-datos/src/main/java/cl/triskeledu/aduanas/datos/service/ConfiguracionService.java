package cl.triskeledu.aduanas.datos.service;

import cl.triskeledu.aduanas.datos.dto.ConfiguracionRequest;
import cl.triskeledu.aduanas.datos.dto.ConfiguracionResponse;
import cl.triskeledu.aduanas.datos.event.ConfiguracionEventProducer;
import cl.triskeledu.aduanas.datos.mapper.ConfiguracionMapper;
import cl.triskeledu.aduanas.datos.model.Configuracion;
import cl.triskeledu.aduanas.datos.repository.ConfiguracionRepository;
import cl.triskeledu.common.event.ConfiguracionCreatedEvent;
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
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final ConfiguracionMapper configuracionMapper;
    private final ConfiguracionEventProducer configuracionEventProducer;

    public List<ConfiguracionResponse> listarTodas() {
        log.info("Listando todas las configuraciones");
        return configuracionMapper.toResponseList(configuracionRepository.findAllByOrderByIdAsc());
    }

    public List<ConfiguracionResponse> listarActivas() {
        log.info("Listando configuraciones activas");
        return configuracionMapper.toResponseList(configuracionRepository.findByActivoTrue());
    }

    public ConfiguracionResponse buscarPorId(Integer id) {
        log.info("Buscando configuracion por id: {}", id);
        return configuracionMapper.toResponse(getConfiguracionById(id));
    }

    public ConfiguracionResponse buscarPorClave(String clave) {
        log.info("Buscando configuracion por clave: {}", clave);
        return configuracionMapper.toResponse(
            configuracionRepository.findByClave(clave)
                .orElseThrow(() -> new EntityNotFoundException("Configuracion", "clave", clave))
        );
    }

    public List<ConfiguracionResponse> listarPorMs(String msDuenio) {
        log.info("Listando configuraciones del MS: {}", msDuenio);
        return configuracionMapper.toResponseList(configuracionRepository.findByMsDuenio(msDuenio));
    }

    @Transactional
    @SuppressWarnings("null")
    public ConfiguracionResponse crear(ConfiguracionRequest request) {
        log.info("Creando configuracion con clave: {}", request.getClave());
        if (configuracionRepository.existsByClave(request.getClave())) {
            throw new DuplicateResourceException("Configuracion", "clave", request.getClave(), request.getClave());
        }
        Configuracion configuracion = configuracionMapper.toEntity(request);
        Configuracion guardada = configuracionRepository.save(configuracion);
        configuracionEventProducer.sendConfiguracionCreated(
            ConfiguracionCreatedEvent.builder()
                .clave(guardada.getClave())
                .valor(guardada.getValor())
                .msDuenio(guardada.getMsDuenio())
                .activo(guardada.getActivo())
                .build()
        );
        return configuracionMapper.toResponse(guardada);
    }

    @Transactional
    public ConfiguracionResponse actualizar(Integer id, ConfiguracionRequest request) {
        log.info("Actualizando configuracion id: {}", id);
        Configuracion configuracion = getConfiguracionById(id);
        if (!configuracion.getClave().equals(request.getClave())
                && configuracionRepository.existsByClave(request.getClave())) {
            throw new DuplicateResourceException("Configuracion", "clave", request.getClave(), request.getClave());
        }
        configuracionMapper.updateEntity(request, configuracion);
        return configuracionMapper.toResponse(configuracionRepository.save(configuracion));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando configuracion id: {}", id);
        configuracionRepository.delete(getConfiguracionById(id));
    }

    @SuppressWarnings("null")
    private Configuracion getConfiguracionById(Integer id) {
        return configuracionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Configuracion", "id", id));
    }
}
