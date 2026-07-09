package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.TramiteRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroResponse;
import cl.triskeledu.aduanas.proceso.service.ViajeroService;
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

@Tag(name = "Viajeros", description = "Registro y control de viajeros en paso fronterizo")
@RestController
@RequestMapping("/api/v1/viajeros")
@RequiredArgsConstructor
public class ViajeroController {

    private final ViajeroService viajeroService;

    @Operation(summary = "Listar todos los viajeros registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ViajeroResponse>>> listarTodos() {
        List<EntityModel<ViajeroResponse>> items = viajeroService.listarTodos().stream()
                .map(v -> EntityModel.of(v,
                        linkTo(methodOn(ViajeroController.class).buscarPorId(v.getId())).withSelfRel(),
                        linkTo(methodOn(ViajeroController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ViajeroController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar viajero por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Viajero encontrado"),
        @ApiResponse(responseCode = "404", description = "Viajero no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ViajeroResponse>> buscarPorId(@PathVariable Integer id) {
        ViajeroResponse response = viajeroService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ViajeroController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ViajeroController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(ViajeroController.class).buscarPorRut(response.getRut())).withRel("por-rut")));
    }

    @Operation(summary = "Buscar viajero por RUT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Viajero encontrado"),
        @ApiResponse(responseCode = "404", description = "Viajero no encontrado")
    })
    @GetMapping("/rut/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ViajeroResponse>> buscarPorRut(@PathVariable String rut) {
        ViajeroResponse response = viajeroService.buscarPorRut(rut);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ViajeroController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ViajeroController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Registrar nuevo viajero en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Viajero registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un viajero con ese RUT")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ViajeroResponse>> crear(@Valid @RequestBody ViajeroRequest request) {
        ViajeroResponse response = viajeroService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(ViajeroController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ViajeroController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar datos de viajero")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Viajero actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Viajero no encontrado")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ViajeroResponse>> actualizar(@PathVariable Integer id,
                                                                    @Valid @RequestBody ViajeroRequest request) {
        ViajeroResponse response = viajeroService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ViajeroController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ViajeroController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar viajero")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Viajero eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Viajero no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        viajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Procesar salida diplomatica de viajero")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Salida diplomatica procesada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping("/salida-diplomatica")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ViajeroResponse>> procesarSalidaDiplomatica(
            @Valid @RequestBody TramiteRequest request) {
        ViajeroResponse response = viajeroService.procesarSalidaDiplomatica(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(ViajeroController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ViajeroController.class).listarTodos()).withRel("collection")));
    }
}
