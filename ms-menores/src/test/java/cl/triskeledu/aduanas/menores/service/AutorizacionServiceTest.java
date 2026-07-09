package cl.triskeledu.aduanas.menores.service;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.mapper.AutorizacionMapper;
import cl.triskeledu.aduanas.menores.model.Autorizacion;
import cl.triskeledu.aduanas.menores.repository.AutorizacionRepository;
import cl.triskeledu.aduanas.menores.repository.MenorRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutorizacionService - Pruebas Unitarias")
class AutorizacionServiceTest {

    @Mock private AutorizacionRepository autorizacionRepository;
    @Mock private MenorRepository menorRepository;
    @Mock private AutorizacionMapper autorizacionMapper;

    @InjectMocks
    private AutorizacionService autorizacionService;

    private Autorizacion autorizacion;
    private AutorizacionResponse autorizacionResponse;
    private AutorizacionRequest autorizacionRequest;

    @BeforeEach
    void setUp() {
        autorizacion = Autorizacion.builder()
                .id(1).rutMenor("11111111-1").tipo("VIAJE_INTERNACIONAL")
                .vigencia(LocalDate.of(2026, 12, 31)).notariaOrigen("Notaría Santiago Centro")
                .build();

        autorizacionResponse = AutorizacionResponse.builder()
                .id(1).rutMenor("11111111-1").tipo("VIAJE_INTERNACIONAL")
                .vigencia(LocalDate.of(2026, 12, 31)).notariaOrigen("Notaría Santiago Centro")
                .build();

        autorizacionRequest = AutorizacionRequest.builder()
                .rutMenor("11111111-1").tipo("VIAJE_INTERNACIONAL")
                .vigencia(LocalDate.of(2026, 12, 31)).notariaOrigen("Notaría Santiago Centro")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodas retorna lista de autorizaciones")
    void listarTodas_retornaLista() {
        when(autorizacionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(autorizacion));
        when(autorizacionMapper.toResponseList(anyList())).thenReturn(List.of(autorizacionResponse));

        List<AutorizacionResponse> resultado = autorizacionService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("VIAJE_INTERNACIONAL");
    }

    @Test
    @DisplayName("listarPorMenor retorna autorizaciones del menor")
    void listarPorMenor_retornaLista() {
        when(autorizacionRepository.findByRutMenor("11111111-1")).thenReturn(List.of(autorizacion));
        when(autorizacionMapper.toResponseList(anyList())).thenReturn(List.of(autorizacionResponse));

        List<AutorizacionResponse> resultado = autorizacionService.listarPorMenor("11111111-1");

        assertThat(resultado).hasSize(1);
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna la autorizacion")
    void buscarPorId_existente_retornaAutorizacion() {
        when(autorizacionRepository.findById(1)).thenReturn(Optional.of(autorizacion));
        when(autorizacionMapper.toResponse(autorizacion)).thenReturn(autorizacionResponse);

        AutorizacionResponse resultado = autorizacionService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(autorizacionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorizacionService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Autorizacion");
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear autorizacion con menor existente retorna response")
    @SuppressWarnings("null")
    void crear_menorExistente_retornaResponse() {
        when(menorRepository.existsByRut("11111111-1")).thenReturn(true);
        when(autorizacionMapper.toEntity(autorizacionRequest)).thenReturn(autorizacion);
        when(autorizacionRepository.save(autorizacion)).thenReturn(autorizacion);
        when(autorizacionMapper.toResponse(autorizacion)).thenReturn(autorizacionResponse);

        AutorizacionResponse resultado = autorizacionService.crear(autorizacionRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRutMenor()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("crear autorizacion con menor inexistente lanza EntityNotFoundException")
    void crear_menorInexistente_lanzaExcepcion() {
        when(menorRepository.existsByRut("11111111-1")).thenReturn(false);

        assertThatThrownBy(() -> autorizacionService.crear(autorizacionRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Menor");
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar autorizacion existente llama delete")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(autorizacionRepository.findById(1)).thenReturn(Optional.of(autorizacion));

        autorizacionService.eliminar(1);

        verify(autorizacionRepository).delete(autorizacion);
    }

    @Test
    @DisplayName("eliminar autorizacion inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(autorizacionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorizacionService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
