package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.VehiculoRequest;
import cl.triskeledu.aduanas.proceso.dto.VehiculoResponse;
import cl.triskeledu.aduanas.proceso.service.VehiculoService;
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

@Tag(name = "Vehiculos", description = "Control de ingreso y salida de vehiculos en frontera")
@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @Operation(summary = "Listar todos los vehiculos registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<VehiculoResponse>>> listarTodos() {
        List<EntityModel<VehiculoResponse>> items = vehiculoService.listarTodos().stream()
                .map(v -> EntityModel.of(v,
                        linkTo(methodOn(VehiculoController.class).buscarPorId(v.getId())).withSelfRel(),
                        linkTo(methodOn(VehiculoController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(VehiculoController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar vehiculo por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehiculo encontrado"),
        @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<VehiculoResponse>> buscarPorId(@PathVariable Integer id) {
        VehiculoResponse response = vehiculoService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(VehiculoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(VehiculoController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(VehiculoController.class).buscarPorPlaca(response.getPlaca())).withRel("por-placa")));
    }

    @Operation(summary = "Buscar vehiculo por placa patente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehiculo encontrado"),
        @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })
    @GetMapping("/placa/{placa}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<VehiculoResponse>> buscarPorPlaca(@PathVariable String placa) {
        VehiculoResponse response = vehiculoService.buscarPorPlaca(placa);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(VehiculoController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(VehiculoController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Admitir vehiculo en frontera (R.3 - calcula ingreso y vencimiento a 180 dias)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vehiculo admitido correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "409", description = "El vehiculo ya se encuentra en el pais")
    })
    @PostMapping("/admitir")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<VehiculoResponse>> admitirVehiculo(
            @Valid @RequestBody VehiculoRequest request) {
        VehiculoResponse response = vehiculoService.admitirVehiculo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(VehiculoController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(VehiculoController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Actualizar datos de vehiculo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehiculo actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<VehiculoResponse>> actualizar(@PathVariable Integer id,
                                                                     @Valid @RequestBody VehiculoRequest request) {
        VehiculoResponse response = vehiculoService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(VehiculoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(VehiculoController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar vehiculo del registro")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vehiculo eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
<<<<<<< HEAD

    @Operation(summary = "Listar vehiculos de un propietario/transportista por RUT")
    @GetMapping("/rut/{rut}")
    public ResponseEntity<CollectionModel<EntityModel<VehiculoResponse>>> listarPorRut(@PathVariable String rut) {
        List<EntityModel<VehiculoResponse>> items = vehiculoService.listarPorRutPropietario(rut).stream()
                .map(v -> EntityModel.of(v,
                        linkTo(methodOn(VehiculoController.class).buscarPorId(v.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(VehiculoController.class).listarPorRut(rut)).withSelfRel()));
    }
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
}
