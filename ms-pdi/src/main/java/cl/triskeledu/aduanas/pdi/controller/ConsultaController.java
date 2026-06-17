package cl.triskeledu.aduanas.pdi.controller;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<List<ConsultaResponse>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<ConsultaResponse>> listarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(consultaService.listarPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<ConsultaResponse> crear(@Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        consultaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
