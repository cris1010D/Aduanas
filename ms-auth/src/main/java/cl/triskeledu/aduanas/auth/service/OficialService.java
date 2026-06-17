package cl.triskeledu.aduanas.auth.service;

import cl.triskeledu.aduanas.auth.dto.LoginRequest;
import cl.triskeledu.aduanas.auth.dto.LoginResponse;
import cl.triskeledu.aduanas.auth.dto.OficialCreateRequest;
import cl.triskeledu.aduanas.auth.dto.OficialResponse;
import cl.triskeledu.aduanas.auth.dto.OficialUpdateRequest;
import cl.triskeledu.aduanas.auth.event.OficialEventProducer;
import cl.triskeledu.aduanas.auth.mapper.OficialMapper;
import cl.triskeledu.aduanas.auth.model.Oficial;
import cl.triskeledu.aduanas.auth.repository.OficialRepository;
import cl.triskeledu.common.event.OficialCreatedEvent;
import cl.triskeledu.common.event.OficialDeletedEvent;
import cl.triskeledu.common.event.OficialUpdatedEvent;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import cl.triskeledu.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OficialService {

    private final OficialRepository oficialRepository;
    private final OficialMapper oficialMapper;
    private final OficialEventProducer oficialEventProducer;
    
    // Inyecciones para la seguridad agregadas por requerimiento
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Autentica a un oficial en el sistema mediante su RUT y contraseña.
     * Si es exitoso, emite un token JWT firmado por el Provider de common.
     */
    public LoginResponse login(LoginRequest request) {
        log.info("Iniciando proceso de autenticación para el RUT: {}", request.getUsername());

        // 1. Buscar al oficial usando el RUT (mapeado en request.getUsername())
        Oficial oficial = oficialRepository.findByRut(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas en el sistema de Aduanas"));

        // 2. Verificar si el oficial está activo en el sistema fronterizo
        if (Boolean.FALSE.equals(oficial.getActivo())) {
            throw new UsernameNotFoundException("El oficial se encuentra inactivo en el sistema.");
        }

        // 3. Validar el password enviado contra el hash almacenado usando PasswordEncoder
        if (!passwordEncoder.matches(request.getPassword(), oficial.getPassword())) {
            throw new UsernameNotFoundException("Credenciales inválidas en el sistema de Aduanas");
        }

        // 4. Configurar claims personalizados para el JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", oficial.getRol());
        claims.put("nombre", oficial.getNombre());

        // 5. Emitir token firmado usando el componente de Common
        String token = jwtTokenProvider.generateToken(oficial.getRut(), claims);

        return new LoginResponse(token, oficial.getRut(), "Bearer");
    }

    public List<OficialResponse> listarTodos() {
        log.info("Listando todos los oficiales");
        return oficialMapper.toResponseList(oficialRepository.findAllByOrderByIdAsc());
    }

    public List<OficialResponse> listarActivos() {
        log.info("Listando oficiales activos");
        return oficialMapper.toResponseList(oficialRepository.findByActivoTrue());
    }

    public OficialResponse buscarPorId(Integer id) {
        log.info("Buscando oficial por id: {}", id);
        return oficialMapper.toResponse(getOficialById(id));
    }

    public OficialResponse buscarPorRut(String rut) {
        log.info("Buscando oficial por rut: {}", rut);
        return oficialMapper.toResponse(
            oficialRepository.findByRut(rut)
                .orElseThrow(() -> new EntityNotFoundException("Oficial", "rut", rut))
        );
    }

    @Transactional
    public OficialResponse crear(OficialCreateRequest request) {
        log.info("Creando oficial con rut: {}", request.getRut());
        validarRutUnico(request.getRut());
        Oficial oficial = oficialMapper.toEntity(request);
        oficial.setPassword(passwordEncoder.encode(request.getPassword()));
        Oficial guardado = oficialRepository.save(oficial);
        oficialEventProducer.sendOficialCreated(
            OficialCreatedEvent.builder()
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .rol(guardado.getRol())
                .activo(guardado.getActivo())
                .build()
        );
        return oficialMapper.toResponse(guardado);
    }

    @Transactional
    public OficialResponse actualizar(Integer id, OficialUpdateRequest request) {
        log.info("Actualizando oficial id: {}", id);
        Oficial oficial = getOficialById(id);
        if (!oficial.getRut().equals(request.getRut())) {
            validarRutUnico(request.getRut());
        }
        oficialMapper.updateEntity(request, oficial);
        Oficial actualizado = oficialRepository.save(oficial);
        oficialEventProducer.sendOficialUpdated(
            OficialUpdatedEvent.builder()
                .rut(actualizado.getRut())
                .nombre(actualizado.getNombre())
                .rol(actualizado.getRol())
                .activo(actualizado.getActivo())
                .build()
        );
        return oficialMapper.toResponse(actualizado);
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando oficial id: {}", id);
        Oficial oficial = getOficialById(id);
        oficialRepository.delete(oficial);
        oficialEventProducer.sendOficialDeleted(
            OficialDeletedEvent.builder()
                .rut(oficial.getRut())
                .build()
        );
    }

    @SuppressWarnings("null")
    private Oficial getOficialById(Integer id) {
        return oficialRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Oficial", "id", id));
    }

    private void validarRutUnico(String rut) {
        if (oficialRepository.existsByRut(rut)) {
            throw new DuplicateResourceException("Oficial", "rut", rut, rut);
        }
    }
}