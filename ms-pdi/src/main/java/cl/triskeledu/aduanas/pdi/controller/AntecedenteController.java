package cl.triskeledu.aduanas.pdi.controller;

import cl.triskeledu.aduanas.pdi.dto.AntecedenteRequest;
import cl.triskeledu.aduanas.pdi.dto.AntecedenteResponse;
import cl.triskeledu.aduanas.pdi.service.AntecedenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/antecedentes")
@RequiredArgsConstructor
public class AntecedenteController {

    private final AntecedenteService antecedenteService;

    @GetMapping
    public ResponseEntity<List<AntecedenteResponse>> listarTodos() {
        return ResponseEntity.ok(antecedenteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntecedenteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(antecedenteService.buscarPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<AntecedenteResponse> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(antecedenteService.buscarPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<AntecedenteResponse> crear(@Valid @RequestBody AntecedenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(antecedenteService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AntecedenteResponse> actualizar(@PathVariable Integer id,
                                                           @Valid @RequestBody AntecedenteRequest request) {
        return ResponseEntity.ok(antecedenteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        antecedenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
