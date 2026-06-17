package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.ViajeroRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroResponse;
import cl.triskeledu.aduanas.proceso.service.ViajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/viajeros")
@RequiredArgsConstructor
public class ViajeroController {

    private final ViajeroService viajeroService;

    @GetMapping
    public ResponseEntity<List<ViajeroResponse>> listarTodos() {
        return ResponseEntity.ok(viajeroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeroResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(viajeroService.buscarPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ViajeroResponse> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(viajeroService.buscarPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<ViajeroResponse> crear(@Valid @RequestBody ViajeroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(viajeroService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajeroResponse> actualizar(@PathVariable Integer id,
                                                       @Valid @RequestBody ViajeroRequest request) {
        return ResponseEntity.ok(viajeroService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        viajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
