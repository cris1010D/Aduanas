package cl.triskeledu.aduanas.menores.controller;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.service.AutorizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/autorizaciones")
@RequiredArgsConstructor
public class AutorizacionController {

    private final AutorizacionService autorizacionService;

    @GetMapping
    public ResponseEntity<List<AutorizacionResponse>> listarTodas() {
        return ResponseEntity.ok(autorizacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorizacionResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(autorizacionService.buscarPorId(id));
    }

    @GetMapping("/menor/{rut}")
    public ResponseEntity<List<AutorizacionResponse>> listarPorMenor(@PathVariable String rut) {
        return ResponseEntity.ok(autorizacionService.listarPorMenor(rut));
    }

    @PostMapping
    public ResponseEntity<AutorizacionResponse> crear(@Valid @RequestBody AutorizacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorizacionService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        autorizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
