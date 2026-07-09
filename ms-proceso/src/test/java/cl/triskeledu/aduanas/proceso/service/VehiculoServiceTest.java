package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.dto.VehiculoRequest;
import cl.triskeledu.aduanas.proceso.dto.VehiculoResponse;
import cl.triskeledu.aduanas.proceso.mapper.VehiculoMapper;
import cl.triskeledu.aduanas.proceso.model.Vehiculo;
import cl.triskeledu.aduanas.proceso.repository.VehiculoRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VehiculoService - Pruebas Unitarias (incluye R.3: admision + vencimiento 180 dias)")
class VehiculoServiceTest {

    @Mock private VehiculoRepository vehiculoRepository;
    @Mock private VehiculoMapper vehiculoMapper;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private VehiculoService vehiculoService;

    private Vehiculo vehiculo;
    private VehiculoResponse vehiculoResponse;
    private VehiculoRequest vehiculoRequest;

    @BeforeEach
    void setUp() {
        LocalDate hoy = LocalDate.now();
        vehiculo = Vehiculo.builder()
                .id(1).placa("ABC-1234").propietario("Juan Perez")
                .fechaIngreso(hoy).fechaVencimiento(hoy.plusDays(180))
                .build();

        vehiculoResponse = VehiculoResponse.builder()
                .id(1).placa("ABC-1234").propietario("Juan Perez")
                .fechaIngreso(hoy).fechaVencimiento(hoy.plusDays(180))
                .build();

        vehiculoRequest = VehiculoRequest.builder()
                .placa("ABC-1234").propietario("Juan Perez")
                .fechaIngreso(hoy).fechaVencimiento(hoy.plusDays(180))
                .build();
    }

    // -------------------------------------------------------
    // listarTodos
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos - retorna lista de vehiculos")
    void listarTodos_retornaLista() {
        when(vehiculoRepository.findAllByOrderByIdAsc()).thenReturn(List.of(vehiculo));
        when(vehiculoMapper.toResponseList(List.of(vehiculo))).thenReturn(List.of(vehiculoResponse));

        List<VehiculoResponse> resultado = vehiculoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPlaca()).isEqualTo("ABC-1234");
    }

    // -------------------------------------------------------
    // buscarPorId
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId - retorna vehiculo existente")
    void buscarPorId_existente_retornaVehiculo() {
        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));
        when(vehiculoMapper.toResponse(vehiculo)).thenReturn(vehiculoResponse);

        VehiculoResponse resultado = vehiculoService.buscarPorId(1);

        assertThat(resultado.getPlaca()).isEqualTo("ABC-1234");
    }

    @Test
    @DisplayName("buscarPorId - lanza EntityNotFoundException si no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(vehiculoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // -------------------------------------------------------
    // buscarPorPlaca
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorPlaca - retorna vehiculo por placa")
    void buscarPorPlaca_existente_retornaVehiculo() {
        when(vehiculoRepository.findByPlaca("ABC-1234")).thenReturn(Optional.of(vehiculo));
        when(vehiculoMapper.toResponse(vehiculo)).thenReturn(vehiculoResponse);

        VehiculoResponse resultado = vehiculoService.buscarPorPlaca("ABC-1234");

        assertThat(resultado.getPlaca()).isEqualTo("ABC-1234");
    }

    @Test
    @DisplayName("buscarPorPlaca - lanza EntityNotFoundException si placa no existe")
    void buscarPorPlaca_noExiste_lanzaExcepcion() {
        when(vehiculoRepository.findByPlaca("ZZZ-9999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.buscarPorPlaca("ZZZ-9999"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ZZZ-9999");
    }

    // -------------------------------------------------------
    // R.3 — admitirVehiculo
    // -------------------------------------------------------

    @Test
    @DisplayName("R.3: admitirVehiculo - fechaIngreso=hoy y fechaVencimiento=hoy+180 dias")
    @SuppressWarnings("null")
    void admitirVehiculo_calculaFechasCorrectamente() {
        when(vehiculoRepository.existsByPlaca("ABC-1234")).thenReturn(false);

        // Capturar el Vehiculo que se persiste para verificar las fechas
        ArgumentCaptor<Vehiculo> captor = ArgumentCaptor.forClass(Vehiculo.class);
        when(vehiculoRepository.save(captor.capture())).thenReturn(vehiculo);
        when(vehiculoMapper.toResponse(vehiculo)).thenReturn(vehiculoResponse);

        LocalDate antes = LocalDate.now();
        vehiculoService.admitirVehiculo(vehiculoRequest);
        LocalDate despues = LocalDate.now();

        Vehiculo capturado = captor.getValue();

        // fechaIngreso debe ser hoy (calculado por el sistema, no del request)
        assertThat(capturado.getFechaIngreso())
                .isAfterOrEqualTo(antes)
                .isBeforeOrEqualTo(despues);

        // fechaVencimiento debe ser exactamente fechaIngreso + 180 dias
        assertThat(capturado.getFechaVencimiento())
                .isEqualTo(capturado.getFechaIngreso().plusDays(180));
    }

    @Test
    @DisplayName("R.3: admitirVehiculo - publica VehiculoAdmitidoEvent en Kafka")
    @SuppressWarnings("null")
    void admitirVehiculo_publicaEventoKafka() {
        when(vehiculoRepository.existsByPlaca("ABC-1234")).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        when(vehiculoMapper.toResponse(vehiculo)).thenReturn(vehiculoResponse);

        vehiculoService.admitirVehiculo(vehiculoRequest);

        verify(kafkaTemplate).send(anyString(), eq("ABC-1234"), any());
    }

    @Test
    @DisplayName("R.3: admitirVehiculo - lanza DuplicateResourceException si placa ya existe")
    @SuppressWarnings("null")
    void admitirVehiculo_placaDuplicada_lanzaExcepcion() {
        when(vehiculoRepository.existsByPlaca("ABC-1234")).thenReturn(true);

        assertThatThrownBy(() -> vehiculoService.admitirVehiculo(vehiculoRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("ABC-1234");

        verify(vehiculoRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // actualizar
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar - actualiza propietario del vehiculo")
    @SuppressWarnings("null")
    void actualizar_exitoso_actualizaVehiculo() {
        VehiculoRequest requestActualizado = VehiculoRequest.builder()
                .placa("ABC-1234").propietario("Maria Garcia")
                .fechaIngreso(LocalDate.now()).fechaVencimiento(LocalDate.now().plusDays(180))
                .build();
        VehiculoResponse respuestaActualizada = VehiculoResponse.builder()
                .id(1).placa("ABC-1234").propietario("Maria Garcia").build();

        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(vehiculo)).thenReturn(vehiculo);
        when(vehiculoMapper.toResponse(vehiculo)).thenReturn(respuestaActualizada);

        VehiculoResponse resultado = vehiculoService.actualizar(1, requestActualizado);

        assertThat(resultado.getPropietario()).isEqualTo("Maria Garcia");
        verify(vehiculoRepository).save(vehiculo);
    }

    // -------------------------------------------------------
    // eliminar
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar - elimina vehiculo correctamente")
    @SuppressWarnings("null")
    void eliminar_exitoso_eliminaVehiculo() {
        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));

        vehiculoService.eliminar(1);

        verify(vehiculoRepository).delete(vehiculo);
    }

    @Test
    @DisplayName("eliminar - lanza EntityNotFoundException si no existe")
    @SuppressWarnings("null")
    void eliminar_noExiste_lanzaExcepcion() {
        when(vehiculoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);

        verify(vehiculoRepository, never()).delete(any());
    }
}
