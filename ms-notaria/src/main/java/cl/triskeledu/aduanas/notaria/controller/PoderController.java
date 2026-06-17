package cl.triskeledu.aduanas.notaria.controller;

import cl.triskeledu.aduanas.notaria.dto.PoderRequest;
import cl.triskeledu.aduanas.notaria.dto.PoderResponse;
import cl.triskeledu.aduanas.notaria.service.PoderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/poderes")
@RequiredArgsConstructor
public class PoderController {

    private final PoderService poderService;

    @GetMapping
    public ResponseEntity<List<PoderResponse>> listarTodos() {
        return ResponseEntity.ok(poderService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoderResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(poderService.buscarPorId(id));
    }

    @GetMapping("/titular/{rut}")
    public ResponseEntity<List<PoderResponse>> listarPorTitular(@PathVariable String rut) {
        return ResponseEntity.ok(poderService.listarPorTitular(rut));
    }

    @PostMapping
    public ResponseEntity<PoderResponse> crear(@Valid @RequestBody PoderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poderService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoderResponse> actualizar(@PathVariable Integer id,
                                                     @Valid @RequestBody PoderRequest request) {
        return ResponseEntity.ok(poderService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        poderService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
