package cl.triskeledu.aduanas.auth.controller;

import cl.triskeledu.aduanas.auth.dto.SesionRequest;
import cl.triskeledu.aduanas.auth.dto.SesionResponse;
import cl.triskeledu.aduanas.auth.service.SesionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sesiones")
@RequiredArgsConstructor
public class SesionController {

    private final SesionService sesionService;

    @GetMapping
    public ResponseEntity<List<SesionResponse>> listarTodas() {
        return ResponseEntity.ok(sesionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(sesionService.buscarPorId(id));
    }

    @GetMapping("/oficial/{rut}")
    public ResponseEntity<List<SesionResponse>> listarPorOficial(@PathVariable String rut) {
        return ResponseEntity.ok(sesionService.listarPorOficial(rut));
    }

    @PostMapping
    public ResponseEntity<SesionResponse> crear(@Valid @RequestBody SesionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        sesionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
