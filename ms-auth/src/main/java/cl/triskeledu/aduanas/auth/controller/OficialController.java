package cl.triskeledu.aduanas.auth.controller;

import cl.triskeledu.aduanas.auth.dto.LoginRequest;
import cl.triskeledu.aduanas.auth.dto.LoginResponse;
import cl.triskeledu.aduanas.auth.dto.OficialCreateRequest;
import cl.triskeledu.aduanas.auth.dto.OficialResponse;
import cl.triskeledu.aduanas.auth.dto.OficialUpdateRequest;
import cl.triskeledu.aduanas.auth.service.OficialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/oficiales")
@RequiredArgsConstructor
public class OficialController {

    private final OficialService oficialService;

    /**
     * Endpoint para la autenticación de oficiales en el sistema de aduanas.
     * Mapea la ruta pública requerida: /api/v1/oficiales/auth/login
     * Nota: Si necesitas que sea exactamente /api/v1/auth/login (sin el prefijo /oficiales),
     * puedes usar una ruta absoluta anteponiendo una barra diagonal en la anotación, 
     * o bien separar este endpoint en un AuthController independiente. 
     * Aquí lo incluimos de forma absoluta para cumplir con el requisito estricto:
     */
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(oficialService.login(request));
    }

    @GetMapping
    public ResponseEntity<List<OficialResponse>> listarTodos() {
        return ResponseEntity.ok(oficialService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<OficialResponse>> listarActivos() {
        return ResponseEntity.ok(oficialService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OficialResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(oficialService.buscarPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<OficialResponse> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(oficialService.buscarPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<OficialResponse> crear(@Valid @RequestBody OficialCreateRequest  request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oficialService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficialResponse> actualizar(@PathVariable Integer id,
                                                       @Valid @RequestBody OficialUpdateRequest  request) {
        return ResponseEntity.ok(oficialService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        oficialService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}