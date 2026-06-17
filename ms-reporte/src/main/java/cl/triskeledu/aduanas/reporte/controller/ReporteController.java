package cl.triskeledu.aduanas.reporte.controller;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteResponse>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @GetMapping("/oficial/{rut}")
    public ResponseEntity<List<ReporteResponse>> listarPorOficial(@PathVariable String rut) {
        return ResponseEntity.ok(reporteService.listarPorOficial(rut));
    }

    @PostMapping
    public ResponseEntity<ReporteResponse> crear(@Valid @RequestBody ReporteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponse> actualizar(@PathVariable Integer id,
                                                       @Valid @RequestBody ReporteRequest request) {
        return ResponseEntity.ok(reporteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
