package cl.triskeledu.aduanas.sag.service;

import cl.triskeledu.aduanas.sag.dto.*;
import cl.triskeledu.aduanas.sag.event.DeclaracionEventProducer;
import cl.triskeledu.aduanas.sag.mapper.DeclaracionMapper;
import cl.triskeledu.aduanas.sag.model.Declaracion;
import cl.triskeledu.aduanas.sag.repository.DeclaracionRepository;
import cl.triskeledu.aduanas.sag.repository.ItemDeclaracionRepository;
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
@DisplayName("DeclaracionService - Pruebas Unitarias")
class DeclaracionServiceTest {

    @Mock private DeclaracionRepository declaracionRepository;
    @Mock private ItemDeclaracionRepository itemDeclaracionRepository;
    @Mock private DeclaracionMapper declaracionMapper;
    @Mock private DeclaracionEventProducer declaracionEventProducer;

    @InjectMocks
    private DeclaracionService declaracionService;

    private Declaracion declaracion;
    private DeclaracionResponse declaracionResponse;
    private DeclaracionRequest declaracionRequest;

    @BeforeEach
    void setUp() {
        declaracion = Declaracion.builder()
                .id(1).rutViajero("11111111-1")
                .fecha(LocalDate.of(2026, 1, 15))
                .estado("APROBADA").pasoFronterizo("Los Libertadores")
                .build();

        declaracionResponse = DeclaracionResponse.builder()
                .id(1).rutViajero("11111111-1")
                .fecha(LocalDate.of(2026, 1, 15))
                .estado("APROBADA").pasoFronterizo("Los Libertadores")
                .build();

        declaracionRequest = DeclaracionRequest.builder()
                .rutViajero("11111111-1")
                .fecha(LocalDate.of(2026, 1, 15))
                .estado("APROBADA").pasoFronterizo("Los Libertadores")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodas retorna lista de declaraciones")
    void listarTodas_retornaLista() {
        when(declaracionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(declaracion));
        when(declaracionMapper.toResponseList(anyList())).thenReturn(List.of(declaracionResponse));

        List<DeclaracionResponse> resultado = declaracionService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRutViajero()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("listarPorViajero retorna declaraciones del viajero")
    void listarPorViajero_retornaLista() {
        when(declaracionRepository.findByRutViajero("11111111-1")).thenReturn(List.of(declaracion));
        when(declaracionMapper.toResponseList(anyList())).thenReturn(List.of(declaracionResponse));

        List<DeclaracionResponse> resultado = declaracionService.listarPorViajero("11111111-1");

        assertThat(resultado).hasSize(1);
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna la declaracion")
    void buscarPorId_existente_retornaDeclaracion() {
        when(declaracionRepository.findById(1)).thenReturn(Optional.of(declaracion));
        when(declaracionMapper.toResponse(declaracion)).thenReturn(declaracionResponse);

        DeclaracionResponse resultado = declaracionService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(declaracionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declaracionService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Declaracion");
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear declaracion publica evento y retorna response")
    @SuppressWarnings("null")
    void crear_exitoso_retornaResponse() {
        when(declaracionMapper.toEntity(declaracionRequest)).thenReturn(declaracion);
        when(declaracionRepository.save(declaracion)).thenReturn(declaracion);
        when(declaracionMapper.toResponse(declaracion)).thenReturn(declaracionResponse);

        DeclaracionResponse resultado = declaracionService.crear(declaracionRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("APROBADA");
        verify(declaracionEventProducer).sendDeclaracionCreated(any());
    }

    // -------------------------------------------------------
    // ACTUALIZAR
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar declaracion existente retorna response actualizado")
    @SuppressWarnings("null")
    void actualizar_existente_retornaActualizado() {
        when(declaracionRepository.findById(1)).thenReturn(Optional.of(declaracion));
        when(declaracionRepository.save(declaracion)).thenReturn(declaracion);
        when(declaracionMapper.toResponse(declaracion)).thenReturn(declaracionResponse);

        DeclaracionResponse resultado = declaracionService.actualizar(1, declaracionRequest);

        assertThat(resultado).isNotNull();
        verify(declaracionMapper).updateEntity(declaracionRequest, declaracion);
    }

    @Test
    @DisplayName("actualizar declaracion inexistente lanza EntityNotFoundException")
    void actualizar_inexistente_lanzaExcepcion() {
        when(declaracionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declaracionService.actualizar(99, declaracionRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar declaracion existente llama delete")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(declaracionRepository.findById(1)).thenReturn(Optional.of(declaracion));

        declaracionService.eliminar(1);

        verify(declaracionRepository).delete(declaracion);
    }

    @Test
    @DisplayName("eliminar declaracion inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(declaracionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declaracionService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // R.5 — TRAMITE SAG (regla de negocio: CUARENTENA vs APROBADA)
    // -------------------------------------------------------

    @Test
    @DisplayName("procesarTramite con item ALTO resulta en estado CUARENTENA")
    @SuppressWarnings("null")
    void procesarTramite_itemAlto_resultaCUARENTENA() {
        ItemDeclaracionRequest itemAlto = ItemDeclaracionRequest.builder()
                .descripcion("Frutas tropicales").cantidad(5).riesgo("ALTO").build();

        TramiteSagRequest tramite = TramiteSagRequest.builder()
                .rutViajero("11111111-1")
                .fecha(LocalDate.of(2026, 1, 15))
                .pasoFronterizo("Los Libertadores")
                .items(List.of(itemAlto))
                .build();

        Declaracion guardada = Declaracion.builder()
                .id(10).rutViajero("11111111-1")
                .fecha(tramite.getFecha())
                .estado("CUARENTENA")
                .pasoFronterizo("Los Libertadores")
                .build();

        DeclaracionResponse responseEsperado = DeclaracionResponse.builder()
                .id(10).estado("CUARENTENA").build();

        when(declaracionRepository.save(any(Declaracion.class))).thenReturn(guardada);
        when(itemDeclaracionRepository.save(any())).thenReturn(null);
        when(declaracionMapper.toResponse(guardada)).thenReturn(responseEsperado);

        DeclaracionResponse resultado = declaracionService.procesarTramite(tramite);

        assertThat(resultado.getEstado()).isEqualTo("CUARENTENA");
        verify(declaracionEventProducer).sendDeclaracionCreated(any());
    }

    @Test
    @DisplayName("procesarTramite sin items ALTO resulta en estado APROBADA")
    @SuppressWarnings("null")
    void procesarTramite_sinItemAlto_resultaAPROBADA() {
        ItemDeclaracionRequest itemBajo = ItemDeclaracionRequest.builder()
                .descripcion("Ropa").cantidad(2).riesgo("BAJO").build();
        ItemDeclaracionRequest itemMedio = ItemDeclaracionRequest.builder()
                .descripcion("Alimentos envasados").cantidad(3).riesgo("MEDIO").build();

        TramiteSagRequest tramite = TramiteSagRequest.builder()
                .rutViajero("22222222-2")
                .fecha(LocalDate.of(2026, 1, 15))
                .pasoFronterizo("Chacalluta")
                .items(List.of(itemBajo, itemMedio))
                .build();

        Declaracion guardada = Declaracion.builder()
                .id(11).rutViajero("22222222-2")
                .fecha(tramite.getFecha())
                .estado("APROBADA")
                .pasoFronterizo("Chacalluta")
                .build();

        DeclaracionResponse responseEsperado = DeclaracionResponse.builder()
                .id(11).estado("APROBADA").build();

        when(declaracionRepository.save(any(Declaracion.class))).thenReturn(guardada);
        when(itemDeclaracionRepository.save(any())).thenReturn(null);
        when(declaracionMapper.toResponse(guardada)).thenReturn(responseEsperado);

        DeclaracionResponse resultado = declaracionService.procesarTramite(tramite);

        assertThat(resultado.getEstado()).isEqualTo("APROBADA");
    }
}
