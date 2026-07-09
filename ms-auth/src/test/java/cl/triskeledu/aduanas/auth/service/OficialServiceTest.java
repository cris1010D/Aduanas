package cl.triskeledu.aduanas.auth.service;

import cl.triskeledu.aduanas.auth.dto.*;
import cl.triskeledu.aduanas.auth.event.OficialEventProducer;
import cl.triskeledu.aduanas.auth.mapper.OficialMapper;
import cl.triskeledu.aduanas.auth.model.Oficial;
import cl.triskeledu.aduanas.auth.repository.OficialRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import cl.triskeledu.common.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OficialService - Pruebas Unitarias")
class OficialServiceTest {

    @Mock private OficialRepository oficialRepository;
    @Mock private OficialMapper oficialMapper;
    @Mock private OficialEventProducer oficialEventProducer;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private OficialService oficialService;

    private Oficial oficial;
    private OficialResponse oficialResponse;
    private OficialCreateRequest createRequest;
    private OficialUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        oficial = Oficial.builder()
                .id(1).rut("12345678-9").nombre("Juan Pérez")
                .rol("INSPECTOR").activo(true).password("$2a$hash")
                .build();

        oficialResponse = OficialResponse.builder()
                .id(1).rut("12345678-9").nombre("Juan Pérez")
                .rol("INSPECTOR").activo(true)
                .build();

        createRequest = OficialCreateRequest.builder()
                .rut("12345678-9").nombre("Juan Pérez")
                .rol("INSPECTOR").activo(true).password("clave123")
                .build();

        updateRequest = OficialUpdateRequest.builder()
                .rut("12345678-9").nombre("Juan Pérez Actualizado")
                .rol("JEFE").activo(true)
                .build();
    }

    // -------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------

    @Test
    @DisplayName("login exitoso retorna token JWT")
    void login_exitoso_retornaToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("12345678-9");
        loginRequest.setPassword("clave123");

        when(oficialRepository.findByRut("12345678-9")).thenReturn(Optional.of(oficial));
        when(passwordEncoder.matches("clave123", "$2a$hash")).thenReturn(true);
        when(jwtTokenProvider.generateToken(eq("12345678-9"), anyMap())).thenReturn("token.jwt.firmado");

        LoginResponse response = oficialService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("token.jwt.firmado");
        assertThat(response.getUsername()).isEqualTo("12345678-9");
    }

    @Test
    @DisplayName("login con RUT inexistente lanza UsernameNotFoundException")
    void login_rutInexistente_lanzaExcepcion() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("99999999-9");
        loginRequest.setPassword("cualquiera");

        when(oficialRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oficialService.login(loginRequest))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("login con oficial inactivo lanza UsernameNotFoundException")
    void login_oficialInactivo_lanzaExcepcion() {
        oficial.setActivo(false);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("12345678-9");
        loginRequest.setPassword("clave123");

        when(oficialRepository.findByRut("12345678-9")).thenReturn(Optional.of(oficial));

        assertThatThrownBy(() -> oficialService.login(loginRequest))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("inactivo");
    }

    @Test
    @DisplayName("login con contraseña incorrecta lanza UsernameNotFoundException")
    void login_passwordIncorrecto_lanzaExcepcion() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("12345678-9");
        loginRequest.setPassword("incorrecta");

        when(oficialRepository.findByRut("12345678-9")).thenReturn(Optional.of(oficial));
        when(passwordEncoder.matches("incorrecta", "$2a$hash")).thenReturn(false);

        assertThatThrownBy(() -> oficialService.login(loginRequest))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos retorna lista de oficiales")
    void listarTodos_retornaLista() {
        when(oficialRepository.findAllByOrderByIdAsc()).thenReturn(List.of(oficial));
        when(oficialMapper.toResponseList(anyList())).thenReturn(List.of(oficialResponse));

        List<OficialResponse> resultado = oficialService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRut()).isEqualTo("12345678-9");
    }

    @Test
    @DisplayName("listarActivos retorna solo activos")
    void listarActivos_retornaActivos() {
        when(oficialRepository.findByActivoTrue()).thenReturn(List.of(oficial));
        when(oficialMapper.toResponseList(anyList())).thenReturn(List.of(oficialResponse));

        List<OficialResponse> resultado = oficialService.listarActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getActivo()).isTrue();
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna el oficial")
    void buscarPorId_existente_retornaOficial() {
        when(oficialRepository.findById(1)).thenReturn(Optional.of(oficial));
        when(oficialMapper.toResponse(oficial)).thenReturn(oficialResponse);

        OficialResponse resultado = oficialService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(oficialRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oficialService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Oficial");
    }

    @Test
    @DisplayName("buscarPorRut inexistente lanza EntityNotFoundException")
    void buscarPorRut_inexistente_lanzaExcepcion() {
        when(oficialRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oficialService.buscarPorRut("99999999-9"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear oficial exitosamente publica evento y retorna response")
    @SuppressWarnings("null")
    void crear_exitoso_retornaResponse() {
        when(oficialRepository.existsByRut("12345678-9")).thenReturn(false);
        when(oficialMapper.toEntity(createRequest)).thenReturn(oficial);
        when(passwordEncoder.encode("clave123")).thenReturn("$2a$hash");
        when(oficialRepository.save(oficial)).thenReturn(oficial);
        when(oficialMapper.toResponse(oficial)).thenReturn(oficialResponse);

        OficialResponse resultado = oficialService.crear(createRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRut()).isEqualTo("12345678-9");
        verify(oficialEventProducer).sendOficialCreated(any());
    }

    @Test
    @DisplayName("crear con RUT duplicado lanza DuplicateResourceException")
    void crear_rutDuplicado_lanzaExcepcion() {
        when(oficialRepository.existsByRut("12345678-9")).thenReturn(true);

        assertThatThrownBy(() -> oficialService.crear(createRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("12345678-9");
    }

    // -------------------------------------------------------
    // ACTUALIZAR
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar oficial existente retorna response actualizado")
    @SuppressWarnings("null")
    void actualizar_existente_retornaActualizado() {
        when(oficialRepository.findById(1)).thenReturn(Optional.of(oficial));
        when(oficialRepository.save(oficial)).thenReturn(oficial);
        when(oficialMapper.toResponse(oficial)).thenReturn(oficialResponse);

        OficialResponse resultado = oficialService.actualizar(1, updateRequest);

        assertThat(resultado).isNotNull();
        verify(oficialMapper).updateEntity(updateRequest, oficial);
        verify(oficialEventProducer).sendOficialUpdated(any());
    }

    @Test
    @DisplayName("actualizar oficial inexistente lanza EntityNotFoundException")
    void actualizar_inexistente_lanzaExcepcion() {
        when(oficialRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oficialService.actualizar(99, updateRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar oficial existente llama delete y publica evento")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(oficialRepository.findById(1)).thenReturn(Optional.of(oficial));

        oficialService.eliminar(1);

        verify(oficialRepository).delete(oficial);
        verify(oficialEventProducer).sendOficialDeleted(any());
    }

    @Test
    @DisplayName("eliminar oficial inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(oficialRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oficialService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
