package cl.triskeledu.aduanas.datos.controller;

import cl.triskeledu.aduanas.datos.dto.CacheEntryRequest;
import cl.triskeledu.aduanas.datos.dto.CacheEntryResponse;
import cl.triskeledu.aduanas.datos.service.CacheEntryService;
import cl.triskeledu.aduanas.datos.service.RedisCacheService;
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
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Cache Redis", description = "Gestion de entradas de cache distribuida con Redis")
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheEntryController {

    private final CacheEntryService cacheEntryService;
    private final RedisCacheService redisCacheService;

    @Operation(summary = "Listar todas las entradas de cache")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<CacheEntryResponse>>> listarTodas() {
        List<EntityModel<CacheEntryResponse>> items = cacheEntryService.listarTodas().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CacheEntryController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(CacheEntryController.class).listarTodas()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(CacheEntryController.class).listarTodas()).withSelfRel()));
    }

    @Operation(summary = "Buscar entrada de cache por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Entrada encontrada"),
        @ApiResponse(responseCode = "404", description = "Entrada no encontrada")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<CacheEntryResponse>> buscarPorId(@PathVariable Integer id) {
        CacheEntryResponse response = cacheEntryService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(CacheEntryController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(CacheEntryController.class).listarTodas()).withRel("collection"),
                linkTo(methodOn(CacheEntryController.class).buscarPorClave(response.getClave())).withRel("por-clave")));
    }

    @Operation(summary = "Buscar entrada de cache por clave")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Entrada encontrada"),
        @ApiResponse(responseCode = "404", description = "Clave no encontrada en cache")
    })
    @GetMapping("/clave/{clave}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<CacheEntryResponse>> buscarPorClave(@PathVariable String clave) {
        CacheEntryResponse response = cacheEntryService.buscarPorClave(clave);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(CacheEntryController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(CacheEntryController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Crear nueva entrada de cache")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Entrada creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<CacheEntryResponse>> crear(@Valid @RequestBody CacheEntryRequest request) {
        CacheEntryResponse response = cacheEntryService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(CacheEntryController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(CacheEntryController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Actualizar entrada de cache existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Entrada actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Entrada no encontrada")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<CacheEntryResponse>> actualizar(@PathVariable Integer id,
                                                                       @Valid @RequestBody CacheEntryRequest request) {
        CacheEntryResponse response = cacheEntryService.actualizar(id, request);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(CacheEntryController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(CacheEntryController.class).listarTodas()).withRel("collection")));
    }

    @Operation(summary = "Eliminar entrada de cache")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Entrada eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Entrada no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cacheEntryService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar valor directamente en Redis con TTL")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valor encontrado en Redis"),
        @ApiResponse(responseCode = "404", description = "Clave no existe o ya expiro en Redis")
    })
    @GetMapping("/redis/{clave}")
    public ResponseEntity<Map<String, Object>> obtenerDesdeRedis(@PathVariable String clave) {
        String valor = redisCacheService.obtener(clave);
        if (valor == null) {
            return ResponseEntity.notFound().build();
        }
        Long ttl = redisCacheService.ttlRestante(clave);
        return ResponseEntity.ok(Map.of(
            "clave",       clave,
            "valor",       valor,
            "ttlRestante", ttl != null ? ttl : -1L,
            "fuente",      "REDIS"
        ));
    }

    @Operation(summary = "Eliminar clave directamente de Redis")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Clave eliminada de Redis")
    })
    @DeleteMapping("/redis/{clave}")
    public ResponseEntity<Void> eliminarDeRedis(@PathVariable String clave) {
        redisCacheService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
