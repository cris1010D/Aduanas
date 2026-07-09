package cl.triskeledu.aduanas.notaria.controller;

import cl.triskeledu.aduanas.notaria.dto.PoderRequest;
import cl.triskeledu.aduanas.notaria.dto.PoderResponse;
import cl.triskeledu.aduanas.notaria.service.PoderService;
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

@Tag(name = "Poderes Notariales", description = "Gestion de poderes notariales para autorizacion de menores")
@RestController
@RequestMapping("/api/v1/poderes")
@RequiredArgsConstructor
public class PoderController {

    private final PoderService poderService;

    @Operation(summary = "Listar todos los poderes notariales")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<PoderResponse>>> listarTodos() {
        List<EntityModel<PoderResponse>> items = poderService.listarTodos().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PoderController.class).buscarPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(PoderController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(PoderController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar poder notarial por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Poder encontrado"),
        @ApiResponse(responseCode = "404", description = "Poder no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<PoderResponse>> buscarPorId(@PathVariable Integer id) {
        PoderResponse response = poderService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(PoderController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PoderController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(PoderController.class).listarPorTitular(response.getRutTitular())).withRel("por-titular")));
    }

    @Operation(summary = "Listar poderes notariales de un titular")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de poderes del titular")
    })
    @GetMapping("/titular/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<PoderResponse>>> listarPorTitular(@PathVariable String rut) {
        List<EntityModel<PoderResponse>> items = poderService.listarPorTitular(rut).stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PoderController.class).buscarPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(PoderController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(PoderController.class).listarPorTitular(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nuevo poder notarial")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Poder registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<PoderResponse>> crear(@Valid @RequestBody PoderRequest request) {
        PoderResponse response = poderService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(PoderController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(PoderController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar poder notarial existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Poder actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Poder no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<PoderResponse>> actualizar(@PathVariable Integer id,
                                                                  @Valid @RequestBody PoderRequest request) {
        PoderResponse response = poderService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(PoderController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PoderController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar poder notarial")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Poder eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Poder no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        poderService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
