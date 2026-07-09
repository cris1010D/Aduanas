package cl.triskeledu.aduanas.monitoreo.controller;

import cl.triskeledu.aduanas.monitoreo.dto.EstadoMsResponse;
import cl.triskeledu.aduanas.monitoreo.dto.SistemaSaludResponse;
import cl.triskeledu.aduanas.monitoreo.service.MonitoreoService;
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

@Tag(name = "Monitoreo", description = "Estado de salud en tiempo real de todos los microservicios")
@RestController
@RequestMapping("/api/v1/monitoreo")
@RequiredArgsConstructor
public class MonitoreoController {

    private final MonitoreoService monitoreoService;

    @Operation(summary = "Estado de salud de todos los microservicios")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estados obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<EstadoMsResponse>>> listarEstadoTodos() {
        List<EntityModel<EstadoMsResponse>> items = monitoreoService.listarEstadoTodos().stream()
                .map(e -> EntityModel.of(e,
                        linkTo(methodOn(MonitoreoController.class).consultarEstadoPorNombre(e.getNombreMs())).withSelfRel(),
                        linkTo(methodOn(MonitoreoController.class).listarEstadoTodos()).withRel("collection"),
                        linkTo(methodOn(MonitoreoController.class).getSistemaSalud()).withRel("dashboard")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(MonitoreoController.class).listarEstadoTodos()).withSelfRel()));
    }

    @Operation(summary = "Dashboard consolidado de salud del sistema (OPERATIVO / DEGRADADO / CRITICO)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dashboard de salud generado correctamente")
    })
    @GetMapping("/status")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<SistemaSaludResponse>> getSistemaSalud() {
        SistemaSaludResponse response = monitoreoService.getSistemaSalud();
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MonitoreoController.class).getSistemaSalud()).withSelfRel(),
                linkTo(methodOn(MonitoreoController.class).listarEstadoTodos()).withRel("microservicios")));
    }

    @Operation(summary = "Estado de salud de un microservicio especifico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado del microservicio"),
        @ApiResponse(responseCode = "404", description = "Microservicio no encontrado")
    })
    @GetMapping("/{nombreMs}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<EstadoMsResponse>> consultarEstadoPorNombre(@PathVariable String nombreMs) {
        EstadoMsResponse response = monitoreoService.consultarEstadoPorNombre(nombreMs);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MonitoreoController.class).consultarEstadoPorNombre(nombreMs)).withSelfRel(),
                linkTo(methodOn(MonitoreoController.class).listarEstadoTodos()).withRel("collection"),
                linkTo(methodOn(MonitoreoController.class).getSistemaSalud()).withRel("dashboard")));
    }
}
