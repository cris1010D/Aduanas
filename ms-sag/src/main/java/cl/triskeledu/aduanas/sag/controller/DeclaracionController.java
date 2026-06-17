package cl.triskeledu.aduanas.sag.controller;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.service.DeclaracionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/declaraciones")
@RequiredArgsConstructor
public class DeclaracionController {

    private final DeclaracionService declaracionService;

    @GetMapping
    public ResponseEntity<List<DeclaracionResponse>> listarTodas() {
        return ResponseEntity.ok(declaracionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeclaracionResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(declaracionService.buscarPorId(id));
    }

    @GetMapping("/viajero/{rut}")
    public ResponseEntity<List<DeclaracionResponse>> listarPorViajero(@PathVariable String rut) {
        return ResponseEntity.ok(declaracionService.listarPorViajero(rut));
    }

    @PostMapping
    public ResponseEntity<DeclaracionResponse> crear(@Valid @RequestBody DeclaracionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(declaracionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeclaracionResponse> actualizar(@PathVariable Integer id,
                                                           @Valid @RequestBody DeclaracionRequest request) {
        return ResponseEntity.ok(declaracionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        declaracionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
