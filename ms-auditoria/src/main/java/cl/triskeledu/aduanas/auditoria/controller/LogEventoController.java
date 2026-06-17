package cl.triskeledu.aduanas.auditoria.controller;

import cl.triskeledu.aduanas.auditoria.dto.LogEventoRequest;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.service.LogEventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogEventoController {

    private final LogEventoService logEventoService;

    @GetMapping
    public ResponseEntity<List<LogEventoResponse>> listarTodos() {
        return ResponseEntity.ok(logEventoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogEventoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(logEventoService.buscarPorId(id));
    }

    @GetMapping("/oficial/{rut}")
    public ResponseEntity<List<LogEventoResponse>> listarPorOficial(@PathVariable String rut) {
        return ResponseEntity.ok(logEventoService.listarPorOficial(rut));
    }

    @GetMapping("/ms/{msOrigen}")
    public ResponseEntity<List<LogEventoResponse>> listarPorMs(@PathVariable String msOrigen) {
        return ResponseEntity.ok(logEventoService.listarPorMs(msOrigen));
    }

    @PostMapping
    public ResponseEntity<LogEventoResponse> crear(@Valid @RequestBody LogEventoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logEventoService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        logEventoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
