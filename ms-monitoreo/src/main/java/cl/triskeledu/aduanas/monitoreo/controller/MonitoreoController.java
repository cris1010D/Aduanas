package cl.triskeledu.aduanas.monitoreo.controller;

import cl.triskeledu.aduanas.monitoreo.dto.EstadoMsResponse;
import cl.triskeledu.aduanas.monitoreo.service.MonitoreoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monitoreo")
@RequiredArgsConstructor
public class MonitoreoController {

    private final MonitoreoService monitoreoService;

    @GetMapping
    public ResponseEntity<List<EstadoMsResponse>> listarEstadoTodos() {
        return ResponseEntity.ok(monitoreoService.listarEstadoTodos());
    }

    @GetMapping("/{nombreMs}")
    public ResponseEntity<EstadoMsResponse> consultarEstadoPorNombre(@PathVariable String nombreMs) {
        return ResponseEntity.ok(monitoreoService.consultarEstadoPorNombre(nombreMs));
    }
}
