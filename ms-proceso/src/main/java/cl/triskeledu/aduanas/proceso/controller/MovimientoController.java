package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.MovimientoRequest;
import cl.triskeledu.aduanas.proceso.dto.MovimientoResponse;
import cl.triskeledu.aduanas.proceso.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listarTodos() {
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(movimientoService.buscarPorId(id));
    }

    @GetMapping("/viajero/{rut}")
    public ResponseEntity<List<MovimientoResponse>> listarPorViajero(@PathVariable String rut) {
        return ResponseEntity.ok(movimientoService.listarPorViajero(rut));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(@Valid @RequestBody MovimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        movimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
