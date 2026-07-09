package cl.triskeledu.aduanas.auditoria.controller;

import cl.triskeledu.aduanas.auditoria.dto.DetalleLogResponse;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.service.LogEventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * R.14 — API de consulta de trazabilidad (SOLO LECTURA / INMUTABLE).
 *
 * Los registros de auditoria son escritos exclusivamente por AuditoriaEventListener
 * al consumir eventos Kafka. Ningun endpoint expone escritura (POST/PUT/DELETE)
 * para garantizar la inmutabilidad exigida por R.14.
 */
@Tag(name = "Auditoria", description = "Registro inmutable de eventos del sistema via Kafka")
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogEventoController {

    private final LogEventoService logEventoService;

    @Operation(summary = "Listar todos los eventos de auditoria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<LogEventoResponse>>> listarTodos() {
        List<EntityModel<LogEventoResponse>> items = logEventoService.listarTodos().stream()
                .map(l -> EntityModel.of(l,
                        linkTo(methodOn(LogEventoController.class).buscarPorId(l.getId())).withSelfRel(),
                        linkTo(methodOn(LogEventoController.class).listarTodos()).withRel("collection"),
                        linkTo(methodOn(LogEventoController.class).listarDetalles(l.getId())).withRel("detalles")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(LogEventoController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar evento de auditoria por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<LogEventoResponse>> buscarPorId(@PathVariable Integer id) {
        LogEventoResponse response = logEventoService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(LogEventoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(LogEventoController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(LogEventoController.class).listarDetalles(id)).withRel("detalles")));
    }

    @Operation(summary = "Listar eventos de auditoria de un oficial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de eventos del oficial")
    })
    @GetMapping("/oficial/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<LogEventoResponse>>> listarPorOficial(@PathVariable String rut) {
        List<EntityModel<LogEventoResponse>> items = logEventoService.listarPorOficial(rut).stream()
                .map(l -> EntityModel.of(l,
                        linkTo(methodOn(LogEventoController.class).buscarPorId(l.getId())).withSelfRel(),
                        linkTo(methodOn(LogEventoController.class).listarTodos()).withRel("collection"),
                        linkTo(methodOn(LogEventoController.class).listarDetalles(l.getId())).withRel("detalles")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(LogEventoController.class).listarPorOficial(rut)).withSelfRel()));
    }

    @Operation(summary = "Listar eventos por microservicio de origen")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de eventos del microservicio")
    })
    @GetMapping("/ms/{msOrigen}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<LogEventoResponse>>> listarPorMs(@PathVariable String msOrigen) {
        List<EntityModel<LogEventoResponse>> items = logEventoService.listarPorMs(msOrigen).stream()
                .map(l -> EntityModel.of(l,
                        linkTo(methodOn(LogEventoController.class).buscarPorId(l.getId())).withSelfRel(),
                        linkTo(methodOn(LogEventoController.class).listarTodos()).withRel("collection"),
                        linkTo(methodOn(LogEventoController.class).listarDetalles(l.getId())).withRel("detalles")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(LogEventoController.class).listarPorMs(msOrigen)).withSelfRel()));
    }

    @Operation(summary = "Ver campos auditados de un evento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalles del evento"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/{id}/detalles")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<DetalleLogResponse>>> listarDetalles(@PathVariable Integer id) {
        List<EntityModel<DetalleLogResponse>> items = logEventoService.listarDetallesPorLog(id).stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(LogEventoController.class).buscarPorId(id)).withRel("log")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(LogEventoController.class).listarDetalles(id)).withSelfRel(),
                linkTo(methodOn(LogEventoController.class).buscarPorId(id)).withRel("log")));
    }

    @Operation(summary = "Eliminar registro de auditoria")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        logEventoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
