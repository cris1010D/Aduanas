package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.dto.ViajeroRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroResponse;
import cl.triskeledu.aduanas.proceso.event.ViajeroEventProducer;
import cl.triskeledu.aduanas.proceso.mapper.ViajeroMapper;
import cl.triskeledu.aduanas.proceso.model.Viajero;
import cl.triskeledu.aduanas.proceso.repository.MovimientoRepository;
import cl.triskeledu.aduanas.proceso.repository.ViajeroRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
<<<<<<< HEAD
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertThrows;
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
<<<<<<< HEAD
@MockitoSettings(strictness = Strictness.LENIENT)
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
@DisplayName("ViajeroService - Pruebas Unitarias")
class ViajeroServiceTest {

    @Mock private ViajeroRepository viajeroRepository;
    @Mock private ViajeroMapper viajeroMapper;
    @Mock private ViajeroEventProducer viajeroEventProducer;
    @Mock private MovimientoRepository movimientoRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private ViajeroService viajeroService;

    private Viajero viajero;
    private ViajeroResponse viajeroResponse;
    private ViajeroRequest viajeroRequest;

    @BeforeEach
    void setUp() {
        viajero = Viajero.builder()
                .id(1).rut("12345678-9").nombre("Carlos Fuentes")
                .pasaporte("A1234567").nacionalidad("Chilena")
                .build();

        viajeroResponse = ViajeroResponse.builder()
                .id(1).rut("12345678-9").nombre("Carlos Fuentes")
                .pasaporte("A1234567").nacionalidad("Chilena")
                .build();

        viajeroRequest = ViajeroRequest.builder()
                .rut("12345678-9").nombre("Carlos Fuentes")
                .pasaporte("A1234567").nacionalidad("Chilena")
                .build();
    }

    // -------------------------------------------------------
    // listarTodos
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos - retorna lista de viajeros")
    void listarTodos_retornaLista() {
        when(viajeroRepository.findAllByOrderByIdAsc()).thenReturn(List.of(viajero));
        when(viajeroMapper.toResponseList(List.of(viajero))).thenReturn(List.of(viajeroResponse));

        List<ViajeroResponse> resultado = viajeroService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRut()).isEqualTo("12345678-9");
        verify(viajeroRepository).findAllByOrderByIdAsc();
    }

    // -------------------------------------------------------
    // buscarPorId
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId - retorna viajero existente")
    void buscarPorId_existente_retornaViajero() {
        when(viajeroRepository.findById(1)).thenReturn(Optional.of(viajero));
        when(viajeroMapper.toResponse(viajero)).thenReturn(viajeroResponse);

        ViajeroResponse resultado = viajeroService.buscarPorId(1);

        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("Carlos Fuentes");
    }

    @Test
    @DisplayName("buscarPorId - lanza EntityNotFoundException si no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(viajeroRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> viajeroService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // -------------------------------------------------------
    // buscarPorRut
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorRut - retorna viajero por rut")
    void buscarPorRut_existente_retornaViajero() {
        when(viajeroRepository.findByRut("12345678-9")).thenReturn(Optional.of(viajero));
        when(viajeroMapper.toResponse(viajero)).thenReturn(viajeroResponse);

        ViajeroResponse resultado = viajeroService.buscarPorRut("12345678-9");

        assertThat(resultado.getRut()).isEqualTo("12345678-9");
    }

    @Test
    @DisplayName("buscarPorRut - lanza EntityNotFoundException si rut no existe")
    void buscarPorRut_noExiste_lanzaExcepcion() {
        when(viajeroRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> viajeroService.buscarPorRut("99999999-9"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99999999-9");
    }

    // -------------------------------------------------------
    // crear
    // -------------------------------------------------------

    @Test
    @DisplayName("crear - persiste viajero y publica evento Kafka")
    @SuppressWarnings("null")
    void crear_exitoso_guardaYPublicaEvento() {
        when(viajeroRepository.existsByRut("12345678-9")).thenReturn(false);
        when(viajeroRepository.existsByPasaporte("A1234567")).thenReturn(false);
        when(viajeroMapper.toEntity(viajeroRequest)).thenReturn(viajero);
        when(viajeroRepository.save(viajero)).thenReturn(viajero);
        when(viajeroMapper.toResponse(viajero)).thenReturn(viajeroResponse);

        ViajeroResponse resultado = viajeroService.crear(viajeroRequest);

        assertThat(resultado.getRut()).isEqualTo("12345678-9");
        verify(viajeroRepository).save(viajero);
        verify(viajeroEventProducer).sendViajeroCreated(any());
    }

    @Test
    @DisplayName("crear - lanza DuplicateResourceException si rut ya existe")
    @SuppressWarnings("null")
    void crear_rutDuplicado_lanzaExcepcion() {
<<<<<<< HEAD
        // 1. Crear el request que envía el usuario
        ViajeroRequest request = new ViajeroRequest();
        request.setRut("12345678-9");

        // 2. Crear la entidad simulada que ya existe en la Base de Datos
        Viajero viajeroExistente = new Viajero();
        viajeroExistente.setRut("12345678-9");
        viajeroExistente.setNombre("Juan Pérez"); // <-- IMPORTANTE: ESTO EVITA EL NULL POINTER!

        // 3. Configurar el Mockito stubbing para la validación exacta
        when(viajeroRepository.findByRut("12345678-9"))
                .thenReturn(Optional.of(viajeroExistente));

        // 4. Verificar que lance la excepción correcta de Aduanas
        assertThrows(DuplicateResourceException.class, () -> {
            viajeroService.crear(request);
        });
=======
        when(viajeroRepository.existsByRut("12345678-9")).thenReturn(true);

        assertThatThrownBy(() -> viajeroService.crear(viajeroRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("12345678-9");

        verify(viajeroRepository, never()).save(any());
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
    }

    @Test
    @DisplayName("crear sin pasaporte - no valida pasaporte duplicado")
    @SuppressWarnings("null")
    void crear_sinPasaporte_noValidaPasaporte() {
        ViajeroRequest sinPasaporte = ViajeroRequest.builder()
                .rut("98765432-1").nombre("Ana Lopez")
                .pasaporte(null).nacionalidad("Argentina")
                .build();
        Viajero viajeroSinPasaporte = Viajero.builder()
                .id(2).rut("98765432-1").nombre("Ana Lopez").nacionalidad("Argentina").build();
        ViajeroResponse respuestaSinPasaporte = ViajeroResponse.builder()
                .id(2).rut("98765432-1").nombre("Ana Lopez").nacionalidad("Argentina").build();

        when(viajeroRepository.existsByRut("98765432-1")).thenReturn(false);
        when(viajeroMapper.toEntity(sinPasaporte)).thenReturn(viajeroSinPasaporte);
        when(viajeroRepository.save(viajeroSinPasaporte)).thenReturn(viajeroSinPasaporte);
        when(viajeroMapper.toResponse(viajeroSinPasaporte)).thenReturn(respuestaSinPasaporte);

        ViajeroResponse resultado = viajeroService.crear(sinPasaporte);

        assertThat(resultado.getRut()).isEqualTo("98765432-1");
        verify(viajeroRepository, never()).existsByPasaporte(any());
    }

    // -------------------------------------------------------
    // actualizar
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar - actualiza datos y publica evento Kafka")
    @SuppressWarnings("null")
    void actualizar_exitoso_actualizaYPublicaEvento() {
        ViajeroRequest requestActualizado = ViajeroRequest.builder()
                .rut("12345678-9").nombre("Carlos Fuentes Actualizado")
                .pasaporte("A1234567").nacionalidad("Chilena").build();
        Viajero viajeroActualizado = Viajero.builder()
                .id(1).rut("12345678-9").nombre("Carlos Fuentes Actualizado")
                .pasaporte("A1234567").nacionalidad("Chilena").build();
        ViajeroResponse respuestaActualizada = ViajeroResponse.builder()
                .id(1).rut("12345678-9").nombre("Carlos Fuentes Actualizado").build();

        when(viajeroRepository.findById(1)).thenReturn(Optional.of(viajero));
        when(viajeroRepository.save(viajero)).thenReturn(viajeroActualizado);
        when(viajeroMapper.toResponse(viajeroActualizado)).thenReturn(respuestaActualizada);

        ViajeroResponse resultado = viajeroService.actualizar(1, requestActualizado);

        assertThat(resultado.getNombre()).isEqualTo("Carlos Fuentes Actualizado");
        verify(viajeroEventProducer).sendViajeroUpdated(any());
    }

    // -------------------------------------------------------
    // eliminar
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar - elimina viajero y publica evento Kafka")
    @SuppressWarnings("null")
    void eliminar_exitoso_eliminaYPublicaEvento() {
        when(viajeroRepository.findById(1)).thenReturn(Optional.of(viajero));

        viajeroService.eliminar(1);

        verify(viajeroRepository).delete(viajero);
        verify(viajeroEventProducer).sendViajeroDeleted(any());
    }

    @Test
    @DisplayName("eliminar - lanza EntityNotFoundException si no existe")
    @SuppressWarnings("null")
    void eliminar_noExiste_lanzaExcepcion() {
        when(viajeroRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> viajeroService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);

        verify(viajeroRepository, never()).delete(any());
    }
}
