package cl.triskeledu.aduanas.menores.controller;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.service.MenorService;
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

@Tag(name = "Menores", description = "Registro y consulta de menores de edad en el sistema")
@RestController
@RequestMapping("/api/v1/menores")
@RequiredArgsConstructor
public class MenorController {

    private final MenorService menorService;

    @Operation(summary = "Listar todos los menores registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<MenorResponse>>> listarTodos() {
        List<EntityModel<MenorResponse>> items = menorService.listarTodos().stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MenorController.class).buscarPorId(m.getId())).withSelfRel(),
                        linkTo(methodOn(MenorController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(MenorController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar menor por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menor encontrado"),
        @ApiResponse(responseCode = "404", description = "Menor no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<MenorResponse>> buscarPorId(@PathVariable Integer id) {
        MenorResponse response = menorService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MenorController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(MenorController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(MenorController.class).listarPorTutor(response.getRutTutor())).withRel("por-tutor")));
    }

    @Operation(summary = "Buscar menor por RUT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menor encontrado"),
        @ApiResponse(responseCode = "404", description = "Menor no encontrado")
    })
    @GetMapping("/rut/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<MenorResponse>> buscarPorRut(@PathVariable String rut) {
        MenorResponse response = menorService.buscarPorRut(rut);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MenorController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(MenorController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Listar menores a cargo de un tutor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de menores del tutor")
    })
    @GetMapping("/tutor/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<MenorResponse>>> listarPorTutor(@PathVariable String rut) {
        List<EntityModel<MenorResponse>> items = menorService.listarPorTutor(rut).stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MenorController.class).buscarPorId(m.getId())).withSelfRel(),
                        linkTo(methodOn(MenorController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(MenorController.class).listarPorTutor(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nuevo menor")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Menor registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un menor con ese RUT")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<MenorResponse>> crear(@Valid @RequestBody MenorRequest request) {
        MenorResponse response = menorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(MenorController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(MenorController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar datos de menor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menor actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Menor no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<MenorResponse>> actualizar(@PathVariable Integer id,
                                                                  @Valid @RequestBody MenorRequest request) {
        MenorResponse response = menorService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(MenorController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(MenorController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar menor")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Menor eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Menor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        menorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
