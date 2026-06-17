package cl.triskeledu.aduanas.datos.controller;

import cl.triskeledu.aduanas.datos.dto.CacheEntryRequest;
import cl.triskeledu.aduanas.datos.dto.CacheEntryResponse;
import cl.triskeledu.aduanas.datos.service.CacheEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheEntryController {

    private final CacheEntryService cacheEntryService;

    @GetMapping
    public ResponseEntity<List<CacheEntryResponse>> listarTodas() {
        return ResponseEntity.ok(cacheEntryService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CacheEntryResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cacheEntryService.buscarPorId(id));
    }

    @GetMapping("/clave/{clave}")
    public ResponseEntity<CacheEntryResponse> buscarPorClave(@PathVariable String clave) {
        return ResponseEntity.ok(cacheEntryService.buscarPorClave(clave));
    }

    @PostMapping
    public ResponseEntity<CacheEntryResponse> crear(@Valid @RequestBody CacheEntryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cacheEntryService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CacheEntryResponse> actualizar(@PathVariable Integer id,
                                                          @Valid @RequestBody CacheEntryRequest request) {
        return ResponseEntity.ok(cacheEntryService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cacheEntryService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
