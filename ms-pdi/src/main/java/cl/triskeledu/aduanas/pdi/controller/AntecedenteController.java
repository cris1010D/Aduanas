package cl.triskeledu.aduanas.pdi.controller;

import cl.triskeledu.aduanas.pdi.dto.AntecedenteRequest;
import cl.triskeledu.aduanas.pdi.dto.AntecedenteResponse;
import cl.triskeledu.aduanas.pdi.service.AntecedenteService;
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

@Tag(name = "Antecedentes PDI", description = "Registro de antecedentes policiales consultados")
@RestController
@RequestMapping("/api/v1/antecedentes")
@RequiredArgsConstructor
public class AntecedenteController {

    private final AntecedenteService antecedenteService;

    @Operation(summary = "Listar todos los antecedentes registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<AntecedenteResponse>>> listarTodos() {
        List<EntityModel<AntecedenteResponse>> items = antecedenteService.listarTodos().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AntecedenteController.class).buscarPorId(a.getId())).withSelfRel(),
                        linkTo(methodOn(AntecedenteController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(AntecedenteController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar antecedente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Antecedente encontrado"),
        @ApiResponse(responseCode = "404", description = "Antecedente no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<AntecedenteResponse>> buscarPorId(@PathVariable Integer id) {
        AntecedenteResponse response = antecedenteService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(AntecedenteController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(AntecedenteController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(AntecedenteController.class).buscarPorRut(response.getRut())).withRel("por-rut")));
    }

    @Operation(summary = "Buscar antecedente por RUT del viajero")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Antecedente encontrado"),
        @ApiResponse(responseCode = "404", description = "Antecedente no encontrado para ese RUT")
    })
    @GetMapping("/rut/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<AntecedenteResponse>> buscarPorRut(@PathVariable String rut) {
        AntecedenteResponse response = antecedenteService.buscarPorRut(rut);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(AntecedenteController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(AntecedenteController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Registrar nuevo antecedente PDI")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Antecedente registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<AntecedenteResponse>> crear(@Valid @RequestBody AntecedenteRequest request) {
        AntecedenteResponse response = antecedenteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(AntecedenteController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(AntecedenteController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar antecedente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Antecedente actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Antecedente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<AntecedenteResponse>> actualizar(@PathVariable Integer id,
                                                                        @Valid @RequestBody AntecedenteRequest request) {
        AntecedenteResponse response = antecedenteService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(AntecedenteController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(AntecedenteController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar antecedente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Antecedente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Antecedente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        antecedenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
