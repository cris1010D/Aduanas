package cl.triskeledu.aduanas.auditoria.event;

import cl.triskeledu.aduanas.auditoria.model.DetalleLog;
import cl.triskeledu.aduanas.auditoria.service.AuditoriaService;
import cl.triskeledu.common.event.AntecedenteConsultadoEvent;
import cl.triskeledu.common.event.AntecedenteCreatedEvent;
import cl.triskeledu.common.event.DeclaracionCreatedEvent;
import cl.triskeledu.common.event.MenorAutorizadoEvent;
import cl.triskeledu.common.event.SalidaDiplomaticaCreatedEvent;
import cl.triskeledu.common.event.VehiculoAdmitidoEvent;
import cl.triskeledu.common.event.ViajeroCreatedEvent;
import cl.triskeledu.common.event.ViajeroDeletedEvent;
import cl.triskeledu.common.event.ViajeroUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static cl.triskeledu.aduanas.auditoria.service.AuditoriaService.detalle;

/**
 * R.14 — Trazabilidad del sistema.
 *
 * Escucha TODOS los topicos Kafka del ecosistema y persiste en ms-auditoria
 * un registro inmutable compuesto por:
 *   - log_evento  : quien, que accion, cuando, desde que MS
 *   - detalle_log : entidad afectada, campo, valor nuevo
 *
 * Topicos cubiertos (9):
 *   proceso.viajero.created          → ViajeroCreatedEvent
 *   proceso.viajero.updated          → ViajeroUpdatedEvent
 *   proceso.viajero.deleted          → ViajeroDeletedEvent
 *   proceso.salida_diplomatica.created → SalidaDiplomaticaCreatedEvent
 *   proceso.vehiculo.admitido        → VehiculoAdmitidoEvent
 *   proceso.menor.autorizado         → MenorAutorizadoEvent
 *   proceso.antecedente.consultado   → AntecedenteConsultadoEvent
 *   sag.declaracion.created          → DeclaracionCreatedEvent
 *   pdi.antecedente.created          → AntecedenteCreatedEvent
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditoriaEventListener {

    private static final String GROUP = "ms-auditoria";
    private static final String SISTEMA = "SISTEMA";

    private final AuditoriaService auditoriaService;

    // -------------------------------------------------------
    // ms-proceso: VIAJEROS
    // -------------------------------------------------------

    @KafkaListener(topics = "proceso.viajero.created", groupId = GROUP)
    public void onViajeroCreado(ViajeroCreatedEvent event) {
        log.info("[AUDITORIA] ViajeroCreatedEvent recibido para rut: {}", event.getRut());
        List<DetalleLog> detalles = List.of(
            detalle("Viajero", "rut",          event.getRut()),
            detalle("Viajero", "nombre",        event.getNombre()),
            detalle("Viajero", "pasaporte",     event.getPasaporte()),
            detalle("Viajero", "nacionalidad",  event.getNacionalidad())
        );
        auditoriaService.registrarEvento(SISTEMA, "VIAJERO_CREADO", "ms-proceso", detalles);
    }

    @KafkaListener(topics = "proceso.viajero.updated", groupId = GROUP)
    public void onViajeroActualizado(ViajeroUpdatedEvent event) {
        log.info("[AUDITORIA] ViajeroUpdatedEvent recibido para rut: {}", event.getRut());
        List<DetalleLog> detalles = List.of(
            detalle("Viajero", "rut",          event.getRut()),
            detalle("Viajero", "nombre",        event.getNombre()),
            detalle("Viajero", "pasaporte",     event.getPasaporte()),
            detalle("Viajero", "nacionalidad",  event.getNacionalidad())
        );
        auditoriaService.registrarEvento(SISTEMA, "VIAJERO_ACTUALIZADO", "ms-proceso", detalles);
    }

    @KafkaListener(topics = "proceso.viajero.deleted", groupId = GROUP)
    public void onViajeroEliminado(ViajeroDeletedEvent event) {
        log.info("[AUDITORIA] ViajeroDeletedEvent recibido para rut: {}", event.getRut());
        List<DetalleLog> detalles = List.of(
            detalle("Viajero", "rut", event.getRut())
        );
        auditoriaService.registrarEvento(SISTEMA, "VIAJERO_ELIMINADO", "ms-proceso", detalles);
    }

    // -------------------------------------------------------
    // ms-proceso: SALIDA DIPLOMATICA
    // -------------------------------------------------------

    @KafkaListener(topics = "proceso.salida_diplomatica.created", groupId = GROUP)
    public void onSalidaDiplomatica(SalidaDiplomaticaCreatedEvent event) {
        log.info("[AUDITORIA] SalidaDiplomaticaCreatedEvent recibido para rut: {}", event.getRutViajero());
        List<DetalleLog> detalles = List.of(
            detalle("SalidaDiplomatica", "rutViajero",        event.getRutViajero()),
            detalle("SalidaDiplomatica", "tipoAcreditacion",  event.getTipoAcreditacion()),
            detalle("SalidaDiplomatica", "pasoFronterizo",    event.getPasoFronterizo()),
            detalle("SalidaDiplomatica", "fecha",             event.getFecha())
        );
        auditoriaService.registrarEvento(SISTEMA, "SALIDA_DIPLOMATICA_CREADA", "ms-proceso", detalles);
    }

    // -------------------------------------------------------
    // ms-proceso: VEHICULO
    // -------------------------------------------------------

    @KafkaListener(topics = "proceso.vehiculo.admitido", groupId = GROUP)
    public void onVehiculoAdmitido(VehiculoAdmitidoEvent event) {
        log.info("[AUDITORIA] VehiculoAdmitidoEvent recibido para placa: {}", event.getPlaca());
        List<DetalleLog> detalles = List.of(
            detalle("Vehiculo", "placa",             event.getPlaca()),
            detalle("Vehiculo", "propietario",       event.getPropietario()),
            detalle("Vehiculo", "fechaIngreso",      event.getFechaIngreso()),
            detalle("Vehiculo", "fechaVencimiento",  event.getFechaVencimiento())
        );
        auditoriaService.registrarEvento(SISTEMA, "VEHICULO_ADMITIDO", "ms-proceso", detalles);
    }

    // -------------------------------------------------------
    // ms-proceso: MENOR
    // -------------------------------------------------------

    @KafkaListener(topics = "proceso.menor.autorizado", groupId = GROUP)
    public void onMenorAutorizado(MenorAutorizadoEvent event) {
        log.info("[AUDITORIA] MenorAutorizadoEvent recibido para menor: {}", event.getRutMenor());
        // rutTutor actua como responsable de la autorizacion
        List<DetalleLog> detalles = List.of(
            detalle("AutorizacionMenor", "rutMenor",          event.getRutMenor()),
            detalle("AutorizacionMenor", "rutTutor",          event.getRutTutor()),
            detalle("AutorizacionMenor", "tipo",              event.getTipo()),
            detalle("AutorizacionMenor", "fechaVencimiento",  event.getFechaVencimiento())
        );
        auditoriaService.registrarEvento(event.getRutTutor(), "MENOR_AUTORIZADO", "ms-proceso", detalles);
    }

    // -------------------------------------------------------
    // ms-proceso: ANTECEDENTE PDI CONSULTADO
    // -------------------------------------------------------

    @KafkaListener(topics = "proceso.antecedente.consultado", groupId = GROUP)
    public void onAntecedenteConsultado(AntecedenteConsultadoEvent event) {
        log.info("[AUDITORIA] AntecedenteConsultadoEvent recibido para rut: {} | oficial: {}",
            event.getRut(), event.getRutOficial());
        List<DetalleLog> detalles = List.of(
            detalle("AntecedenteConsultado", "rut",        event.getRut()),
            detalle("AntecedenteConsultado", "resultado",  event.getResultado()),
            detalle("AntecedenteConsultado", "fuente",     event.getFuente()),
            detalle("AntecedenteConsultado", "fecha",      event.getFecha())
        );
        auditoriaService.registrarEvento(
            event.getRutOficial(), "ANTECEDENTE_PDI_CONSULTADO", "ms-proceso", detalles);
    }

    // -------------------------------------------------------
    // ms-sag: DECLARACION SAG
    // -------------------------------------------------------

    @KafkaListener(topics = "sag.declaracion.created", groupId = GROUP)
    public void onDeclaracionSag(DeclaracionCreatedEvent event) {
        log.info("[AUDITORIA] DeclaracionCreatedEvent recibido para viajero: {}", event.getRutViajero());
        String estado  = event.getEstado() != null ? event.getEstado() : "DESCONOCIDO";
        String accion  = "DECLARACION_SAG_" + estado;          // DECLARACION_SAG_CUARENTENA | _APROBADA
        String fechaStr = event.getFecha() != null ? event.getFecha().toString() : null;
        List<DetalleLog> detalles = List.of(
            detalle("DeclaracionSAG", "rutViajero",     event.getRutViajero()),
            detalle("DeclaracionSAG", "estado",         estado),
            detalle("DeclaracionSAG", "pasoFronterizo", event.getPasoFronterizo()),
            detalle("DeclaracionSAG", "fecha",          fechaStr)
        );
        auditoriaService.registrarEvento(SISTEMA, accion, "ms-sag", detalles);
    }

    // -------------------------------------------------------
    // ms-pdi: ANTECEDENTE REGISTRADO
    // -------------------------------------------------------

    @KafkaListener(topics = "pdi.antecedente.created", groupId = GROUP)
    public void onAntecedenteCreado(AntecedenteCreatedEvent event) {
        log.info("[AUDITORIA] AntecedenteCreatedEvent recibido para rut: {}", event.getRut());
        String fechaStr = event.getFechaConsulta() != null ? event.getFechaConsulta().toString() : null;
        List<DetalleLog> detalles = List.of(
            detalle("AntecedentePDI", "rut",          event.getRut()),
            detalle("AntecedentePDI", "resultado",    event.getResultado()),
            detalle("AntecedentePDI", "fuente",       event.getFuente()),
            detalle("AntecedentePDI", "fechaConsulta", fechaStr)
        );
        auditoriaService.registrarEvento(SISTEMA, "ANTECEDENTE_PDI_REGISTRADO", "ms-pdi", detalles);
    }
}
