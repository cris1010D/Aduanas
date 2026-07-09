package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.MovimientoRequest;
import cl.triskeledu.aduanas.proceso.dto.MovimientoResponse;
import cl.triskeledu.aduanas.proceso.service.MovimientoService;
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

@Tag(name = "Movimientos Fronterizos", description = "Registro de movimientos de entrada y salida en frontera")
@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(summary = "Listar todos los movimientos fronterizos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<MovimientoResponse>>> listarTodos() {
        List<EntityModel<MovimientoResponse>> items = movimientoService.listarTodos().stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MovimientoController.class).buscarPorId(m.getId())).withSelfRel(),
                        linkTo(methodOn(MovimientoController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(MovimientoController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar movimiento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
        @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<MovimientoResponse>> buscarPorId(@PathVariable Integer id) {
        MovimientoResponse response = movimientoService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MovimientoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(MovimientoController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(MovimientoController.class).listarPorViajero(response.getRutViajero())).withRel("por-viajero")));
    }

    @Operation(summary = "Listar movimientos de un viajero")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de movimientos del viajero")
    })
    @GetMapping("/viajero/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<MovimientoResponse>>> listarPorViajero(@PathVariable String rut) {
        List<EntityModel<MovimientoResponse>> items = movimientoService.listarPorViajero(rut).stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MovimientoController.class).buscarPorId(m.getId())).withSelfRel(),
                        linkTo(methodOn(MovimientoController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(MovimientoController.class).listarPorViajero(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nuevo movimiento fronterizo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<MovimientoResponse>> crear(@Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse response = movimientoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(MovimientoController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(MovimientoController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar movimiento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        movimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
