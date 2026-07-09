package cl.triskeledu.aduanas.auth.controller;

import cl.triskeledu.aduanas.auth.dto.SesionRequest;
import cl.triskeledu.aduanas.auth.dto.SesionResponse;
import cl.triskeledu.aduanas.auth.service.SesionService;
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

@Tag(name = "Sesiones", description = "Control y consulta de sesiones activas")
@RestController
@RequestMapping("/api/v1/sesiones")
@RequiredArgsConstructor
public class SesionController {

    private final SesionService sesionService;

    @Operation(summary = "Listar todas las sesiones activas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<SesionResponse>>> listarTodas() {
        List<EntityModel<SesionResponse>> items = sesionService.listarTodas().stream()
                .map(s -> EntityModel.of(s,
                        linkTo(methodOn(SesionController.class).buscarPorId(s.getId())).withSelfRel(),
                        linkTo(methodOn(SesionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(SesionController.class).listarTodas()).withSelfRel()));
    }

    @Operation(summary = "Buscar sesion por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sesion encontrada"),
        @ApiResponse(responseCode = "404", description = "Sesion no encontrada")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<SesionResponse>> buscarPorId(@PathVariable Integer id) {
        SesionResponse response = sesionService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(SesionController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(SesionController.class).listarTodas()).withRel("collection"),
                linkTo(methodOn(SesionController.class).listarPorOficial(response.getRutOficial())).withRel("por-oficial")));
    }

    @Operation(summary = "Listar sesiones de un oficial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de sesiones del oficial")
    })
    @GetMapping("/oficial/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<SesionResponse>>> listarPorOficial(@PathVariable String rut) {
        List<EntityModel<SesionResponse>> items = sesionService.listarPorOficial(rut).stream()
                .map(s -> EntityModel.of(s,
                        linkTo(methodOn(SesionController.class).buscarPorId(s.getId())).withSelfRel(),
                        linkTo(methodOn(SesionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(SesionController.class).listarPorOficial(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nueva sesion")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Sesion registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<SesionResponse>> crear(@Valid @RequestBody SesionRequest request) {
        SesionResponse response = sesionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(SesionController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(SesionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Cerrar sesion")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Sesion cerrada correctamente"),
        @ApiResponse(responseCode = "404", description = "Sesion no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        sesionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
