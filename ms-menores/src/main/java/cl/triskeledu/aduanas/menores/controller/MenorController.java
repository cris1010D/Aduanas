package cl.triskeledu.aduanas.menores.controller;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.service.MenorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menores")
@RequiredArgsConstructor
public class MenorController {

    private final MenorService menorService;

    @GetMapping
    public ResponseEntity<List<MenorResponse>> listarTodos() {
        return ResponseEntity.ok(menorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenorResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(menorService.buscarPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<MenorResponse> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(menorService.buscarPorRut(rut));
    }

    @GetMapping("/tutor/{rut}")
    public ResponseEntity<List<MenorResponse>> listarPorTutor(@PathVariable String rut) {
        return ResponseEntity.ok(menorService.listarPorTutor(rut));
    }

    @PostMapping
    public ResponseEntity<MenorResponse> crear(@Valid @RequestBody MenorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menorService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenorResponse> actualizar(@PathVariable Integer id,
                                                     @Valid @RequestBody MenorRequest request) {
        return ResponseEntity.ok(menorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        menorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
