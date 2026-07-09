package cl.triskeledu.aduanas.menores.controller;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.service.AutorizacionService;
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

@Tag(name = "Autorizaciones Menores", description = "Autorizaciones de salida al exterior para menores de edad")
@RestController
@RequestMapping("/api/v1/autorizaciones")
@RequiredArgsConstructor
public class AutorizacionController {

    private final AutorizacionService autorizacionService;

    @Operation(summary = "Listar todas las autorizaciones de menores")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<AutorizacionResponse>>> listarTodas() {
        List<EntityModel<AutorizacionResponse>> items = autorizacionService.listarTodas().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AutorizacionController.class).buscarPorId(a.getId())).withSelfRel(),
                        linkTo(methodOn(AutorizacionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(AutorizacionController.class).listarTodas()).withSelfRel()));
    }

    @Operation(summary = "Buscar autorizacion por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autorizacion encontrada"),
        @ApiResponse(responseCode = "404", description = "Autorizacion no encontrada")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<AutorizacionResponse>> buscarPorId(@PathVariable Integer id) {
        AutorizacionResponse response = autorizacionService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(AutorizacionController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(AutorizacionController.class).listarTodas()).withRel("collection"),
                linkTo(methodOn(AutorizacionController.class).listarPorMenor(response.getRutMenor())).withRel("por-menor")));
    }

    @Operation(summary = "Listar autorizaciones de un menor especifico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de autorizaciones del menor")
    })
    @GetMapping("/menor/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<AutorizacionResponse>>> listarPorMenor(@PathVariable String rut) {
        List<EntityModel<AutorizacionResponse>> items = autorizacionService.listarPorMenor(rut).stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AutorizacionController.class).buscarPorId(a.getId())).withSelfRel(),
                        linkTo(methodOn(AutorizacionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(AutorizacionController.class).listarPorMenor(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nueva autorizacion de salida para menor")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Autorizacion registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<AutorizacionResponse>> crear(@Valid @RequestBody AutorizacionRequest request) {
        AutorizacionResponse response = autorizacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(AutorizacionController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(AutorizacionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Eliminar autorizacion")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Autorizacion eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Autorizacion no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        autorizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
