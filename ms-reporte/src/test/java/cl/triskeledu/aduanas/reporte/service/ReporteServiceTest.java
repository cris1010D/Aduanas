package cl.triskeledu.aduanas.reporte.service;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.event.ReporteEventProducer;
import cl.triskeledu.aduanas.reporte.mapper.ReporteMapper;
import cl.triskeledu.aduanas.reporte.model.Reporte;
import cl.triskeledu.aduanas.reporte.repository.ReporteRepository;
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
@DisplayName("ReporteService - Pruebas Unitarias")
class ReporteServiceTest {

    @Mock private ReporteRepository reporteRepository;
    @Mock private ReporteMapper reporteMapper;
    @Mock private ReporteEventProducer reporteEventProducer;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;
    private ReporteResponse reporteResponse;
    private ReporteRequest reporteRequest;

    @BeforeEach
    void setUp() {
        reporte = Reporte.builder()
                .id(1).tipo("MOVIMIENTO_DIARIO")
                .fecha(LocalDate.of(2026, 1, 15))
                .rutOficial("12345678-9").formato("PDF")
                .build();

        reporteResponse = ReporteResponse.builder()
                .id(1).tipo("MOVIMIENTO_DIARIO")
                .fecha(LocalDate.of(2026, 1, 15))
                .rutOficial("12345678-9").formato("PDF")
                .build();

        reporteRequest = ReporteRequest.builder()
                .tipo("MOVIMIENTO_DIARIO")
                .fecha(LocalDate.of(2026, 1, 15))
                .rutOficial("12345678-9").formato("PDF")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos retorna lista de reportes")
    void listarTodos_retornaLista() {
        when(reporteRepository.findAllByOrderByIdAsc()).thenReturn(List.of(reporte));
        when(reporteMapper.toResponseList(anyList())).thenReturn(List.of(reporteResponse));

        List<ReporteResponse> resultado = reporteService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("MOVIMIENTO_DIARIO");
    }

    @Test
    @DisplayName("listarPorOficial retorna reportes del oficial")
    void listarPorOficial_retornaLista() {
        when(reporteRepository.findByRutOficial("12345678-9")).thenReturn(List.of(reporte));
        when(reporteMapper.toResponseList(anyList())).thenReturn(List.of(reporteResponse));

        List<ReporteResponse> resultado = reporteService.listarPorOficial("12345678-9");

        assertThat(resultado).hasSize(1);
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna el reporte")
    void buscarPorId_existente_retornaReporte() {
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));
        when(reporteMapper.toResponse(reporte)).thenReturn(reporteResponse);

        ReporteResponse resultado = reporteService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getFormato()).isEqualTo("PDF");
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(reporteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Reporte");
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear reporte publica evento y retorna response")
    void crear_exitoso_retornaResponse() {
        when(reporteMapper.toEntity(reporteRequest)).thenReturn(reporte);
        when(reporteRepository.save(reporte)).thenReturn(reporte);
        when(reporteMapper.toResponse(reporte)).thenReturn(reporteResponse);

        ReporteResponse resultado = reporteService.crear(reporteRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTipo()).isEqualTo("MOVIMIENTO_DIARIO");
        verify(reporteEventProducer).sendReporteCreated(any());
    }

    // -------------------------------------------------------
    // ACTUALIZAR
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar reporte existente retorna response actualizado")
    void actualizar_existente_retornaActualizado() {
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(reporte)).thenReturn(reporte);
        when(reporteMapper.toResponse(reporte)).thenReturn(reporteResponse);

        ReporteResponse resultado = reporteService.actualizar(1, reporteRequest);

        assertThat(resultado).isNotNull();
        verify(reporteMapper).updateEntity(reporteRequest, reporte);
    }

    @Test
    @DisplayName("actualizar reporte inexistente lanza EntityNotFoundException")
    void actualizar_inexistente_lanzaExcepcion() {
        when(reporteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.actualizar(99, reporteRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar reporte existente llama delete")
    void eliminar_existente_exitoso() {
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));

        reporteService.eliminar(1);

        verify(reporteRepository).delete(reporte);
    }

    @Test
    @DisplayName("eliminar reporte inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(reporteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
