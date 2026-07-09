package cl.triskeledu.aduanas.monitoreo.service;

import cl.triskeledu.aduanas.monitoreo.client.*;
import cl.triskeledu.aduanas.monitoreo.dto.EstadoMsResponse;
import cl.triskeledu.aduanas.monitoreo.dto.HealthResponse;
import cl.triskeledu.aduanas.monitoreo.dto.SistemaSaludResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MonitoreoService - Pruebas Unitarias (salud centralizada del ecosistema)")
class MonitoreoServiceTest {

    @Mock private AuthHealthClient      authHealthClient;
    @Mock private ProcesoHealthClient   procesoHealthClient;
    @Mock private MenoresHealthClient   menoresHealthClient;
    @Mock private ReporteHealthClient   reporteHealthClient;
    @Mock private AuditoriaHealthClient auditoriaHealthClient;
    @Mock private SagHealthClient       sagHealthClient;
    @Mock private PdiHealthClient       pdiHealthClient;
    @Mock private NotariaHealthClient   notariaHealthClient;
    @Mock private DatosHealthClient     datosHealthClient;
    @Mock private EurekaHealthClient    eurekaHealthClient;

    @InjectMocks
    private MonitoreoService monitoreoService;

    private HealthResponse healthUp;

    @BeforeEach
    void setUp() {
        healthUp = HealthResponse.builder().status("UP").build();
    }

    // -------------------------------------------------------
    // Helper: configura todos los clientes como UP
    // -------------------------------------------------------

    private void todosLosClientesUp() {
        when(authHealthClient.health()).thenReturn(healthUp);
        when(procesoHealthClient.health()).thenReturn(healthUp);
        when(menoresHealthClient.health()).thenReturn(healthUp);
        when(reporteHealthClient.health()).thenReturn(healthUp);
        when(auditoriaHealthClient.health()).thenReturn(healthUp);
        when(sagHealthClient.health()).thenReturn(healthUp);
        when(pdiHealthClient.health()).thenReturn(healthUp);
        when(notariaHealthClient.health()).thenReturn(healthUp);
        when(datosHealthClient.health()).thenReturn(healthUp);
        when(eurekaHealthClient.health()).thenReturn(healthUp);
    }

    // -------------------------------------------------------
    // listarEstadoTodos
    // -------------------------------------------------------

    @Test
    @DisplayName("listarEstadoTodos - retorna 10 estados (9 MS + eureka)")
    void listarEstadoTodos_retornaDiezEstados() {
        todosLosClientesUp();

        List<EstadoMsResponse> estados = monitoreoService.listarEstadoTodos();

        assertThat(estados).hasSize(10);
    }

    @Test
    @DisplayName("listarEstadoTodos - todos los MS responden UP")
    void listarEstadoTodos_todosUp_retornaEstadoUpParaTodos() {
        todosLosClientesUp();

        List<EstadoMsResponse> estados = monitoreoService.listarEstadoTodos();

        assertThat(estados).allMatch(e -> "UP".equals(e.getEstado()));
    }

    @Test
    @DisplayName("listarEstadoTodos - cliente lanza excepcion → estado DOWN con detalle")
    void listarEstadoTodos_clienteLanzaExcepcion_retornaDown() {
        todosLosClientesUp();
        when(authHealthClient.health()).thenThrow(new RuntimeException("Connection refused"));

        List<EstadoMsResponse> estados = monitoreoService.listarEstadoTodos();

        EstadoMsResponse authEstado = estados.stream()
                .filter(e -> "ms-auth".equals(e.getNombreMs()))
                .findFirst().orElseThrow();

        assertThat(authEstado.getEstado()).isEqualTo("DOWN");
        assertThat(authEstado.getDetalle()).contains("Connection refused");
    }

    // -------------------------------------------------------
    // consultarEstadoPorNombre
    // -------------------------------------------------------

    @Test
    @DisplayName("consultarEstadoPorNombre - ms-auth retorna estado correcto")
    void consultarEstadoPorNombre_msAuth_retornaEstado() {
        when(authHealthClient.health()).thenReturn(healthUp);

        EstadoMsResponse resultado = monitoreoService.consultarEstadoPorNombre("ms-auth");

        assertThat(resultado.getNombreMs()).isEqualTo("ms-auth");
        assertThat(resultado.getEstado()).isEqualTo("UP");
        assertThat(resultado.getPuerto()).isEqualTo(9001);
    }

    @Test
    @DisplayName("consultarEstadoPorNombre - ms-datos retorna puerto 9010")
    void consultarEstadoPorNombre_msDatos_retornaPuerto9010() {
        when(datosHealthClient.health()).thenReturn(healthUp);

        EstadoMsResponse resultado = monitoreoService.consultarEstadoPorNombre("ms-datos");

        assertThat(resultado.getPuerto()).isEqualTo(9010);
        assertThat(resultado.getEstado()).isEqualTo("UP");
    }

    @Test
    @DisplayName("consultarEstadoPorNombre - MS no reconocido retorna UNKNOWN")
    void consultarEstadoPorNombre_desconocido_retornaUnknown() {
        EstadoMsResponse resultado = monitoreoService.consultarEstadoPorNombre("ms-inexistente");

        assertThat(resultado.getEstado()).isEqualTo("UNKNOWN");
        assertThat(resultado.getPuerto()).isEqualTo(0);
    }

    // -------------------------------------------------------
    // getSistemaSalud — lógica de clasificación
    // -------------------------------------------------------

    @Test
    @DisplayName("getSistemaSalud - todos UP → estadoGeneral=OPERATIVO y disponibilidad=100%")
    void getSistemaSalud_todosUp_retornaOperativo() {
        todosLosClientesUp();

        SistemaSaludResponse resultado = monitoreoService.getSistemaSalud();

        assertThat(resultado.getEstadoGeneral()).isEqualTo("OPERATIVO");
        assertThat(resultado.getServiciosDown()).isEqualTo(0);
        assertThat(resultado.getPorcentajeDisponibilidad()).isEqualTo(100.0);
        assertThat(resultado.getEstados()).hasSize(10);
    }

    @Test
    @DisplayName("getSistemaSalud - un MS DOWN → estadoGeneral=DEGRADADO")
    void getSistemaSalud_unMsDown_retornaDegradado() {
        todosLosClientesUp();
        when(datosHealthClient.health()).thenThrow(new RuntimeException("Timeout"));

        SistemaSaludResponse resultado = monitoreoService.getSistemaSalud();

        assertThat(resultado.getEstadoGeneral()).isEqualTo("DEGRADADO");
        assertThat(resultado.getServiciosDown()).isEqualTo(1);
        assertThat(resultado.getServiciosUp()).isEqualTo(9);
    }

    @Test
    @DisplayName("getSistemaSalud - todos DOWN → estadoGeneral=CRITICO y disponibilidad=0%")
    void getSistemaSalud_todosDown_retornaCritico() {
        when(authHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(procesoHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(menoresHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(reporteHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(auditoriaHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(sagHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(pdiHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(notariaHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(datosHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(eurekaHealthClient.health()).thenThrow(new RuntimeException("down"));

        SistemaSaludResponse resultado = monitoreoService.getSistemaSalud();

        assertThat(resultado.getEstadoGeneral()).isEqualTo("CRITICO");
        assertThat(resultado.getServiciosUp()).isEqualTo(0);
        assertThat(resultado.getPorcentajeDisponibilidad()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getSistemaSalud - calcula porcentaje de disponibilidad correctamente")
    void getSistemaSalud_calculaPorcentajeCorrectamente() {
        // 5 UP de 10 total = 50%
        when(authHealthClient.health()).thenReturn(healthUp);
        when(procesoHealthClient.health()).thenReturn(healthUp);
        when(menoresHealthClient.health()).thenReturn(healthUp);
        when(reporteHealthClient.health()).thenReturn(healthUp);
        when(auditoriaHealthClient.health()).thenReturn(healthUp);
        when(sagHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(pdiHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(notariaHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(datosHealthClient.health()).thenThrow(new RuntimeException("down"));
        when(eurekaHealthClient.health()).thenThrow(new RuntimeException("down"));

        SistemaSaludResponse resultado = monitoreoService.getSistemaSalud();

        assertThat(resultado.getPorcentajeDisponibilidad()).isEqualTo(50.0);
        assertThat(resultado.getEstadoGeneral()).isEqualTo("DEGRADADO");
    }

    @Test
    @DisplayName("getSistemaSalud - incluye timestamp de consulta")
    void getSistemaSalud_incluyeFechaConsulta() {
        todosLosClientesUp();

        SistemaSaludResponse resultado = monitoreoService.getSistemaSalud();

        assertThat(resultado.getFechaConsulta()).isNotNull();
    }
}
