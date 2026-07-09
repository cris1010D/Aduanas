package cl.triskeledu.aduanas.pdi.controller;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.service.ConsultaService;
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

@Tag(name = "Consultas PDI", description = "Historial de consultas realizadas al sistema PDI")
@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @Operation(summary = "Listar todas las consultas PDI")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ConsultaResponse>>> listarTodas() {
        List<EntityModel<ConsultaResponse>> items = consultaService.listarTodas().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ConsultaController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ConsultaController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ConsultaController.class).listarTodas()).withSelfRel()));
    }

    @Operation(summary = "Buscar consulta PDI por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ConsultaResponse>> buscarPorId(@PathVariable Integer id) {
        ConsultaResponse response = consultaService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ConsultaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ConsultaController.class).listarTodas()).withRel("collection"),
                linkTo(methodOn(ConsultaController.class).listarPorRut(response.getRut())).withRel("por-rut")));
    }

    @Operation(summary = "Listar consultas PDI de un RUT especifico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de consultas del RUT")
    })
    @GetMapping("/rut/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ConsultaResponse>>> listarPorRut(@PathVariable String rut) {
        List<EntityModel<ConsultaResponse>> items = consultaService.listarPorRut(rut).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ConsultaController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ConsultaController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ConsultaController.class).listarPorRut(rut)).withSelfRel()));
    }

    @Operation(summary = "Registrar nueva consulta PDI")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Consulta registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ConsultaResponse>> crear(@Valid @RequestBody ConsultaRequest request) {
        ConsultaResponse response = consultaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(ConsultaController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ConsultaController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Eliminar consulta PDI")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Consulta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        consultaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
