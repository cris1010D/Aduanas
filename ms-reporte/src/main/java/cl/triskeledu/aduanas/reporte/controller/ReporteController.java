package cl.triskeledu.aduanas.reporte.controller;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.service.ReporteExportService;
import cl.triskeledu.aduanas.reporte.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Reportes", description = "Generacion y exportacion de reportes (PDF/Excel - R.7/R.8)")
@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService       reporteService;
    private final ReporteExportService reporteExportService;

    @Operation(summary = "Listar todos los reportes generados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    //@SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ReporteResponse>>> listarTodos() {
        List<EntityModel<ReporteResponse>> items = reporteService.listarTodos().stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(ReporteController.class).buscarPorId(r.getId())).withSelfRel(),
                        linkTo(methodOn(ReporteController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ReporteController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar reporte por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    //@SuppressWarnings("null")
    public ResponseEntity<EntityModel<ReporteResponse>> buscarPorId(@PathVariable Integer id) {
        ReporteResponse response = reporteService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ReporteController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(ReporteController.class).listarPorOficial(response.getRutOficial())).withRel("por-oficial")));
    }

    @Operation(summary = "Listar reportes generados por un oficial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reportes del oficial")
    })
    @GetMapping("/oficial/{rut}")
    //@SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ReporteResponse>>> listarPorOficial(@PathVariable String rut) {
        List<EntityModel<ReporteResponse>> items = reporteService.listarPorOficial(rut).stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(ReporteController.class).buscarPorId(r.getId())).withSelfRel(),
                        linkTo(methodOn(ReporteController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ReporteController.class).listarPorOficial(rut)).withSelfRel()));
    }

    @Operation(summary = "Generar nuevo reporte")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reporte generado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    //@SuppressWarnings("null")
    public ResponseEntity<EntityModel<ReporteResponse>> crear(@Valid @RequestBody ReporteRequest request) {
        ReporteResponse response = reporteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(ReporteController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar reporte existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    //@SuppressWarnings("null")
    public ResponseEntity<EntityModel<ReporteResponse>> actualizar(@PathVariable Integer id,
                                                                    @Valid @RequestBody ReporteRequest request) {
        ReporteResponse response = reporteService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ReporteController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar reporte")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reporte eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Exportar reporte en formato PDF o Excel (R.7/R.8)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo generado correctamente"),
        @ApiResponse(responseCode = "400", description = "Formato no soportado (usar pdf o excel)")
    })
    @GetMapping("/exportar/{formato}")
    public ResponseEntity<byte[]> exportar(@PathVariable String formato,
                                           @RequestParam(required = false) String rutOficial) {
        return reporteExportService.exportar(formato, rutOficial);
    }
}
