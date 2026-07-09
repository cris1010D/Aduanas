package cl.triskeledu.aduanas.datos.controller;

import cl.triskeledu.aduanas.datos.dto.ConfiguracionRequest;
import cl.triskeledu.aduanas.datos.dto.ConfiguracionResponse;
import cl.triskeledu.aduanas.datos.service.ConfiguracionService;
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

@Tag(name = "Configuraciones", description = "Configuracion centralizada del ecosistema de microservicios")
@RestController
@RequestMapping("/api/v1/configuraciones")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @Operation(summary = "Listar todas las configuraciones")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ConfiguracionResponse>>> listarTodas() {
        List<EntityModel<ConfiguracionResponse>> items = configuracionService.listarTodas().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ConfiguracionController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ConfiguracionController.class).listarTodas()).withSelfRel()));
    }

    @Operation(summary = "Listar configuraciones activas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de configuraciones activas")
    })
    @GetMapping("/activas")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ConfiguracionResponse>>> listarActivas() {
        List<EntityModel<ConfiguracionResponse>> items = configuracionService.listarActivas().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ConfiguracionController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ConfiguracionController.class).listarActivas()).withSelfRel()));
    }

    @Operation(summary = "Buscar configuracion por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuracion encontrada"),
        @ApiResponse(responseCode = "404", description = "Configuracion no encontrada")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ConfiguracionResponse>> buscarPorId(@PathVariable Integer id) {
        ConfiguracionResponse response = configuracionService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ConfiguracionController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection"),
                linkTo(methodOn(ConfiguracionController.class).buscarPorClave(response.getClave())).withRel("por-clave")));
    }

    @Operation(summary = "Buscar configuracion por clave")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuracion encontrada"),
        @ApiResponse(responseCode = "404", description = "Clave no encontrada")
    })
    @GetMapping("/clave/{clave}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ConfiguracionResponse>> buscarPorClave(@PathVariable String clave) {
        ConfiguracionResponse response = configuracionService.buscarPorClave(clave);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ConfiguracionController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Listar configuraciones de un microservicio")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de configuraciones del microservicio")
    })
    @GetMapping("/ms/{msDuenio}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<ConfiguracionResponse>>> listarPorMs(@PathVariable String msDuenio) {
        List<EntityModel<ConfiguracionResponse>> items = configuracionService.listarPorMs(msDuenio).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ConfiguracionController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(ConfiguracionController.class).listarPorMs(msDuenio)).withSelfRel()));
    }

    @Operation(summary = "Crear nueva configuracion")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Configuracion creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe una configuracion con esa clave")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ConfiguracionResponse>> crear(@Valid @RequestBody ConfiguracionRequest request) {
        ConfiguracionResponse response = configuracionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(ConfiguracionController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Actualizar configuracion existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuracion actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Configuracion no encontrada")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<ConfiguracionResponse>> actualizar(@PathVariable Integer id,
                                                                          @Valid @RequestBody ConfiguracionRequest request) {
        ConfiguracionResponse response = configuracionService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ConfiguracionController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ConfiguracionController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Eliminar configuracion")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Configuracion eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Configuracion no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        configuracionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
