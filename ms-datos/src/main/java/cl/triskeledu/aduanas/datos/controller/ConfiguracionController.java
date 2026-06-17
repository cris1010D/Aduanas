package cl.triskeledu.aduanas.datos.controller;

import cl.triskeledu.aduanas.datos.dto.ConfiguracionRequest;
import cl.triskeledu.aduanas.datos.dto.ConfiguracionResponse;
import cl.triskeledu.aduanas.datos.service.ConfiguracionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configuraciones")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @GetMapping
    public ResponseEntity<List<ConfiguracionResponse>> listarTodas() {
        return ResponseEntity.ok(configuracionService.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<ConfiguracionResponse>> listarActivas() {
        return ResponseEntity.ok(configuracionService.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(configuracionService.buscarPorId(id));
    }

    @GetMapping("/clave/{clave}")
    public ResponseEntity<ConfiguracionResponse> buscarPorClave(@PathVariable String clave) {
        return ResponseEntity.ok(configuracionService.buscarPorClave(clave));
    }

    @GetMapping("/ms/{msDuenio}")
    public ResponseEntity<List<ConfiguracionResponse>> listarPorMs(@PathVariable String msDuenio) {
        return ResponseEntity.ok(configuracionService.listarPorMs(msDuenio));
    }

    @PostMapping
    public ResponseEntity<ConfiguracionResponse> crear(@Valid @RequestBody ConfiguracionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(configuracionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionResponse> actualizar(@PathVariable Integer id,
                                                             @Valid @RequestBody ConfiguracionRequest request) {
        return ResponseEntity.ok(configuracionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        configuracionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
