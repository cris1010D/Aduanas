package cl.triskeledu.aduanas.pdi.service;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.mapper.ConsultaMapper;
import cl.triskeledu.aduanas.pdi.model.Consulta;
import cl.triskeledu.aduanas.pdi.repository.AntecedenteRepository;
import cl.triskeledu.aduanas.pdi.repository.ConsultaRepository;
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
@DisplayName("ConsultaService - Pruebas Unitarias")
class ConsultaServiceTest {

    @Mock private ConsultaRepository consultaRepository;
    @Mock private AntecedenteRepository antecedenteRepository;
    @Mock private ConsultaMapper consultaMapper;

    @InjectMocks
    private ConsultaService consultaService;

    private Consulta consulta;
    private ConsultaResponse consultaResponse;
    private ConsultaRequest consultaRequest;

    @BeforeEach
    void setUp() {
        consulta = Consulta.builder()
                .id(1).rut("11111111-1").rutOficial("33333333-3")
                .fecha(LocalDate.of(2026, 1, 15)).motivo("Control fronterizo")
                .build();

        consultaResponse = ConsultaResponse.builder()
                .id(1).rut("11111111-1").rutOficial("33333333-3")
                .fecha(LocalDate.of(2026, 1, 15)).motivo("Control fronterizo")
                .build();

        consultaRequest = ConsultaRequest.builder()
                .rut("11111111-1").rutOficial("33333333-3")
                .fecha(LocalDate.of(2026, 1, 15)).motivo("Control fronterizo")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodas retorna lista de consultas")
    void listarTodas_retornaLista() {
        when(consultaRepository.findAllByOrderByIdAsc()).thenReturn(List.of(consulta));
        when(consultaMapper.toResponseList(anyList())).thenReturn(List.of(consultaResponse));

        List<ConsultaResponse> resultado = consultaService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRut()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("listarPorRut retorna consultas del rut")
    void listarPorRut_retornaLista() {
        when(consultaRepository.findByRut("11111111-1")).thenReturn(List.of(consulta));
        when(consultaMapper.toResponseList(anyList())).thenReturn(List.of(consultaResponse));

        List<ConsultaResponse> resultado = consultaService.listarPorRut("11111111-1");

        assertThat(resultado).hasSize(1);
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna la consulta")
    void buscarPorId_existente_retornaConsulta() {
        when(consultaRepository.findById(1)).thenReturn(Optional.of(consulta));
        when(consultaMapper.toResponse(consulta)).thenReturn(consultaResponse);

        ConsultaResponse resultado = consultaService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getMotivo()).isEqualTo("Control fronterizo");
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(consultaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Consulta");
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear consulta con antecedente existente retorna response")
    @SuppressWarnings("null")
    void crear_antecedenteExistente_retornaResponse() {
        when(antecedenteRepository.existsByRut("11111111-1")).thenReturn(true);
        when(consultaMapper.toEntity(consultaRequest)).thenReturn(consulta);
        when(consultaRepository.save(consulta)).thenReturn(consulta);
        when(consultaMapper.toResponse(consulta)).thenReturn(consultaResponse);

        ConsultaResponse resultado = consultaService.crear(consultaRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRut()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("crear consulta con antecedente inexistente lanza EntityNotFoundException")
    void crear_antecedenteInexistente_lanzaExcepcion() {
        when(antecedenteRepository.existsByRut("11111111-1")).thenReturn(false);

        assertThatThrownBy(() -> consultaService.crear(consultaRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Antecedente");
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar consulta existente llama delete")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(consultaRepository.findById(1)).thenReturn(Optional.of(consulta));

        consultaService.eliminar(1);

        verify(consultaRepository).delete(consulta);
    }

    @Test
    @DisplayName("eliminar consulta inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(consultaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
