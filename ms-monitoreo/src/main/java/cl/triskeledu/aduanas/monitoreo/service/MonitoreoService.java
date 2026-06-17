package cl.triskeledu.aduanas.monitoreo.service;

import cl.triskeledu.aduanas.monitoreo.client.*;
import cl.triskeledu.aduanas.monitoreo.dto.EstadoMsResponse;
import cl.triskeledu.aduanas.monitoreo.dto.HealthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoreoService {

    private final AuthHealthClient authHealthClient;
    private final ProcesoHealthClient procesoHealthClient;
    private final MenoresHealthClient menoresHealthClient;
    private final ReporteHealthClient reporteHealthClient;
    private final AuditoriaHealthClient auditoriaHealthClient;
    private final SagHealthClient sagHealthClient;
    private final PdiHealthClient pdiHealthClient;
    private final NotariaHealthClient notariaHealthClient;
    private final DatosHealthClient datosHealthClient;

    public List<EstadoMsResponse> listarEstadoTodos() {
        log.info("Consultando estado de todos los microservicios");
        List<EstadoMsResponse> estados = new ArrayList<>();
        estados.add(consultarEstado("ms-auth",      9001, authHealthClient::health));
        estados.add(consultarEstado("ms-proceso",   9002, procesoHealthClient::health));
        estados.add(consultarEstado("ms-menores",   9003, menoresHealthClient::health));
        estados.add(consultarEstado("ms-reporte",   9004, reporteHealthClient::health));
        estados.add(consultarEstado("ms-auditoria", 9005, auditoriaHealthClient::health));
        estados.add(consultarEstado("ms-sag",       9006, sagHealthClient::health));
        estados.add(consultarEstado("ms-pdi",       9007, pdiHealthClient::health));
        estados.add(consultarEstado("ms-notaria",   9008, notariaHealthClient::health));
        estados.add(consultarEstado("ms-datos",     9010, datosHealthClient::health));
        return estados;
    }

    public EstadoMsResponse consultarEstadoPorNombre(String nombreMs) {
        log.info("Consultando estado de: {}", nombreMs);
        return switch (nombreMs.toLowerCase()) {
            case "ms-auth"      -> consultarEstado("ms-auth",      9001, authHealthClient::health);
            case "ms-proceso"   -> consultarEstado("ms-proceso",   9002, procesoHealthClient::health);
            case "ms-menores"   -> consultarEstado("ms-menores",   9003, menoresHealthClient::health);
            case "ms-reporte"   -> consultarEstado("ms-reporte",   9004, reporteHealthClient::health);
            case "ms-auditoria" -> consultarEstado("ms-auditoria", 9005, auditoriaHealthClient::health);
            case "ms-sag"       -> consultarEstado("ms-sag",       9006, sagHealthClient::health);
            case "ms-pdi"       -> consultarEstado("ms-pdi",       9007, pdiHealthClient::health);
            case "ms-notaria"   -> consultarEstado("ms-notaria",   9008, notariaHealthClient::health);
            case "ms-datos"     -> consultarEstado("ms-datos",     9010, datosHealthClient::health);
            default -> EstadoMsResponse.builder()
                    .nombreMs(nombreMs).estado("UNKNOWN").puerto(0)
                    .detalle("Microservicio no reconocido").build();
        };
    }

    private EstadoMsResponse consultarEstado(String nombre, int puerto,
                                              java.util.function.Supplier<HealthResponse> supplier) {
        try {
            HealthResponse health = supplier.get();
            String estado = health != null && "UP".equalsIgnoreCase(health.getStatus()) ? "UP" : "DOWN";
            return EstadoMsResponse.builder()
                    .nombreMs(nombre).estado(estado).puerto(puerto)
                    .detalle("Actuator respondio correctamente").build();
        } catch (Exception e) {
            log.warn("Error consultando salud de {}: {}", nombre, e.getMessage());
            return EstadoMsResponse.builder()
                    .nombreMs(nombre).estado("DOWN").puerto(puerto)
                    .detalle("No responde: " + e.getMessage()).build();
        }
    }
}
