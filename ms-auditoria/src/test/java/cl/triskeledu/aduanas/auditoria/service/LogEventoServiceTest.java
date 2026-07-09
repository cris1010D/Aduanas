package cl.triskeledu.aduanas.auditoria.service;

import cl.triskeledu.aduanas.auditoria.dto.DetalleLogResponse;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoRequest;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.event.LogEventoEventProducer;
import cl.triskeledu.aduanas.auditoria.mapper.LogEventoMapper;
import cl.triskeledu.aduanas.auditoria.model.DetalleLog;
import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import cl.triskeledu.aduanas.auditoria.repository.DetalleLogRepository;
import cl.triskeledu.aduanas.auditoria.repository.LogEventoRepository;
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
@DisplayName("LogEventoService - Pruebas Unitarias")
class LogEventoServiceTest {

    @Mock private LogEventoRepository logEventoRepository;
    @Mock private DetalleLogRepository detalleLogRepository;
    @Mock private LogEventoMapper logEventoMapper;
    @Mock private LogEventoEventProducer logEventoEventProducer;

    @InjectMocks
    private LogEventoService logEventoService;

    private LogEvento logEvento;
    private LogEventoResponse logEventoResponse;
    private LogEventoRequest logEventoRequest;

    @BeforeEach
    void setUp() {
        logEvento = LogEvento.builder()
                .id(1).rutOficial("12345678-9").accion("CREAR_VIAJERO")
                .fecha(LocalDate.of(2026, 1, 15)).msOrigen("ms-proceso")
                .build();

        logEventoResponse = LogEventoResponse.builder()
                .id(1).rutOficial("12345678-9").accion("CREAR_VIAJERO")
                .fecha(LocalDate.of(2026, 1, 15)).msOrigen("ms-proceso")
                .build();

        logEventoRequest = LogEventoRequest.builder()
                .rutOficial("12345678-9").accion("CREAR_VIAJERO")
                .fecha(LocalDate.of(2026, 1, 15)).msOrigen("ms-proceso")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos retorna lista de logs")
    void listarTodos_retornaLista() {
        when(logEventoRepository.findAllByOrderByIdAsc()).thenReturn(List.of(logEvento));
        when(logEventoMapper.toResponseList(anyList())).thenReturn(List.of(logEventoResponse));

        List<LogEventoResponse> resultado = logEventoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getAccion()).isEqualTo("CREAR_VIAJERO");
    }

    @Test
    @DisplayName("listarPorOficial retorna logs del oficial")
    void listarPorOficial_retornaLista() {
        when(logEventoRepository.findByRutOficial("12345678-9")).thenReturn(List.of(logEvento));
        when(logEventoMapper.toResponseList(anyList())).thenReturn(List.of(logEventoResponse));

        List<LogEventoResponse> resultado = logEventoService.listarPorOficial("12345678-9");

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("listarPorMs retorna logs del microservicio")
    void listarPorMs_retornaLista() {
        when(logEventoRepository.findByMsOrigen("ms-proceso")).thenReturn(List.of(logEvento));
        when(logEventoMapper.toResponseList(anyList())).thenReturn(List.of(logEventoResponse));

        List<LogEventoResponse> resultado = logEventoService.listarPorMs("ms-proceso");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getMsOrigen()).isEqualTo("ms-proceso");
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna el log")
    void buscarPorId_existente_retornaLog() {
        when(logEventoRepository.findById(1)).thenReturn(Optional.of(logEvento));
        when(logEventoMapper.toResponse(logEvento)).thenReturn(logEventoResponse);

        LogEventoResponse resultado = logEventoService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getMsOrigen()).isEqualTo("ms-proceso");
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(logEventoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> logEventoService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("LogEvento");
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear log publica evento y retorna response")
    @SuppressWarnings("null")
    void crear_exitoso_retornaResponse() {
        when(logEventoMapper.toEntity(logEventoRequest)).thenReturn(logEvento);
        when(logEventoRepository.save(logEvento)).thenReturn(logEvento);
        when(logEventoMapper.toResponse(logEvento)).thenReturn(logEventoResponse);

        LogEventoResponse resultado = logEventoService.crear(logEventoRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getAccion()).isEqualTo("CREAR_VIAJERO");
        verify(logEventoEventProducer).sendLogEventoCreated(any());
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar log existente llama delete")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(logEventoRepository.findById(1)).thenReturn(Optional.of(logEvento));

        logEventoService.eliminar(1);

        verify(logEventoRepository).delete(logEvento);
    }

    @Test
    @DisplayName("eliminar log inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(logEventoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> logEventoService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // R.14 — DETALLES DE UN LOG
    // -------------------------------------------------------

    @Test
    @DisplayName("listarDetallesPorLog con log existente retorna lista de detalles")
    void listarDetallesPorLog_logExistente_retornaDetalles() {
        DetalleLog detalle = DetalleLog.builder()
                .id(1).idLog(1).entidad("Viajero")
                .campo("estado").valorNuevo("APROBADO")
                .build();

        when(logEventoRepository.findById(1)).thenReturn(Optional.of(logEvento));
        when(detalleLogRepository.findByIdLog(1)).thenReturn(List.of(detalle));

        List<DetalleLogResponse> resultado = logEventoService.listarDetallesPorLog(1);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEntidad()).isEqualTo("Viajero");
        assertThat(resultado.get(0).getCampo()).isEqualTo("estado");
        assertThat(resultado.get(0).getValorNuevo()).isEqualTo("APROBADO");
    }

    @Test
    @DisplayName("listarDetallesPorLog con log inexistente lanza EntityNotFoundException")
    void listarDetallesPorLog_logInexistente_lanzaExcepcion() {
        when(logEventoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> logEventoService.listarDetallesPorLog(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("LogEvento");
    }
}
