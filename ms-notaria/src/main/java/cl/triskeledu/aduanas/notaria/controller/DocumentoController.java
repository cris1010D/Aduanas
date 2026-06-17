package cl.triskeledu.aduanas.notaria.controller;

import cl.triskeledu.aduanas.notaria.dto.DocumentoRequest;
import cl.triskeledu.aduanas.notaria.dto.DocumentoResponse;
import cl.triskeledu.aduanas.notaria.service.DocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping
    public ResponseEntity<List<DocumentoResponse>> listarTodos() {
        return ResponseEntity.ok(documentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(documentoService.buscarPorId(id));
    }

    @GetMapping("/poder/{idPoder}")
    public ResponseEntity<List<DocumentoResponse>> listarPorPoder(@PathVariable Integer idPoder) {
        return ResponseEntity.ok(documentoService.listarPorPoder(idPoder));
    }

    @PostMapping
    public ResponseEntity<DocumentoResponse> crear(@Valid @RequestBody DocumentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.crear(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
