package cl.triskeledu.aduanas.menores.service;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.event.MenorEventProducer;
import cl.triskeledu.aduanas.menores.mapper.MenorMapper;
import cl.triskeledu.aduanas.menores.model.Menor;
import cl.triskeledu.aduanas.menores.repository.MenorRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MenorService - Pruebas Unitarias")
class MenorServiceTest {

    @Mock private MenorRepository menorRepository;
    @Mock private MenorMapper menorMapper;
    @Mock private MenorEventProducer menorEventProducer;

    @InjectMocks
    private MenorService menorService;

    private Menor menor;
    private MenorResponse menorResponse;
    private MenorRequest menorRequest;

    @BeforeEach
    void setUp() {
        menor = Menor.builder()
                .id(1).rut("11111111-1").nombre("Ana García")
                .fechaNac(LocalDate.of(2015, 3, 20)).rutTutor("22222222-2")
                .build();

        menorResponse = MenorResponse.builder()
                .id(1).rut("11111111-1").nombre("Ana García")
                .fechaNac(LocalDate.of(2015, 3, 20)).rutTutor("22222222-2")
                .build();

        menorRequest = MenorRequest.builder()
                .rut("11111111-1").nombre("Ana García")
                .fechaNac(LocalDate.of(2015, 3, 20)).rutTutor("22222222-2")
                .build();
    }

    // -------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos retorna lista de menores")
    void listarTodos_retornaLista() {
        when(menorRepository.findAllByOrderByIdAsc()).thenReturn(List.of(menor));
        when(menorMapper.toResponseList(anyList())).thenReturn(List.of(menorResponse));

        List<MenorResponse> resultado = menorService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRut()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("listarPorTutor retorna menores del tutor")
    void listarPorTutor_retornaLista() {
        when(menorRepository.findByRutTutor("22222222-2")).thenReturn(List.of(menor));
        when(menorMapper.toResponseList(anyList())).thenReturn(List.of(menorResponse));

        List<MenorResponse> resultado = menorService.listarPorTutor("22222222-2");

        assertThat(resultado).hasSize(1);
    }

    // -------------------------------------------------------
    // BUSCAR
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId existente retorna el menor")
    void buscarPorId_existente_retornaMenor() {
        when(menorRepository.findById(1)).thenReturn(Optional.of(menor));
        when(menorMapper.toResponse(menor)).thenReturn(menorResponse);

        MenorResponse resultado = menorService.buscarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Ana García");
    }

    @Test
    @DisplayName("buscarPorId inexistente lanza EntityNotFoundException")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(menorRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menorService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Menor");
    }

    @Test
    @DisplayName("buscarPorRut existente retorna el menor")
    void buscarPorRut_existente_retornaMenor() {
        when(menorRepository.findByRut("11111111-1")).thenReturn(Optional.of(menor));
        when(menorMapper.toResponse(menor)).thenReturn(menorResponse);

        MenorResponse resultado = menorService.buscarPorRut("11111111-1");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRut()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("buscarPorRut inexistente lanza EntityNotFoundException")
    void buscarPorRut_inexistente_lanzaExcepcion() {
        when(menorRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menorService.buscarPorRut("99999999-9"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // CREAR
    // -------------------------------------------------------

    @Test
    @DisplayName("crear menor exitosamente publica evento y retorna response")
    @SuppressWarnings("null")
    void crear_exitoso_retornaResponse() {
        when(menorRepository.existsByRut("11111111-1")).thenReturn(false);
        when(menorMapper.toEntity(menorRequest)).thenReturn(menor);
        when(menorRepository.save(menor)).thenReturn(menor);
        when(menorMapper.toResponse(menor)).thenReturn(menorResponse);

        MenorResponse resultado = menorService.crear(menorRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRut()).isEqualTo("11111111-1");
        verify(menorEventProducer).sendMenorCreated(any());
    }

    @Test
    @DisplayName("crear menor con RUT duplicado lanza DuplicateResourceException")
    void crear_rutDuplicado_lanzaExcepcion() {
        // 1. Instanciar el menor existente que causará el duplicado
        Menor menorExistente = new Menor();
        menorExistente.setRut("11111111-1");
        menorExistente.setNombre("Diego Pérez"); // <-- Evita que falle al armar la excepción

        // 2. Mockear el método correcto que usa tu Service
        when(menorRepository.findByRut("11111111-1"))
                .thenReturn(Optional.of(menorExistente));

        // 3. Validar el lanzamiento de la excepción
        assertThatThrownBy(() -> menorService.crear(menorRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("11111111-1");
    }

    @Test
    @DisplayName("actualizar menor existente retorna response actualizado")
    @SuppressWarnings("null")
    void actualizar_existente_retornaActualizado() {
        when(menorRepository.findById(1)).thenReturn(Optional.of(menor));
        when(menorRepository.save(menor)).thenReturn(menor);
        when(menorMapper.toResponse(menor)).thenReturn(menorResponse);

        MenorResponse resultado = menorService.actualizar(1, menorRequest);

        assertThat(resultado).isNotNull();
        verify(menorMapper).updateEntity(menorRequest, menor);
    }

    @Test
    @DisplayName("actualizar menor inexistente lanza EntityNotFoundException")
    void actualizar_inexistente_lanzaExcepcion() {
        when(menorRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menorService.actualizar(99, menorRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar menor existente llama delete")
    @SuppressWarnings("null")
    void eliminar_existente_exitoso() {
        when(menorRepository.findById(1)).thenReturn(Optional.of(menor));

        menorService.eliminar(1);

        verify(menorRepository).delete(menor);
    }

    @Test
    @DisplayName("eliminar menor inexistente lanza EntityNotFoundException")
    void eliminar_inexistente_lanzaExcepcion() {
        when(menorRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menorService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
