package cl.triskeledu.aduanas.auth.service;

<<<<<<< HEAD
import cl.triskeledu.aduanas.auth.dto.*;
=======
import cl.triskeledu.aduanas.auth.dto.LoginRequest;
import cl.triskeledu.aduanas.auth.dto.LoginResponse;
import cl.triskeledu.aduanas.auth.dto.OficialCreateRequest;
import cl.triskeledu.aduanas.auth.dto.OficialResponse;
import cl.triskeledu.aduanas.auth.dto.OficialUpdateRequest;
import cl.triskeledu.aduanas.auth.dto.RegistroViajeroRequest;
import cl.triskeledu.aduanas.auth.dto.RegistroViajeroResponse;
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
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
        // --- DEBUG: remover antes de produccion ---
        log.debug("[AUTH-DEBUG] Password recibido (raw)       : '{}'", request.getPassword());
        log.debug("[AUTH-DEBUG] Hash almacenado en BD (rut={}) : '{}'", oficial.getRut(), oficial.getPassword());
        boolean passwordValido = passwordEncoder.matches(request.getPassword(), oficial.getPassword());
        log.debug("[AUTH-DEBUG] Resultado passwordEncoder.matches(): {}", passwordValido);
        // ------------------------------------------
        if (!passwordValido) {
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

    /**
     * Registro público de viajeros. No requiere token.
     * El rol se fuerza a VIAJERO — el usuario no puede elegirlo.
     * Retorna un JWT para que el viajero quede autenticado inmediatamente.
     */
    @Transactional
    @SuppressWarnings("null")
    public RegistroViajeroResponse registrarViajero(RegistroViajeroRequest request) {
        log.info("Registro de viajero con rut: {}", request.getRut());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        validarRutUnico(request.getRut());

        Oficial viajero = Oficial.builder()
            .rut(request.getRut())
            .nombre(request.getNombre())
            .rol("VIAJERO")
            .activo(true)
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

        Oficial guardado = oficialRepository.save(viajero);
        log.info("Viajero registrado exitosamente: {}", guardado.getRut());

        // Generar token JWT para login inmediato
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", guardado.getRol());
        claims.put("nombre", guardado.getNombre());
        String token = jwtTokenProvider.generateToken(guardado.getRut(), claims);

        return RegistroViajeroResponse.builder()
            .id(guardado.getId())
            .rut(guardado.getRut())
            .nombre(guardado.getNombre())
            .rol(guardado.getRol())
            .token(token)
            .mensaje("Registro exitoso. Bienvenido al Sistema de Aduanas.")
            .build();
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
<<<<<<< HEAD

    /**
     * Registro público de transportistas/choferes comerciales. No requiere token.
     * El rol se fuerza a TRANSPORTISTA — el usuario no puede elegirlo.
     */
    @Transactional
    @SuppressWarnings("null")
    public RegistroTransportistaResponse registrarTransportista(RegistroTransportistaRequest request) {
        log.info("Registro de transportista con rut: {}", request.getRut());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        validarRutUnico(request.getRut());

        Oficial transportista = Oficial.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .rol("TRANSPORTISTA")
                .activo(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Oficial guardado = oficialRepository.save(transportista);
        log.info("Transportista registrado exitosamente: {}", guardado.getRut());

        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", guardado.getRol());
        claims.put("nombre", guardado.getNombre());
        String token = jwtTokenProvider.generateToken(guardado.getRut(), claims);

        return RegistroTransportistaResponse.builder()
                .id(guardado.getId())
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .empresa(request.getEmpresa())
                .rol(guardado.getRol())
                .token(token)
                .mensaje("Registro exitoso. Bienvenido al Portal de Transportistas.")
                .build();
    }
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
}
