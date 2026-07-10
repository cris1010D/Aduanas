package cl.triskeledu.aduanas.auth.controller;

import cl.triskeledu.aduanas.auth.dto.OficialCreateRequest;
import cl.triskeledu.aduanas.auth.dto.OficialResponse;
import cl.triskeledu.aduanas.auth.dto.OficialUpdateRequest;
import cl.triskeledu.aduanas.auth.service.OficialService;
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

@Tag(name = "Oficiales", description = "Gestion de oficiales de aduana (CRUD y estado)")
@RestController
@RequestMapping("/api/v1/oficiales")
@RequiredArgsConstructor
public class OficialController {

    private final OficialService oficialService;

    // NOTA: se eliminó el endpoint duplicado "POST /api/v1/oficiales/auth/login"
    // que existía aquí. Era inservible: caía bajo la regla de seguridad
    // "POST /api/v1/oficiales/** -> hasRole(SUPERVISOR)", es decir, exigía estar
    // logueado como SUPERVISOR para poder... loguearse. El login real y funcional
    // sigue siendo POST /api/v1/auth/login (AuthController), que es permitAll.

    @Operation(summary = "Listar todos los oficiales")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<OficialResponse>>> listarTodos() {
        List<EntityModel<OficialResponse>> items = oficialService.listarTodos().stream()
                .map(o -> EntityModel.of(o,
                        linkTo(methodOn(OficialController.class).buscarPorId(o.getId())).withSelfRel(),
                        linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(OficialController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Listar oficiales activos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de oficiales activos")
    })
    @GetMapping("/activos")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<OficialResponse>>> listarActivos() {
        List<EntityModel<OficialResponse>> items = oficialService.listarActivos().stream()
                .map(o -> EntityModel.of(o,
                        linkTo(methodOn(OficialController.class).buscarPorId(o.getId())).withSelfRel(),
                        linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(OficialController.class).listarActivos()).withSelfRel()));
    }

    @Operation(summary = "Listar usuarios por rol (ej: TRANSPORTISTA, VIAJERO, OFICIAL)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios con el rol indicado")
    })
    @GetMapping("/rol/{rol}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<OficialResponse>>> listarPorRol(@PathVariable String rol) {
        List<EntityModel<OficialResponse>> items = oficialService.listarPorRol(rol.toUpperCase()).stream()
                .map(o -> EntityModel.of(o,
                        linkTo(methodOn(OficialController.class).buscarPorId(o.getId())).withSelfRel(),
                        linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(OficialController.class).listarPorRol(rol)).withSelfRel()));
    }

    @Operation(summary = "Buscar oficial por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oficial encontrado"),
            @ApiResponse(responseCode = "404", description = "Oficial no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<OficialResponse>> buscarPorId(@PathVariable Integer id) {
        OficialResponse response = oficialService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(OficialController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(OficialController.class).buscarPorRut(response.getRut())).withRel("por-rut")));
    }

    @Operation(summary = "Buscar oficial por RUT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oficial encontrado"),
            @ApiResponse(responseCode = "404", description = "Oficial no encontrado")
    })
    @GetMapping("/rut/{rut}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<OficialResponse>> buscarPorRut(@PathVariable String rut) {
        OficialResponse response = oficialService.buscarPorRut(rut);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(OficialController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Registrar nuevo oficial")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Oficial registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un oficial con ese RUT")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<OficialResponse>> crear(@Valid @RequestBody OficialCreateRequest request) {
        OficialResponse response = oficialService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(OficialController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar datos de oficial")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oficial actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Oficial no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<OficialResponse>> actualizar(@PathVariable Integer id,
                                                                   @Valid @RequestBody OficialUpdateRequest request) {
        OficialResponse response = oficialService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(OficialController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(OficialController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar oficial")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Oficial eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Oficial no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        oficialService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}