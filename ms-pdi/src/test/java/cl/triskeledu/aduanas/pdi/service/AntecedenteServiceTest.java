package cl.triskeledu.aduanas.pdi.service;

import cl.triskeledu.aduanas.pdi.dto.*;
import cl.triskeledu.aduanas.pdi.event.AntecedenteEventProducer;
import cl.triskeledu.aduanas.pdi.mapper.AntecedenteMapper;
import cl.triskeledu.aduanas.pdi.model.Antecedente;
import cl.triskeledu.aduanas.pdi.model.Consulta;
import cl.triskeledu.aduanas.pdi.repository.AntecedenteRepository;
import cl.triskeledu.aduanas.pdi.repository.ConsultaRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
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
@DisplayName("AntecedenteService - Pruebas Unitarias")
class AntecedenteServiceTest {

    @Mock private AntecedenteRepository antecedenteRepository;
    @Mock private ConsultaRepository consultaRepository;
    @Mock private AntecedenteMapper antecedenteMapper;
    @Mock private AntecedenteEventProducer antecedenteEventProducer;

    @InjectMocks
    private AntecedenteService antecedenteService;

    private Antecedente antecedente;
    private AntecedenteResponse antecedenteResponse;
    private AntecedenteRequest antecedenteRequest;

    @BeforeEach
    void setUp() {
        antecedente = Antecedente.builder()
                .id(1).rut("11111111-1").resultado("SIN_REGISTROS")
                .fechaConsulta(LocalDate.of(2026, 1, 10)).fuente("REGISTRO_CIVIL")
                .build();

        antecedenteResponse = AntecedenteResponse.builder()
                .id(1).rut("11111111-1").resultado("SIN_REGISTROS")
                .fechaConsulta(LocalDate.of(2026, 1, 10)).fuente("REGISTRO_CIVIL")
                .build();

        antecedenteRequest = AntecedenteRequest.builder()
                .rut("11111111-1").resultado("SIN_REGISTROS")
                .fechaConsulta(LocalDate.of(2026, 1, 10)).fuente("REGISTRO_CIVIL")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos retorna lista de antecedentes")
    void listarTodos_retornaLista() {
        when(antecedenteRepository.findAllByOrderByIdAsc()).thenReturn(List.of(antecedente));
        when(antecedenteMapper.toResponseList(anyList())).thenReturn(List.of(antecedenteResponse));

        List<AntecedenteResponse> resultado = antecedenteService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRut()).isEqualTo("11111111-1");
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna el antecedente")
    void buscarPorId_existente_retornaAntecedente() {
        when(antecedenteRepository.findById(1)).thenReturn(Optional.of(antecedente));
        when(antecedenteMapper.toResponse(antecedente)).thenReturn(antecedenteResponse);

        AntecedenteResponse resultado = antecedenteService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getResultado()).isEqualTo("SIN_REGISTROS");
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(antecedenteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> antecedenteService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Antecedente");
    }

    @Test
    @DisplayName("buscarPorRut existente retorna el antecedente")
    void buscarPorRut_existente_retornaAntecedente() {
        when(antecedenteRepository.findByRut("11111111-1")).thenReturn(Optional.of(antecedente));
        when(antecedenteMapper.toResponse(antecedente)).thenReturn(antecedenteResponse);

        AntecedenteResponse resultado = antecedenteService.buscarPorRut("11111111-1");

        assertThat(resultado.getRut()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("buscarPorRut inexistente lanza EntityNotFoundException")
    void buscarPorRut_inexistente_lanzaExcepcion() {
        when(antecedenteRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> antecedenteService.buscarPorRut("99999999-9"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear antecedente exitosamente publica evento y retorna response")
    @SuppressWarnings("null")
    void crear_exitoso_retornaResponse() {
        when(antecedenteRepository.existsByRut("11111111-1")).thenReturn(false);
        when(antecedenteMapper.toEntity(antecedenteRequest)).thenReturn(antecedente);
        when(antecedenteRepository.save(antecedente)).thenReturn(antecedente);
        when(antecedenteMapper.toResponse(antecedente)).thenReturn(antecedenteResponse);

        AntecedenteResponse resultado = antecedenteService.crear(antecedenteRequest);

        assertThat(resultado).isNotNull();
        verify(antecedenteEventProducer).sendAntecedenteCreated(any());
    }

    @Test
    @DisplayName("crear antecedente con RUT duplicado lanza DuplicateResourceException")
    void crear_rutDuplicado_lanzaExcepcion() {
        when(antecedenteRepository.existsByRut("11111111-1")).thenReturn(true);

        assertThatThrownBy(() -> antecedenteService.crear(antecedenteRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("11111111-1");
    }

    // -------------------------------------------------------
    // ACTUALIZAR
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar antecedente existente retorna response actualizado")
    @SuppressWarnings("null")
    void actualizar_existente_retornaActualizado() {
        when(antecedenteRepository.findById(1)).thenReturn(Optional.of(antecedente));
        when(antecedenteRepository.save(antecedente)).thenReturn(antecedente);
        when(antecedenteMapper.toResponse(antecedente)).thenReturn(antecedenteResponse);

        AntecedenteResponse resultado = antecedenteService.actualizar(1, antecedenteRequest);

        assertThat(resultado).isNotNull();
        verify(antecedenteMapper).updateEntity(antecedenteRequest, antecedente);
    }

    @Test
    @DisplayName("actualizar antecedente inexistente lanza EntityNotFoundException")
    void actualizar_inexistente_lanzaExcepcion() {
        when(antecedenteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> antecedenteService.actualizar(99, antecedenteRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar antecedente existente llama delete")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(antecedenteRepository.findById(1)).thenReturn(Optional.of(antecedente));

        antecedenteService.eliminar(1);

        verify(antecedenteRepository).delete(antecedente);
    }

    @Test
    @DisplayName("eliminar antecedente inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(antecedenteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> antecedenteService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // R.6 — CONSULTAR ANTECEDENTE PDI
    // -------------------------------------------------------

    @Test
    @DisplayName("consultarAntecedente con RUT existente registra consulta y retorna resultado")
    @SuppressWarnings("null")
    void consultarAntecedente_rutExistente_retornaResultado() {
        ConsultaRequest request = ConsultaRequest.builder()
                .rut("11111111-1").rutOficial("33333333-3")
                .fecha(LocalDate.of(2026, 1, 15)).motivo("Control fronterizo")
                .build();

        Consulta consultaGuardada = Consulta.builder()
                .id(5).rut("11111111-1").rutOficial("33333333-3")
                .fecha(request.getFecha()).motivo("Control fronterizo")
                .build();

        when(antecedenteRepository.findByRut("11111111-1")).thenReturn(Optional.of(antecedente));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaGuardada);

        ConsultaAntecedenteResponse resultado = antecedenteService.consultarAntecedente(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getResultado()).isEqualTo("SIN_REGISTROS");
        assertThat(resultado.getIdConsulta()).isEqualTo(5);
        assertThat(resultado.getRutOficial()).isEqualTo("33333333-3");
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("consultarAntecedente con RUT sin registro previo crea antecedente SIN_REGISTROS")
    @SuppressWarnings("null")
    void consultarAntecedente_sinRegistro_creaAntecedenteYRetornaResultado() {
        ConsultaRequest request = ConsultaRequest.builder()
                .rut("44444444-4").rutOficial("33333333-3")
                .fecha(LocalDate.of(2026, 1, 15)).motivo("Primer ingreso al país")
                .build();

        Antecedente nuevoAntecedente = Antecedente.builder()
                .id(99).rut("44444444-4").resultado("SIN_REGISTROS")
                .fuente("REGISTRO_CIVIL").fechaConsulta(request.getFecha())
                .build();

        Consulta consultaGuardada = Consulta.builder()
                .id(10).rut("44444444-4").rutOficial("33333333-3")
                .fecha(request.getFecha()).motivo("Primer ingreso al país")
                .build();

        when(antecedenteRepository.findByRut("44444444-4")).thenReturn(Optional.empty());
        when(antecedenteRepository.save(any(Antecedente.class))).thenReturn(nuevoAntecedente);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaGuardada);

        ConsultaAntecedenteResponse resultado = antecedenteService.consultarAntecedente(request);

        assertThat(resultado.getResultado()).isEqualTo("SIN_REGISTROS");
        verify(antecedenteRepository, times(1)).save(any(Antecedente.class));
    }
}
