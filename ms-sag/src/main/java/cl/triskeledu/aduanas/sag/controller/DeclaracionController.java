package cl.triskeledu.aduanas.sag.controller;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.service.DeclaracionService;
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

@Tag(name = "Declaraciones SAG", description = "Declaraciones fitosanitarias y agricolas para control fronterizo")
@RestController
@RequestMapping("/api/v1/declaraciones")
@RequiredArgsConstructor
public class DeclaracionController {

    private final DeclaracionService declaracionService;

    @Operation(summary = "Listar todas las declaraciones SAG")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<DeclaracionResponse>>> listarTodas() {
        List<EntityModel<DeclaracionResponse>> items = declaracionService.listarTodas().stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DeclaracionController.class).buscarPorId(d.getId())).withSelfRel(),
                        linkTo(methodOn(DeclaracionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(DeclaracionController.class).listarTodas()).withSelfRel()));
    }

    @Operation(summary = "Buscar declaracion SAG por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Declaracion encontrada"),
        @ApiResponse(responseCode = "404", description = "Declaracion no encontrada")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<DeclaracionResponse>> buscarPorId(@PathVariable Integer id) {
        DeclaracionResponse response = declaracionService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(DeclaracionController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(DeclaracionController.class).listarTodas()).withRel("collection"),
                linkTo(methodOn(DeclaracionController.class).listarPorViajero(response.getRutViajero())).withRel("por-viajero")));
    }

    @Operation(summary = "Listar declaraciones SAG de un viajero")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de declaraciones del viajero")
    })
    @GetMapping("/viajero/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<DeclaracionResponse>>> listarPorViajero(@PathVariable String rut) {
        List<EntityModel<DeclaracionResponse>> items = declaracionService.listarPorViajero(rut).stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DeclaracionController.class).buscarPorId(d.getId())).withSelfRel(),
                        linkTo(methodOn(DeclaracionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(DeclaracionController.class).listarPorViajero(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nueva declaracion SAG")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Declaracion registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<DeclaracionResponse>> crear(@Valid @RequestBody DeclaracionRequest request) {
        DeclaracionResponse response = declaracionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(DeclaracionController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(DeclaracionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Actualizar declaracion SAG")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Declaracion actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Declaracion no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<DeclaracionResponse>> actualizar(@PathVariable Integer id,
                                                                        @Valid @RequestBody DeclaracionRequest request) {
        DeclaracionResponse response = declaracionService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(DeclaracionController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(DeclaracionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Eliminar declaracion SAG")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Declaracion eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Declaracion no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        declaracionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
